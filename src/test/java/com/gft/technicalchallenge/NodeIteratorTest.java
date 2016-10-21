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

public class NodeIteratorTest {

    @Mock
    private StubNode mockedTree = mock(StubNode.class);

    @Mock
    private StubNode mockedChildrenNode = mock(StubNode.class);

    @Mock
    private StubNode mockedChildrensLeaf = mock(StubNode.class);


    @Test
    public void shouldIterateThroughWholeTree() throws Exception {

        when(mockedChildrensLeaf.isNode()).thenReturn(false);
        when(mockedChildrenNode.isNode()).thenReturn(true);
        when(mockedChildrenNode.getChildren()).thenReturn(new LinkedList<>(Arrays.asList(mockedChildrensLeaf, mockedChildrensLeaf)));
        when(mockedTree.getChildren()).thenReturn(new LinkedList<>(Arrays.asList(mockedChildrenNode, mockedChildrensLeaf, mockedChildrensLeaf)));


        IterableTree<StubNode> iterableTree = new IterableTree<>(mockedTree);
        Iterator<StubNode> iterator = iterableTree.iterator();
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
        IterableTree<StubNode> iterableTree = new IterableTree<>(mockedTree);
        Iterator<StubNode> iterator = iterableTree.iterator();

        iterator.next().getChildren().forEach(elem -> returnedChildrenSize[0] +=1);

        assertThat(returnedChildrenSize[0], is(expectedChildrenSize));
    }

    private class StubNode implements Node<StubNode> {

        @Override
        public boolean isNode() {
            return false;
        }

        @Override
        public LinkedList<StubNode> getChildren() {
            return null;
        }

    }

}