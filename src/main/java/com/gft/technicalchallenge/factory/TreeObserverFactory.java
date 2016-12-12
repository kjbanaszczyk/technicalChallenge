package com.gft.technicalchallenge.factory;

import com.gft.technicalchallenge.reactivex.TreeObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@ThreadSafe
public final class TreeObserverFactory {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private AtomicInteger endPointAccumulator = new AtomicInteger(0);

    public TreeObserver getObserver() throws IOException {
        return new TreeObserver(String.valueOf(endPointAccumulator.incrementAndGet()), simpMessagingTemplate);

    }

}
