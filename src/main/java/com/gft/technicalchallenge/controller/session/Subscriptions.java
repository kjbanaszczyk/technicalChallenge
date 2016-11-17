package com.gft.technicalchallenge.controller.session;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import rx.Subscription;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
@SessionScope
public class Subscriptions implements AutoCloseable{

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
        Subscription subscription = getSub(key);
        subscription.unsubscribe();
        WSEndPointSubscriptions.remove(key);
    }

    @Override
    public void close() throws IOException {
        WSEndPointSubscriptions.forEach((s, subscription) -> {subscription.unsubscribe(); LOGGER.info("Unsubscribed");});
    }
}