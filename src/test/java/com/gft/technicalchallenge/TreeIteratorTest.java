package com.gft.technicalchallenge;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TreeIteratorTest {

    @Mock
    private StubTree mockedTree = mock(StubTree.class);

    @Mock
    private StubTree mockedChildrenNode = mock(StubTree.class);

    @Mock
    private StubTree mockedChildrensLeaf = mock(StubTree.class);

    @Test
    public void shouldIterateThroughWholeTree() throws Exception {

        when(mockedChildrensLeaf.isNode()).thenReturn(false);
        when(mockedChildrenNode.isNode()).thenReturn(true);
        when(mockedChildrenNode.getChildren()).thenReturn(new LinkedList<>(Arrays.asList(mockedChildrensLeaf, mockedChildrensLeaf)));
        when(mockedTree.getChildren()).thenReturn(new LinkedList<>(Arrays.asList(mockedChildrenNode, mockedChildrensLeaf, mockedChildrensLeaf)));

        TreeIterator iterator = new TreeIterator<>(mockedTree);
        int size=0;
        while(iterator.hasNext()) {
            iterator.next();
            size++;
        }

        assertThat(size,is(6));
    }

    @Test
    public void shouldReturnExpectedFirstElement(){
        when(mockedTree.getChildren()).thenReturn(new LinkedList<>(Arrays.asList(mockedChildrensLeaf, mockedChildrensLeaf, mockedChildrensLeaf)));
        int expectedChildrenSize = 3;

        final int[] returnedChildrenSize = {0};
        TreeIterator<StubTree> iterator = new TreeIterator<>(mockedTree);

        iterator.next().getChildren().forEach(elem -> returnedChildrenSize[0] +=1);

        assertThat(returnedChildrenSize[0], is(expectedChildrenSize));
    }

    private class StubTree implements Tree<StubTree>{

        @Override
        public boolean isNode() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public Iterable<StubTree> getChildren() {
            return null;
        }

        @Override
        public Iterator<StubTree> iterator() {
            return null;
        }
    }

}