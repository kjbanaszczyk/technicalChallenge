package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.model.Event;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ThreadSafe
public class TreeReactiveStream implements AutoCloseable {

    private enum State {
        NEW, RUNNING, CLOSED
    }

    private static final Logger LOGGER = Logger.getLogger(TreeReactiveStream.class.getName());
    private WatchService service;
    private Path path;
    private Observable<Event> observable;
    private CustomClosable onClosing;
    private AtomicReference<State> state = new AtomicReference<>(State.NEW);

    TreeReactiveStream(Path path) throws IOException {
        this(path, () -> {
        });
        service = FileSystems.getDefault().newWatchService();
    }

    public TreeReactiveStream(Path path, CustomClosable onClosing) throws IOException {
        this.path = path;
        this.onClosing = onClosing;
        service = FileSystems.getDefault().newWatchService();
    }

    public void initialize() throws IOException {
        if (state.get() == State.NEW) {
            IterableTree<FileTree> iterableTree = new IterableTree<>(new FileTree(path));
            path.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

            List<Event> convertedList = StreamSupport.stream(iterableTree.spliterator(), false).
                    map(o -> {
                        try {
                            if (o.getPath().toFile().isDirectory())
                                o.getPath().register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return new Event(o.getPath().toString(), o.getPath().getFileName().toString(), "Existing");
                    })
                    .collect(Collectors.toList());

            Observable<Event> creates = Observable.fromCallable(new EventObtainer()).flatMap(Observable::from).subscribeOn(Schedulers.io()).repeat().doOnUnsubscribe(() -> {
                try {
                    close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).share().startWith(Observable.from(convertedList).subscribeOn(Schedulers.io()));

            if (state.compareAndSet(State.NEW, State.RUNNING)) {
                observable = creates;
            }
        }
    }

    public Observable<Event> getObservable() throws ClosedWatchServiceException{
        if(state.get()==State.CLOSED)
            throw new ClosedWatchServiceException();
        return observable;
    }

    private class EventObtainer implements Callable<List<Event>> {
        @Override
        public List<Event> call() throws InterruptedException {
            WatchKey key = service.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            List<Event> events = convertWatchEvent(watchEvents, key);
            registerNewDirectories(events);
            key.reset();
            return events;

        }
    }

    private static List<Event> convertWatchEvent(List<WatchEvent<?>> watchEvents, WatchKey key) {
        return watchEvents.stream().map(watchEvent -> new Event(watchEvent, (Path) key.watchable())).collect(Collectors.toList());
    }

    private void registerNewDirectories(List<Event> events) {
        for (Event event : events) {
            Path fullPath = Paths.get(event.getPath());
            if (event.getEventType().equals("ENTRY_CREATE")) {
                if (fullPath.toFile().isDirectory()) {
                    try {
                        fullPath.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("Registered " + fullPath.toAbsolutePath());
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (state.compareAndSet(State.RUNNING, State.CLOSED)) {
            onClosing.onClosing();
            service.close();
        }
    }

    @FunctionalInterface
    public interface CustomClosable {
        void onClosing();
    }

}


