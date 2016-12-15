package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.model.Event;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ThreadSafe
public class TreeReactiveStream implements AutoCloseable {


    private enum State {
        NEW, RUNNING, CLOSED, INITIALIZING
    }

    private static final Logger LOGGER = Logger.getLogger(TreeReactiveStream.class.getName());
    private WatchService service;
    private Path path;
    private Observable<Event> observable;

    private CustomClosable onClosing;
    private AtomicReference<State> state = new AtomicReference<>(State.NEW);

    TreeReactiveStream(Path path) {
        this(path, () -> {
        });
    }

    public TreeReactiveStream(Path path, CustomClosable onClosing) {
        this.path = path;
        this.onClosing = onClosing;
    }

    public void initialize() throws IOException {
        if (!state.compareAndSet(State.NEW, State.INITIALIZING)) return;

        service = FileSystems.getDefault().newWatchService();

        IterableTree<FileTree> iterableTree = new IterableTree<>(new FileTree(path));
        path.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

        iterableTree.forEach(o -> {
            try {
                if (o.getPath().toFile().isDirectory())
                    o.getPath().register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        observable = Observable.fromCallable(new EventObtainer()).flatMap(Observable::from).subscribeOn(Schedulers.io()).repeat().doOnUnsubscribe(() -> {
            try {
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).share().startWith(Observable.from(iterableTree).map(
                o -> new Event(o.getPath().toString(), o.getPath().getFileName().toString(), "Existing")));

        state.set(State.RUNNING);
    }

    public Observable<Event> getObservable() {
        if (state.get() == State.CLOSED)
            return Observable.error(new ClosedWatchServiceException());

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

    /**
     * Closes only resources without invoking onClosing()
     * @throws IOException - if an I/O error occurs
     */
    void resourceClose() throws IOException {
        if(state.compareAndSet(State.RUNNING, State.CLOSED)) {
            this.service.close();
        }
    }

    @FunctionalInterface
    public interface CustomClosable {
        void onClosing();
    }

}


