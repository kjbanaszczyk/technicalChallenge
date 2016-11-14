package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.model.Event;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TreeReactiveStream implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(TreeReactiveStream.class.getName());
    private WatchService service;
    private Path path;
    private Observable<Event> observable;

    public TreeReactiveStream(Path path) throws IOException {
        service = FileSystems.getDefault().newWatchService();
        this.path = path;
    }

    public Observable<Event> getObservable() throws IOException {
        if(observable == null) {
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

            observable = Observable.fromCallable(new EventObtainer()).flatMap(Observable::from).subscribeOn(Schedulers.io()).repeat().share();
        }

        return observable;
    }

    private Future<List<Event>> obtainEvents(){
        return new Future<List<Event>>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public List<Event> get() throws InterruptedException, ExecutionException {
                WatchKey key = service.take();
                List<WatchEvent<?>> watchEvents = key.pollEvents();
                List<Event> events = convertWatchEvent(watchEvents,key);
                registerNewDirectories(events);
                key.reset();
                return events;
            }

            @Override
            public List<Event> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    private class EventObtainer implements Callable<List<Event>> {

        @Override
        public List<Event> call() throws InterruptedException {
            WatchKey key = service.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            List<Event> events = convertWatchEvent(watchEvents,key);
            registerNewDirectories(events);
            key.reset();
            return events;
        }
    }

    private static List<Event> convertWatchEvent(List<WatchEvent<?>> watchEvents, WatchKey key){
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
        service.close();
    }
}
