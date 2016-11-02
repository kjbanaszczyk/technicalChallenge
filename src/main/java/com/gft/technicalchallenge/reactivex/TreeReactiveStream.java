package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import rx.Observable;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class TreeReactiveStream implements AutoCloseable{

    private static final Logger LOGGER = Logger.getLogger( TreeReactiveStream.class.getName() );
    private WatchService service;

    public Observable<WatchEvent<?>> convertDirectoryToObservable(Path path) throws IOException {
        service=FileSystems.getDefault().newWatchService();
        IterableTree<FileTree> iterableTree = new IterableTree<>(new FileTree(path));
        iterableTree.forEach(o -> {
            try {
                if(o.getPath().toFile().isDirectory())
                    o.getPath().register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return Observable.fromCallable(new WatchEventObtainer()).flatMap(Observable::from).repeat();
    }

    private class WatchEventObtainer implements Callable<List<WatchEvent<?>>> {

        @Override
        public List<WatchEvent<?>> call() throws Exception {
            WatchKey key = service.take();
            List<WatchEvent<?>> events = key.pollEvents();
            registerNewDirectories(events, key);
            key.reset();
            return events;
        }
    }

    private void registerNewDirectories(List<WatchEvent<?>> events, WatchKey key){
        Path dir = (Path)key.watchable();
        for(WatchEvent event : events){
            Path fullPath = dir.resolve((Path) event.context());
            if(event.kind().name().equals("ENTRY_CREATE")){
                    try {
                        if(fullPath.toFile().isDirectory()) {
                            fullPath.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
                            LOGGER.info("Registered " + fullPath.toAbsolutePath());
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }
    }

    @Override
    public void close() throws Exception {
        service.close();
    }
}
