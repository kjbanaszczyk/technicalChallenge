package com.gft.technicalchallenge.old;

import com.gft.technicalchallenge.Tree;

interface Tree1<T> extends Tree<T> {

    void addNode(T node);
    boolean removeNode(T node);

}
