package com.gft.technicalchallenge;

import java.util.Iterator;
import java.util.Stack;

class IterableTree<T extends Node<T>> implements Iterable<T> {

    private T node;

    IterableTree(T node){
        this.node=node;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<>(node);
    }

    private class TreeIterator<F extends Node<F>> implements Iterator<F> {

        private Stack<Iterator<F>> currentIterators;
        private Iterator<F> currentIterator;
        private F node;

        TreeIterator(F node){
            currentIterators = new Stack<>();
            currentIterators.push(node.getChildren().iterator());
            this.node=node;
        }

        @Override
        public boolean hasNext() {
            for (Iterator<F> it : currentIterators)
                if (it.hasNext()) return true;
            return false;
        }

        @Override
        public F next() {
            if (currentIterator == null) {
                currentIterator = currentIterators.peek();
                return node;
            }
            if (currentIterator.hasNext()) {
                F peekNext = currentIterator.next();
                if (peekNext.isNode()) {
                    currentIterators.push(peekNext.getChildren().iterator());
                    currentIterator = currentIterators.peek();
                }
                return peekNext;
            } else {
                currentIterators.pop();
                if (!currentIterators.isEmpty())
                    currentIterator = currentIterators.peek();
                return next();
            }
        }
    }
}
