package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.model.Event;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class TreeReactiveStreamTest {

    @Rule
    public TemporaryFolder folder1 = new TemporaryFolder();

    @Test(timeout = 1000)
    public void shouldEmitEventOnNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));

        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        Files.createFile(Paths.get(folder1.getRoot().getPath() + "/test"));

        testSubscriber.toBlocking().first();
    }

    @Test(timeout = 1000)
    public void shouldCloseItselfWhenAllSubscribersUnsubscribes() throws IOException {

        TreeReactiveStream.CustomClosable closable = mock(TreeReactiveStream.CustomClosable.class);

        TestSubscriber<Event> testSubscriberFirst = new TestSubscriber<>();
        TestSubscriber<Event> testSubscriberSecond = new TestSubscriber<>();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()), closable);

        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriberFirst);
        observable.subscribe(testSubscriberSecond);

        verify(closable, times(0)).onClosing();

        testSubscriberFirst.unsubscribe();

        verify(closable, times(0)).onClosing();

        testSubscriberSecond.unsubscribe();

        verify(closable, times(1)).onClosing();

    }

    @Test
    public void shouldReturnNewObservableAfterClose() throws IOException {

        TreeReactiveStream treeReactiveStream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        Observable<Event> instanceFirst = treeReactiveStream.getObservable();

        treeReactiveStream.close();
        Observable<Event> instanceSecond = treeReactiveStream.getObservable();

        Assertions.assertThat(instanceFirst).isNotSameAs(instanceSecond);

    }

    @Test
    public void shouldReopenResourceAfterClose() throws IOException {

        TreeReactiveStream treeReactiveStream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        treeReactiveStream.getObservable();
        treeReactiveStream.close();
        Assertions.assertThatThrownBy(treeReactiveStream::close).isInstanceOf(ClosedWatchServiceException.class);

        treeReactiveStream.getObservable();

        Assertions.assertThatThrownBy(treeReactiveStream::close).isNull();
    }

    @Test(timeout = 1000)
    public void shouldRegisterNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));

        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        if (new File(folder1.getRoot().getPath() + "/testDir").mkdir())
            Files.createFile(Paths.get(folder1.getRoot().getPath() + "/testDir" + "/test"));

        // we expected to watch element created by event related to subfolder.
        testSubscriber.toBlocking().first();
    }

    @Test
    public void shouldConvertDirectoryToObservable() throws IOException {

        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        Assertions.assertThat(stream.getObservable()).isInstanceOf(Observable.class);

    }

    @Test
    public void shouldCloseResources() throws Exception {
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));

        stream.getObservable();
        stream.close();

        Assertions.assertThatThrownBy(stream::close).isInstanceOf(ClosedWatchServiceException.class);
    }

}