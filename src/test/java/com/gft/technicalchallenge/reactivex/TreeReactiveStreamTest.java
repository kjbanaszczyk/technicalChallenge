package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.model.Event;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.Iterables;
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
        stream.initialize();
        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        Files.createFile(Paths.get(folder1.getRoot().getPath() + "/test"));

        testSubscriber.toBlocking().first();
    }

    @Test(timeout = 10000)
    public void shouldEmitExistingFilesInDirectory() throws IOException, InterruptedException {

        folder1.newFile("test1");
        folder1.newFile("test2");
        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        stream.initialize();
        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);

        Assertions.assertThat(testSubscriber.getValues()).hasSize(2);
    }

    @Test(timeout = 1000)
    public void shouldCloseItselfWhenAllSubscribersUnsubscribes() throws IOException {

        TreeReactiveStream.CustomClosable closable = mock(TreeReactiveStream.CustomClosable.class);

        TestSubscriber<Event> testSubscriberFirst = new TestSubscriber<>();
        TestSubscriber<Event> testSubscriberSecond = new TestSubscriber<>();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()), closable);
        stream.initialize();
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
    public void shouldThrowClosedWatchServiceAfterClose() throws IOException {

        TreeReactiveStream treeReactiveStream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        treeReactiveStream.initialize();
        treeReactiveStream.getObservable();

        treeReactiveStream.close();

        Assertions.assertThat(treeReactiveStream.getObservable());
    }

    @Test(timeout = 3000)
    public void shouldRegisterNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        stream.initialize();
        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        if (new File(folder1.getRoot().getPath() + "/testDir").mkdir())
            Files.createFile(Paths.get(folder1.getRoot().getPath() + "/testDir" + "/test"));

        // we expected to watch element created by event related to subfolder.
        testSubscriber.toBlocking().first();
    }

    @Test(timeout = 3000)
    public void shouldRegisterAllExistingDirectory() throws IOException, InterruptedException {

        File insideFolder = folder1.newFolder("test");

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));
        stream.initialize();
        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);

        Files.createFile(Paths.get(insideFolder.getPath() + "/test"));

        // we expected to watch element created by event related to subfolder.
        testSubscriber.toBlocking().first();
    }

    @Test
    public void shouldInvokeOnClosingWhenClose() throws Exception {

        TreeReactiveStream.CustomClosable closable = mock(TreeReactiveStream.CustomClosable.class);
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()), closable);
        stream.initialize();

        stream.getObservable();
        stream.close();

        verify(closable, times(1)).onClosing();

    }

    @Test
    public void shouldNotInvokeOnClosingWhenCloseOnlyResources() throws Exception {

        TreeReactiveStream.CustomClosable closable = mock(TreeReactiveStream.CustomClosable.class);
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()), closable);
        stream.initialize();

        stream.getObservable();
        stream.resourceClose();

        verify(closable, times(0)).onClosing();

    }

}