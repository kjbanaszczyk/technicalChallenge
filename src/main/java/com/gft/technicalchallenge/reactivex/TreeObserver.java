package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import rx.Observer;

import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

class TreeObserver implements Observer<FileTree> {

    @Override
    public void onCompleted() {
        System.out.print("Completed");
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onNext(FileTree fileTree) {
        for(WatchEvent<?> event : fileTree.obtainEvents()){
            switch (event.kind().name()){
                case "ENTRY_MODIFY":
                    System.out.println("Modified: " + event.context());
                    break;
                case "ENTRY_DELETE":
                    System.out.println("Deleted " + event.context());
                    fileTree.removeChildrenNode(new FileTree(fileTree.toString()+"\\"+event.context()));
                    break;
                case "ENTRY_CREATE":
                    System.out.println("Created " + event.context());
                    fileTree.addChildrenNode(new FileTree(fileTree.toString()+"\\"+event.context()));
                    break;
                default:
                    break;
            }
        }
    }
}