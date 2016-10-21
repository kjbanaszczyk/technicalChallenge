package com.gft.technicalchallenge;

import java.util.Iterator;
import java.util.Stack;

public class TreeIterator<T extends Tree<T>> implements Iterator<T> {

    private Stack<Iterator<T>> currentIterators;
    private Iterator<T> currentIterator;

    public TreeIterator(T node){
        currentIterators = new Stack<>();
        currentIterators.push(node.getChildren().iterator());
        currentIterator = currentIterators.peek();
    }

    @Override
    public boolean hasNext() {
        for(Iterator<? extends Tree> it : currentIterators)
            if(it.hasNext()) return true;
        return false;
    }

    @Override
    public T next() {
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
