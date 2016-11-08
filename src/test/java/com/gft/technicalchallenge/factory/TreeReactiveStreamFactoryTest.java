package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldCloseAllReactiveStreams() throws IOException{

        TreeReactiveStreamFactory treeReactiveStreamFactory = new TreeReactiveStreamFactory();
        TreeReactiveStream streamFirst = treeReactiveStreamFactory.getReactiveStream(Paths.get("C://"));
        TreeReactiveStream streamSecond = treeReactiveStreamFactory.getReactiveStream(Paths.get("D://"));

        treeReactiveStreamFactory.close();

        Assertions.assertThatThrownBy(streamFirst::createObservable).isInstanceOf(ClosedWatchServiceException.class);
        Assertions.assertThatThrownBy(streamSecond::createObservable).isInstanceOf(ClosedWatchServiceException.class);

    }


}