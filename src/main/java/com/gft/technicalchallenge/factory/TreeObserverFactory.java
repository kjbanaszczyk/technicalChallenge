package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import sun.reflect.generics.tree.Tree;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public final class TreeObserverFactory {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private AtomicInteger endPointAccumulator = new AtomicInteger(0);

    public TreeObserver getObserver() throws IOException {

        return new TreeObserver(String.valueOf(endPointAccumulator.incrementAndGet()), simpMessagingTemplate);
    }

}
