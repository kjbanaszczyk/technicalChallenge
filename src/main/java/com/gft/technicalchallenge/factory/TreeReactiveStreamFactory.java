package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream.CustomClosable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public final class TreeReactiveStreamFactory implements AutoCloseable {

    private final ConcurrentHashMap<String, TreeReactiveStream> reactiveStreams = new ConcurrentHashMap<>();
    private final static Logger LOGGER = Logger.getLogger(TreeReactiveStreamFactory.class.getName());

    public TreeReactiveStream getReactiveStream(Path path) throws IOException {

        TreeReactiveStream stream = reactiveStreams.get(path.toString());

        if (stream != null) return stream;

        CustomClosable onClosing = () -> {reactiveStreams.remove(path.toString()); LOGGER.info("Size is " + reactiveStreams.size());};

        stream = new TreeReactiveStream(path, onClosing);

        reactiveStreams.put(path.toString(), stream);

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