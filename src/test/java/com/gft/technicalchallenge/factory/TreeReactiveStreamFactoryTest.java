package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class TreeReactiveStreamFactoryTest {

    @Test
    public void shouldReturnOneReactiveStreamForSamePath() throws IOException {

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();

        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get("C://"));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get("C://"));

        Assertions.assertThat(streamFirst).isEqualTo(streamSecond);
    }

    @Test
    public void shouldReturnTwoDifferentReactiveStreamForDifferentPath() throws IOException {

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();

        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get("C://"));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get("D://"));

        Assertions.assertThat(streamFirst).isNotEqualTo(streamSecond);
    }


}