package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.factory.TreeObserverFactory;
import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import com.gft.technicalchallenge.model.Event;
import com.gft.technicalchallenge.reactivex.TreeObserver;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/app")
public final class ObserverController {

    @Autowired
    private TreeReactiveStreamFactory treeReactiveStreamFactory;

    @Autowired
    private TreeObserverFactory treeObserverFactory;

    private List<Subscription> subscriptions = new ArrayList<>();

    @CrossOrigin
    @RequestMapping(path = "/start", method = RequestMethod.POST)
    public String startObserving(@RequestBody String path) throws IOException {
        TreeReactiveStream treeReactiveStream = treeReactiveStreamFactory.getReactiveStream(Paths.get(path));
        Observable<Event> observable = treeReactiveStream.createObservable();

        TreeObserver observer = treeObserverFactory.getObserver(path);

        subscriptions.add(observable.subscribeOn(Schedulers.io()).subscribe(observer));
        return observer.getEndPoint();
    }

    @CrossOrigin
    @RequestMapping(path = "/stop", method = RequestMethod.POST)
    public void stopObserving() throws IOException {
        treeReactiveStreamFactory.close();
        treeReactiveStreamFactory.removeAll();
        treeObserverFactory.removeAll();
        subscriptions.forEach(Subscription::unsubscribe);
    }

}
