package com.gft.technicalchallenge;

public interface Node<T extends Node> {

    Iterable<T> getChildren();

}