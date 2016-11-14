package com.gft.technicalchallenge.reactivex;
import com.gft.technicalchallenge.model.Event;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public final class TreeReactiveStreamTest {

    @Rule
    public TemporaryFolder folder1 = new TemporaryFolder();

    @Test(timeout = 1000)
    public void shouldEmitEventOnNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));

        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        Files.createFile(Paths.get(folder1.getRoot().getPath()+"/test"));

        testSubscriber.toBlocking().first();

    }

    @Test(timeout = 1000)
    public void shouldRegisterNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(folder1.getRoot().getPath()));

        Observable<Event> observable = stream.getObservable();
        observable.subscribe(testSubscriber);
        if(new File(folder1.getRoot().getPath()+"/testDir").mkdir())
            Files.createFile(Paths.get(folder1.getRoot().getPath()+"/testDir"+"/test"));

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
        stream.close();

        Assertions.assertThatThrownBy(stream::getObservable).isInstanceOf(ClosedWatchServiceException.class);
    }

}