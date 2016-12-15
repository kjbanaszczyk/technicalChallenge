package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream.CustomClosable;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
@ThreadSafe
public final class TreeReactiveStreamFactory implements AutoCloseable {

    private final ConcurrentHashMap<Path, TreeReactiveStream> reactiveStreams = new ConcurrentHashMap<>();
    private final static Logger LOGGER = Logger.getLogger(TreeReactiveStreamFactory.class.getName());

    public TreeReactiveStream getReactiveStream(Path path) throws IOException {
        return reactiveStreams.computeIfAbsent(path, this::produce);
    }

    private TreeReactiveStream produce(Path path) {
        CustomClosable onClosing = () -> {
            reactiveStreams.remove(path);
            LOGGER.info("Size is " + reactiveStreams.size());
        };

        TreeReactiveStream stream = new TreeReactiveStream(path, onClosing);

        try {
            stream.initialize();
        } catch (IOException e) {
            return null;
        }

        return stream;
    }


    @Override
    public void close() throws IOException {
        reactiveStreams.forEach((s, treeReactiveStream) -> {
            try {
                treeReactiveStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.reactiveStreams.clear();
    }

}