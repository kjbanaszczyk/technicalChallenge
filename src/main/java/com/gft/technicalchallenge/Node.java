package com.gft.technicalchallenge;

import com.sun.istack.internal.NotNull;

public interface Node<T extends Node> {

    @NotNull
    Iterable<T> getChildren();

}