package com.gft.technicalchallenge.filetree;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileTreeTest {

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
    public void shouldFileTreeCorrectlyReturnItsChildren(){
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        Iterable<FileTree> children = tree.getChildren();

        Assertions.assertThat((Lists.newArrayList(children)).size()).isEqualTo(3);
    }

    @Test
    public void shouldFileTreeNotReturnChildrenOnFile(){
        FileTree tree = new FileTree(path+pathToResource+firstDirectory+firstFile);

        Iterable<FileTree> children = tree.getChildren();

        Assertions.assertThat((Lists.newArrayList(children)).size()).isEqualTo(0);
    }

    @Test
    public void shouldFileTreeCorrectlyPrintsItPath(){
        FileTree tree = new FileTree(path+pathToResource+firstDirectory+firstFile);

        String actualToString = tree.toString();

        Assertions.assertThat(actualToString).isEqualTo(path+pathToResource+firstDirectory+firstFile);
    }

    @Test
    public void shouldFileTreeBeRegisteredWhenDirectory() throws IOException {
        WatchService service = FileSystems.getDefault().newWatchService();
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        tree.registerFileTree(service);
        service.close();

        Assertions.assertThat(tree.isRegistered()).isEqualTo(true);
    }

    @Test
    public void shouldObtainEventsWhenDirectoryIsChanged() throws IOException {
        WatchService service = FileSystems.getDefault().newWatchService();
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        tree.registerFileTree(service);
        Files.createFile(Paths.get(path+pathToResource+firstDirectory+"\\test"));
        List<WatchEvent<?>> events = tree.obtainEvents();

        Assertions.assertThat(events.size()).isEqualTo(1);
    }

    @Test
    public void shouldNotObtainAnyEventsWhenNotRegistered() throws IOException {
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        List<WatchEvent<?>> events = tree.obtainEvents();

        Assertions.assertThat(events.size()).isEqualTo(0);
    }

    @Test
    public void shouldBeNotEqualsWithNull() {
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        Assertions.assertThat(tree.equals(null)).isFalse();
    }

    @Test
    public void shouldBeEqualsWithSelf() {
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);

        Assertions.assertThat(tree.equals(tree)).isTrue();
    }

    @Test
    public void shouldBeNotEqualsWithDifferentPath() {
        FileTree tree = new FileTree(path+pathToResource+firstDirectory);
        FileTree tree2 = new FileTree(path+pathToResource+secondDirectory);

        Assertions.assertThat(tree.equals(tree2)).isFalse();
    }





}