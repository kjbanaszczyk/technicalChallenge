package com.gft.technicalchallenge.filetree;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Paths;

public class FileTreeTest {

    @Rule
    public TemporaryFolder folder1 = new TemporaryFolder();

    @Test
    public void shouldFileTreeCorrectlyReturnItsChildren() throws IOException {
        FileTree tree = new FileTree(Paths.get(folder1.getRoot().getPath()));
        folder1.newFile("test");
        folder1.newFile("test2");
        folder1.newFile("test3");

        Iterable<FileTree> children = tree.getChildren();

        Assertions.assertThat((Lists.newArrayList(children)).size()).isEqualTo(3);
    }

    @Test
    public void shouldFileTreeNotReturnChildrenOnFile() throws IOException {
        FileTree tree = new FileTree(Paths.get(folder1.newFile("test").getPath()));

        Iterable<FileTree> children = tree.getChildren();

        Assertions.assertThat((Lists.newArrayList(children)).size()).isEqualTo(0);
    }

    @Test
    public void shouldFileTreeCorrectlyPrintsItPath(){
        FileTree tree = new FileTree(Paths.get(folder1.getRoot().getPath()));

        String actualToString = tree.toString();

        Assertions.assertThat(actualToString).isEqualTo(folder1.getRoot().getPath());
    }

//    @Test
//    public void shouldFileTreeBeRegisteredWhenDirectory() throws IOException {
//        WatchService service = FileSystems.getDefault().newWatchService();
//        FileTree tree = new FileTree(Paths.get(path+pathToResource+firstDirectory));
//
//        tree.registerFileTree(service);
//        service.close();
//
//        Assertions.assertThat(tree.isRegistered()).isEqualTo(true);
//    }
//
//    @Test
//    public void shouldObtainEventsWhenDirectoryIsChanged() throws IOException {
//        WatchService service = FileSystems.getDefault().newWatchService();
//        FileTree tree = new FileTree(path+pathToResource+firstDirectory);
//
//        tree.registerFileTree(service);
//        Files.createFile(Paths.get(path+pathToResource+firstDirectory+"\\test"));
//        List<WatchEvent<?>> events = tree.obtainEvents();
//
//        Assertions.assertThat(events.size()).isEqualTo(1);
//    }
//
//    @Test
//    public void shouldNotObtainAnyEventsWhenNotRegistered() throws IOException {
//        FileTree tree = new FileTree(path+pathToResource+firstDirectory);
//
//        List<WatchEvent<?>> events = tree.obtainEvents();
//
//        Assertions.assertThat(events.size()).isEqualTo(0);
//    }

    @Test
    public void shouldBeNotEqualsWithNull() {
        FileTree tree = new FileTree(Paths.get(folder1.getRoot().getPath()));

        Assertions.assertThat(tree).isNotNull();
    }

    @Test
    public void shouldBeEqualsWithSelf() {
        FileTree tree = new FileTree(Paths.get(folder1.getRoot().getPath()));

        Assertions.assertThat(tree).isEqualTo(tree);
    }

    @Test
    public void shouldBeNotEqualsWithDifferentPath() throws IOException {
        FileTree tree = new FileTree(Paths.get(folder1.getRoot().getPath()));
        FileTree tree2 = new FileTree(Paths.get(folder1.newFile("test").getPath()));

        Assertions.assertThat(tree.equals(tree2)).isFalse();
    }

}