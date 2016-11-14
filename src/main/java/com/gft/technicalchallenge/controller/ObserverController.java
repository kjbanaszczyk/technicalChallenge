package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.controller.scheduler.SessionManager;
import com.gft.technicalchallenge.controller.scheduler.Subscriptions;
import com.gft.technicalchallenge.factory.TreeObserverFactory;
import com.gft.technicalchallenge.factory.TreeReactiveStreamFactory;
import com.gft.technicalchallenge.model.Event;
import com.gft.technicalchallenge.reactivex.TreeObserver;
import com.gft.technicalchallenge.reactivex.TreeReactiveStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextListener;
import rx.Observable;
import rx.observables.ConnectableObservable;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/app")
final class ObserverController {

    private static java.util.logging.Logger Logger = java.util.logging.Logger.getLogger(ObserverController.class.getName());

    @Autowired
    private TreeReactiveStreamFactory treeReactiveStreamFactory;

    @Autowired
    private TreeObserverFactory treeObserverFactory;

    @Autowired
    private SessionManager manager;

    private static final String SUBSCRIPTION = "Subscriptions";

    @Autowired
    private Subscriptions subscriptions;

    @CrossOrigin
    @RequestMapping(path = "/start", method = RequestMethod.POST)
    ResponseEntity<String> startObserving(@RequestBody String path, HttpSession httpSession) throws IOException {

        TreeReactiveStream treeReactiveStream = treeReactiveStreamFactory.getReactiveStream(Paths.get(path));
        Observable<Event> observable = treeReactiveStream.getObservable();

        TreeObserver observer = treeObserverFactory.getObserver();
        subscriptions.addSub(observer.getEndPoint(), observable.subscribe(observer));
        Logger.info("Endpoint " + observer.getEndPoint());

        httpSession.setAttribute(SUBSCRIPTION, subscriptions);

        return new ResponseEntity<>(observer.getEndPoint(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/endSession", method = RequestMethod.POST)
    ResponseEntity endSession(HttpSession httpSession){
        Logger.info(httpSession.getId());
        httpSession.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/stop/{websocket}", method = RequestMethod.POST)
    ResponseEntity stopObservingOnEndPoint(HttpSession httpSession, @PathVariable String websocket) throws IOException {

        Logger.info("Endpoint on close " + websocket);
        subscriptions.unsubscribe(websocket);
        return new ResponseEntity(HttpStatus.OK);

    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<String> exceptionFileNotFoundHandler(Exception ex){

        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(NotDirectoryException.class)
    public ResponseEntity<String> exceptionNotDirectoryHandler(Exception ex){

        return new ResponseEntity<>("Not directory", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}