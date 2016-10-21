package com.gft.technicalchallenge;

import java.util.Iterator;
import java.util.Stack;

public class TreeIterator<T extends Tree<T>> implements Iterator<T> {

    private Stack<Iterator<T>> currentIterators;
    private Iterator<T> currentIterator;
    private T node;
    public TreeIterator(T node){
        currentIterators = new Stack<>();
        currentIterators.push(node.getChildren().iterator());
        this.node=node;
    }

    @Override
    public boolean hasNext() {
        for(Iterator<? extends Tree> it : currentIterators)
            if(it.hasNext()) return true;
        return false;
    }

    @Override
    public T next() {
        if(currentIterator==null){
            currentIterator = currentIterators.peek();
            return node;
        }
        if(currentIterator.hasNext()){
            T peekNext = currentIterator.next();
            if(peekNext.isNode()) {
                currentIterators.push(peekNext.getChildren().iterator());
                currentIterator = currentIterators.peek();
            }
                return peekNext;
        }
        else{
            currentIterators.pop();
            if(!currentIterators.isEmpty())
                currentIterator=currentIterators.peek();
            return next();
        }
    }
}
