package com.gft.technicalchallenge;

import com.gft.technicalchallenge.reactivex.TreeObserver;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import rx.Observable;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

public class Main {

    public static void main(String... args) throws InterruptedException, IOException {

        TreeReactiveStream treeReactiveStream = new TreeReactiveStream();
        Observable<WatchEvent<?>> event = treeReactiveStream.convertDirectoryToObservable(Paths.get("C:\\Program Files"));
        event.subscribe(new TreeObserver());

    }

}
