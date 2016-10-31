package com.gft.technicalchallenge.nodeabstraction;

public interface Node<T extends Node> {

    Iterable<T> getChildren();

}