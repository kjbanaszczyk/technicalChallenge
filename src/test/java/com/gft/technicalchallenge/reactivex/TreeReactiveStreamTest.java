package com.gft.technicalchallenge.reactivex;
import com.gft.technicalchallenge.model.Event;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subjects.ReplaySubject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public final class TreeReactiveStreamTest {

    private final static String pathToResource = "\\src\\test\\Resources\\FileTree";
    private final static String firstDirectory = "\\Directory1";
    private final static String firstFile = "\\Directory1\\emptyFile.txt";
    private final static String secondDirectory = "\\Directory1\\Directory2";
    private final static String secondFile = "\\Directory1\\file2";
    private final static String path = new File("").getAbsolutePath();

    @Before
    public void SetUpDirectoryWithTwoFolders() throws IOException, InitializationError {
        Path existing = Paths.get(path+pathToResource+firstDirectory);
        FileUtils.deleteDirectory(existing.toFile());
        if(!(new File(path+pathToResource+firstDirectory).mkdir()))
            SetUpDirectoryWithTwoFolders();
        if(!(new File(path+pathToResource+firstFile).createNewFile()))
            SetUpDirectoryWithTwoFolders();
        if(!(new File(path+pathToResource+secondDirectory).mkdir()))
            SetUpDirectoryWithTwoFolders();
        if(!(new File(path+pathToResource+secondFile).createNewFile()))
            SetUpDirectoryWithTwoFolders();
    }

    @Test(timeout = 1000)
    public void shouldEmitEventOnNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(path + pathToResource));

        Observable<Event> observable = stream.createObservable();
        observable.subscribe(testSubscriber);
        Files.createFile(Paths.get(path+pathToResource+firstDirectory+"\\test"));

        testSubscriber.toBlocking().first();

    }

    @Test(timeout = 1000)
    public void shouldRegisterNewDirectory() throws IOException, InterruptedException {

        ReplaySubject<Event> testSubscriber = ReplaySubject.create();
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(path + pathToResource));

        Observable<Event> observable = stream.createObservable();
        observable.subscribe(testSubscriber);
        if(new File(path+pathToResource+firstDirectory+"\\testDir").mkdir())
            Files.createFile(Paths.get(path+pathToResource+firstDirectory+"\\testDir"+"\\test"));

        // we expected to watch element created by event related to subfolder.
        testSubscriber.toBlocking().first();
    }

    @Test
    public void shouldConvertDirectoryToObservable() throws IOException {

        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(path + pathToResource));
        Assertions.assertThat(stream.createObservable()).isInstanceOf(Observable.class);

    }

    @Test
    public void shouldCloseResources() throws Exception {
        TreeReactiveStream stream = new TreeReactiveStream(Paths.get(path + pathToResource));
        stream.close();

        Assertions.assertThatThrownBy(stream::createObservable).isInstanceOf(ClosedWatchServiceException.class);
    }

}