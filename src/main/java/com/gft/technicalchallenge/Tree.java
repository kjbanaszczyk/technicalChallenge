package com.gft.technicalchallenge;

interface Tree<T> {

    void addNode(T node);
    void removeNode(T node);
    boolean isNode();
    boolean isLeaf();
}
