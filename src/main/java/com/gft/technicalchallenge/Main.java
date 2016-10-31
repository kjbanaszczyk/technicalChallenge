package com.gft.technicalchallenge;

import com.gft.technicalchallenge.filetree.FileTree;
import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;

public class Main {

    public static void main(String... args) throws InterruptedException, IOException {

        FileTree tree = new FileTree("C:\\Intel");

        IterableTree<FileTree> iterableTree = new IterableTree<>(tree);

        WatchService service = FileSystems.getDefault().newWatchService();

        TreeReactiveStream treeReactiveStream = new TreeReactiveStream(iterableTree,service);

        treeReactiveStream.startWatchingFiles();

        while(true){

            System.out.print(service.take().pollEvents().size());
        }

    }

}
