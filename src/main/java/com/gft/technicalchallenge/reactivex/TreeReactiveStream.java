package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import com.gft.technicalchallenge.filetree.FileTree;
import rx.Observable;

import java.io.File;
import java.io.IOException;
import java.nio.file.WatchService;

public class TreeReactiveStream implements Runnable{

    private IterableTree<FileTree> tree;

    private WatchService service;

    public TreeReactiveStream(IterableTree<FileTree> tree, WatchService service) {
        this.tree = tree;
        this.service = service;
    }

    private Observable<FileTree> registerAllFiles(){
        return Observable.from(tree).map(o -> {
            try {
                return o.registerFileTree(service);
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public void startWatchingFiles(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        registerAllFiles().filter(o -> o!=null).repeat().subscribe(new TreeObserver());
    }
}
