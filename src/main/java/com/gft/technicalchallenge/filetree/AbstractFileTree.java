package com.gft.technicalchallenge.filetree;

import com.gft.technicalchallenge.Tree;

interface AbstractFileTree<T extends Tree> extends Tree<T> {

    void addNode(T node);
    boolean removeNode(T node);

}
