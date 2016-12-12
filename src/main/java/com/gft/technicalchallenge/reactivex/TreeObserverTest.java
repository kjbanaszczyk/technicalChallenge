package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.model.Event;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rx.Subscriber;

import java.util.logging.Logger;

public final class TreeObserverTest extends Subscriber<Event> {

    private static final Logger LOGGER = Logger.getLogger(TreeObserverTest.class.getName());

    public TreeObserverTest() {
    }

    @Override
    public void onCompleted() {
        LOGGER.info("Completed");
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onNext(Event event) {
        LOGGER.info(event.getEventType() + event.getPath() + event.getFileName());
    }

}