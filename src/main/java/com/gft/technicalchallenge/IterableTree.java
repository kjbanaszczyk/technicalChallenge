package com.gft.technicalchallenge;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

class IterableTree<T extends Node<T>> implements Iterable<T> {

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

        // contains only non-empty iterators.
        private Stack<Iterator<F>> knownIterators;

        TreeIterator(F node){
            knownIterators = new Stack<>();

            if (!node.getChildren().iterator().hasNext()) return;

            knownIterators.push(node.getChildren().iterator());
        }

        @Override
        public boolean hasNext() {
            return !knownIterators.isEmpty();
        }

        @Override
        public F next() {

            if (knownIterators.isEmpty())
                throw new NoSuchElementException();

            Iterator<F> current = knownIterators.peek();
            F node = current.next();
            if (!current.hasNext()) knownIterators.pop();

            if (node.getChildren().iterator().hasNext()) {
                knownIterators.push(node.getChildren().iterator());
            }

            return node;
        }
    }
}
