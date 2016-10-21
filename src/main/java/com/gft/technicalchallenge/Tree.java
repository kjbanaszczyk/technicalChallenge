package com.gft.technicalchallenge;

/**
 * Created by klbk on 21/10/2016.
 */
public interface Tree<T> extends Iterable<T> {

    boolean isNode();
    boolean isLeaf();
    Iterable<T> getChildren();

}