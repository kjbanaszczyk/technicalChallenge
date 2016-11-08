package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeObserver;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class TreeObserverFactoryTest {

    @Test
    public void shouldReturnOneReactiveStreamForSamePath() throws IOException {

        TreeObserverFactory treeObserverFactory = new TreeObserverFactory();

        TreeObserver observerFirst = treeObserverFactory.getObserver("C://");
        TreeObserver observerSecond = treeObserverFactory.getObserver("C://");

        Assertions.assertThat(observerFirst).isSameAs(observerSecond);
    }

    @Test
    public void shouldReturnTwoDifferentReactiveStreamForDifferentPath() throws IOException {

        TreeObserverFactory treeObserverFactory = new TreeObserverFactory();

        TreeObserver observerFirst = treeObserverFactory.getObserver("C://");
        TreeObserver observerSecond = treeObserverFactory.getObserver("D://");

        Assertions.assertThat(observerFirst).isNotSameAs(observerSecond);

    }


    @Test
    public void shouldClearEverythingOnCl() throws IOException{

        TreeObserverFactory treeObserverFactory = new TreeObserverFactory();
        TreeObserver observerFirst = treeObserverFactory.getObserver("C://");
        TreeObserver observerSecond = treeObserverFactory.getObserver("D://");

        treeObserverFactory.removeAll();
        TreeObserver observerFirstAfterRemove = treeObserverFactory.getObserver("C://");
        TreeObserver observerSecondAfterRemove = treeObserverFactory.getObserver("D://");

        Assertions.assertThat(observerFirst).isNotSameAs(observerFirstAfterRemove);
        Assertions.assertThat(observerSecond).isNotSameAs(observerSecondAfterRemove);

    }
}