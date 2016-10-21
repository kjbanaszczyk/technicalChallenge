package com.gft.technicalchallenge;

public interface Tree<T extends Tree> extends Iterable<T> {

    boolean isNode();
    boolean isLeaf();
    Iterable<T> getChildren();

}