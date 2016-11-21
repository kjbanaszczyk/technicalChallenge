package com.gft.technicalchallenge.controller.session;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import rx.Subscription;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
@SessionScope
public class Subscriptions implements AutoCloseable, HttpSessionBindingListener {

    private static Logger LOGGER = Logger.getLogger(SessionManager.class.getName());

    private ConcurrentHashMap<String, Subscription> WSEndPointSubscriptions;

    public Subscriptions() {
        this.WSEndPointSubscriptions = new ConcurrentHashMap<>();
    }

    public void addSub(String key, Subscription subscription) {
        WSEndPointSubscriptions.put(key, subscription);
    }

    public Subscription getSub(String key) {
        return WSEndPointSubscriptions.get(key);
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
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LOGGER.info("Bound " + event.getName());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {

    }
}