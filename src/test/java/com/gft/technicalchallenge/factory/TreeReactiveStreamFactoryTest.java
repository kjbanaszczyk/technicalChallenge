package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Paths;

public class TreeReactiveStreamFactoryTest {


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldReturnOneReactiveStreamForSamePath() throws IOException {

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();

        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.getRoot().getPath()));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.getRoot().getPath()));

        Assertions.assertThat(streamFirst).isSameAs(streamSecond);
    }

    @Test
    public void shouldReturnTwoDifferentReactiveStreamForDifferentPath() throws IOException {

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();

        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.getRoot().getPath()));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.newFolder("test").getPath()));

        Assertions.assertThat(streamFirst).isNotSameAs(streamSecond);

    }

    @Test
    public void shouldCloseAllReactiveStreams() throws IOException{

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();
        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.getRoot().getPath()));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get(temporaryFolder.newFolder("test").getPath()));

        treeReactiveStreamFactory.close();

        // TODO: 01/12/2016 Implement assertions

    }

}