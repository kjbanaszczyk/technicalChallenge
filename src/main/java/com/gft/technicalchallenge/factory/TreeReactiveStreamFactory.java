package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

@Service
public final class TreeReactiveStreamFactory implements AutoCloseable {

    private final HashMap<String, TreeReactiveStream> reactiveStreams = new HashMap<>();

    public TreeReactiveStream getReactiveStream(Path path) throws IOException {

        TreeReactiveStream stream = reactiveStreams.get(path.toString());

        if (stream == null) {
            stream = new TreeReactiveStream(path);
            reactiveStreams.put(path.toString(), stream);
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
