package com.gft.technicalchallenge.model;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public final class Event {

    private String path;
    private String fileName;
    private String eventType;

    public Event() {
    }

    public Event(WatchEvent event, Path path){
        this.path = path.resolve((Path) event.context()).toString();
        this.fileName = event.context().toString();
        this.eventType = event.kind().toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
