package com.gft.technicalchallenge.reactivex;
import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

public class TreeReactiveStreamTest {

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

    @Test
    public void shouldEmitEventOnNewDirectory() throws IOException, InterruptedException {

        TestSubscriber<WatchEvent> testSubscriber = new TestSubscriber<>();
        TreeReactiveStream stream = new TreeReactiveStream();

        Observable<WatchEvent<?>> observable = stream.convertDirectoryToObservable(Paths.get(path + pathToResource));

        Files.createFile(Paths.get(path+pathToResource+firstDirectory+"\\test"));

        observable.first().subscribe(testSubscriber);

        testSubscriber.assertValueCount(1);
    }
}