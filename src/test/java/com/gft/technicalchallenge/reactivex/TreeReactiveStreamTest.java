package com.gft.technicalchallenge.reactivex;
import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;
import rx.observers.TestSubscriber;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

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
//
//    @Test
//    public void shouldEmitRegisteredDirectories() throws IOException {
//
//        WatchService service = FileSystems.getDefault().newWatchService();
//        TestSubscriber<FileTree> testSubscriber = new TestSubscriber<>();
//        FileTree tree = new FileTree(path+pathToResource+firstDirectory);
//        IterableTree<FileTree> iterableTree = new IterableTree<>(tree);
//
//        TreeReactiveStream stream = new TreeReactiveStream(iterableTree,service);
//        stream.registerAllFiles();
//        stream.(testSubscriber);
//
//        testSubscriber.assertCompleted();
//        testSubscriber.assertReceivedOnNext(Lists.newArrayList(iterableTree.iterator()));
//    }
}