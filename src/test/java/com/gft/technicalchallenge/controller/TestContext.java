package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.controller.session.SessionManager;
import com.gft.technicalchallenge.controller.session.Subscriptions;
import com.gft.technicalchallenge.factory.TreeObserverFactory;
import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.context.annotation.ScopedProxyMode;
class TestContext {

    @Autowired
    private
    SimpMessagingTemplate simpMessagingTemplate;


    @Bean
    ObserverController getObserverController(){
        return new ObserverController();
    }

    @Bean
    TreeReactiveStreamFactory getTreeReactiveStreamFactory(){
        return new TreeReactiveStreamFactory();
    }

    @Bean
    SimpMessagingTemplate getSimpMessagingTemplate(){
        return simpMessagingTemplate;
    }

    @Bean
    TreeObserverFactory getTreeObserverFactory(){
        return new TreeObserverFactory();
    }

    @Bean
    SessionManager getSessionManager() { return new SessionManager();}

    @Bean
    @SessionScope
    Subscriptions getSubscriptions() {return new Subscriptions();}

}
