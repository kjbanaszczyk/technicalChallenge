package com.gft.technicalchallenge.filetree;

import com.gft.technicalchallenge.nodeabstraction.Node;

interface AbstractFileNode<T extends Node> extends Node {

    void addFolder(T node);
    boolean removeFolder(T node);

}
