//package com.gft.technicalchallenge.reactivestream;
//
//import com.gft.technicalchallenge.Node;
//import com.sun.istack.internal.NotNull;
//import org.reactivestreams.Publisher;
//import org.reactivestreams.Subscriber;
//
//import java.util.concurrent.Executor;
//
///**
// * Created by klbk on 26/10/2016.
// */
//public class TreePublisher<T extends Node<T>> implements Publisher<T> {
//
//    @NotNull
//    private final Iterable<T> elements;
//    @NotNull
//    private final Executor executor;
//
//    public TreePublisher(Iterable<T> elements, Executor executor) {
//        this.elements = elements;
//        this.executor = executor;
//    }
//
//    @Override
//    public void subscribe(Subscriber<? super T> subscriber) {
//        new TreeSubscription<T>(subscriber);
//    }
//
//
//
//}
