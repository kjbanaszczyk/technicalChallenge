package com.gft.technicalchallenge.reactivex;

import rx.Observer;

import java.nio.file.WatchEvent;

public class TreeObserver implements Observer<WatchEvent> {

    @Override
    public void onCompleted() {
        System.out.print("Completed");
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onNext(WatchEvent event) {
        System.out.println(event.kind() + ": " + event.context());
    }
}