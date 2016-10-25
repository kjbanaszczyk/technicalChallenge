package com.gft.technicalchallenge;

import com.sun.istack.internal.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

class IterableTree<T extends Node<T>> implements Iterable<T> {
    @NotNull
    private T root;

    IterableTree(T root){
        this.root = root;
    }

    T getRoot(){
        return root;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<>(root);
    }

    private class TreeIterator<F extends Node<F>> implements Iterator<F> {

        private Stack<Iterator<F>> currentIterators;

        TreeIterator(F node){
            currentIterators = new Stack<>();
            currentIterators.push(node.getChildren().iterator());
        }

        @Override
        public boolean hasNext() {
            return currentIterators.peek().hasNext();
        }

        @Override
        public F next() {

            if(!hasNext())
                throw new NoSuchElementException();

            F Node = currentIterators.peek().next();
            if (Node.getChildren().iterator().hasNext()) {
                currentIterators.push(Node.getChildren().iterator());
            }
            if (!currentIterators.isEmpty() && !currentIterators.peek().hasNext()) {
                currentIterators.pop();
            }

            return Node;
        }
    }
}
