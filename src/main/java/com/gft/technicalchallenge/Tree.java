package com.gft.technicalchallenge;

interface Tree<T> {

    void addNode(T node);
    boolean removeNode(T node);
    boolean isNode();
    boolean isLeaf();
}
