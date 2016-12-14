package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.model.Event;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rx.Subscriber;

import java.util.logging.Logger;

public final class TreeObserver extends Subscriber<Event> {

    private static final Logger LOGGER = Logger.getLogger(TreeObserver.class.getName());
    private SimpMessagingTemplate simpMessagingTemplate;

    public String getEndPoint() {
        return endPoint;
    }

    private String endPoint;

    public TreeObserver(String endPoint, SimpMessagingTemplate simpMessagingTemplate) {
        this.endPoint = endPoint;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @Override
    public void onCompleted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onNext(Event event) {
        LOGGER.info(event.getEventType() + event.getPath() + event.getFileName());
        LOGGER.info("/events/get/" + endPoint);
        simpMessagingTemplate.convertAndSend("/events/get/" + endPoint, event);
    }

}