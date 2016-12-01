package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.controller.session.SessionManager;
import com.gft.technicalchallenge.factory.TreeObserverFactory;
import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

}
