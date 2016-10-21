package com.gft.technicalchallenge;

public interface Node<T extends Node> {

    boolean isNode();
    Iterable<T> getChildren();

}