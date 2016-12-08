package com.gft.technicalchallenge.controller.session;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
@SessionScope
public class Subscriptions<T> implements AutoCloseable {

    private static Logger LOGGER = Logger.getLogger(SessionManager.class.getName());

    private ConcurrentHashMap<String, Subscription> WSEndPointSubscriptions;
    private ConcurrentHashMap<String, Subscriber<T>> WSEndPointSubscriber;

    public Subscriptions() {

        this.WSEndPointSubscriptions = new ConcurrentHashMap<>();
        this.WSEndPointSubscriber = new ConcurrentHashMap<>();
    }

    public void addSubscriber(String endPoint, Subscriber<T> subscriber) {
        WSEndPointSubscriber.put(endPoint, subscriber);
    }

    public void subscribe(String endPoint, Observable<T> observable){
        Subscriber<T> subscriber = WSEndPointSubscriber.remove(endPoint);
        WSEndPointSubscriptions.put(endPoint, observable.subscribe(subscriber));
    }

    public void unsubscribe(String key) {
        Subscription currentSubscription = WSEndPointSubscriptions.remove(key);
        if (currentSubscription == null) return;
        currentSubscription.unsubscribe();
    }

    @Override
    public void close() throws IOException {
        WSEndPointSubscriptions.forEach((s, subscription) -> {
            subscription.unsubscribe();
            LOGGER.info("Unsubscribed");
        });
        WSEndPointSubscriber.clear();
    }
}