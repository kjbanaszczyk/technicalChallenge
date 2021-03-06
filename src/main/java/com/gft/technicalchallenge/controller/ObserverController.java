package com.gft.technicalchallenge.controller;

import com.gft.technicalchallenge.controller.session.SessionManager;
import com.gft.technicalchallenge.controller.session.Subscriptions;
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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;

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
    private Subscriptions<Event> subscriptions;

    @CrossOrigin
    @RequestMapping(path = "/start/{endPoint}", method = RequestMethod.POST)
    @ResponseBody ResponseEntity startObserving(@RequestBody String path, @PathVariable String endPoint, HttpSession httpSession) throws IOException {

        TreeReactiveStream treeReactiveStream = treeReactiveStreamFactory.getReactiveStream(Paths.get(path));

        if(treeReactiveStream!=null)
            subscriptions.subscribe(endPoint, treeReactiveStream.getObservable());
        else
            throw new IOException();

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @CrossOrigin
    @RequestMapping(path = "/obtainEndPoint", method = RequestMethod.GET)
    @ResponseBody ResponseEntity<String> obtainEndPoint() throws IOException {
        TreeObserver observer = treeObserverFactory.getObserver();

        subscriptions.addSubscriber(observer.getEndPoint(), observer);

        return new ResponseEntity<>(observer.getEndPoint(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/endSession", method = RequestMethod.POST)
    ResponseEntity endSession(HttpSession httpSession){
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

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> exceptionFileNotFoundHandler(Exception ex){
        System.out.println(new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND).toString());
        return new ResponseEntity<>("\"Could not observe given directory (don't exist or is no directory)\"", HttpStatus.NOT_FOUND);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}