package com.gft.technicalchallenge.controller.scheduler;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import rx.Subscription;

import java.util.concurrent.ConcurrentHashMap;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Subscriptions {

    private ConcurrentHashMap<String, Subscription> WSSubscriptions;

    public Subscriptions(ConcurrentHashMap<String, Subscription> WSSubscriptions) {
        this.WSSubscriptions = WSSubscriptions;
    }

    public Subscriptions() {
        this.WSSubscriptions = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Subscription> getWSSubscriptions() {
        return WSSubscriptions;
    }

    public void addSub(String key, Subscription subscription) {
        WSSubscriptions.put(key, subscription);
    }

    public Subscription getSub(String key) {
        return WSSubscriptions.get(key);
    }

    public void unsubscribe(String key) {
        Subscription subscription = getSub(key);
        subscription.unsubscribe();
        WSSubscriptions.remove(key);
    }

}
