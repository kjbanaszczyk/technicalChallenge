package com.gft.technicalchallenge;

import com.gft.technicalchallenge.nodeabstraction.IterableTree;
import com.gft.technicalchallenge.nodeabstraction.Node;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class NodeIteratorTest {

    private static StubNode getTreeWithEmptyChildren(){
        LinkedList<StubNode> emptyChildren = new LinkedList<>();

        return new StubNode(emptyChildren,"1");
    }
    /** 1 <br>
     * /\ \ <br>
     * 2 3 4 <br>
     *  /\  \\ <br>
     *  5 6 5 6 <br>
     * /\   /\ <br>
     * 5 6  5 6  <br> **/
    private static StubNode getTreeOfTwelveElements(){
        LinkedList<StubNode> emptyChildren = new LinkedList<>();
        LinkedList<StubNode> thirdDepthChildren = new LinkedList<>(Arrays.asList(new StubNode(emptyChildren,"5"), new StubNode(emptyChildren,"6")));
        LinkedList<StubNode> secondDepthChildren = new LinkedList<>(Arrays.asList(new StubNode(thirdDepthChildren,"5"), new StubNode(emptyChildren,"6")));
        LinkedList<StubNode> firstDepthChildren = new LinkedList<>(Arrays.asList(new StubNode(emptyChildren,"2"),
                new StubNode(secondDepthChildren,"3"), new StubNode(secondDepthChildren,"4")));

        return new StubNode(firstDepthChildren,"1");
    }

    @Test
    public void shouldConvertToListConsistingOfAllElementsOfATreeWithoutRoot() throws Exception {
        StubNode node = getTreeOfTwelveElements();

        IterableTree<StubNode> iterableTree = new IterableTree<>(node);
        Iterator<StubNode> iterator = iterableTree.iterator();

        Assertions.assertThat(Lists.newArrayList(iterator).size()).isEqualTo(11);
    }

    @Test
    public void shouldStoreRootOfTree(){
        StubNode node = getTreeOfTwelveElements();

        IterableTree<StubNode> iterableTree = new IterableTree<>(node);

        Assertions.assertThat(iterableTree.getRoot()).isEqualTo(node);
    }

    @Test
    public void shouldNotReturnRootOfTree(){
        StubNode node = getTreeOfTwelveElements();

        IterableTree<StubNode> iterableTree = new IterableTree<>(node);
        Iterator<StubNode> iterator = iterableTree.iterator();

        Assertions.assertThat(Lists.newArrayList(iterator).contains(node)).isFalse();
    }

    @Test
    public void shouldHasNextReturnFalseIfChildrenIsEmpty(){
        StubNode node = getTreeWithEmptyChildren();

        IterableTree<StubNode> iterableTree = new IterableTree<>(node);
        Iterator<StubNode> iterator = iterableTree.iterator();

        Assertions.assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldThrowNoSuchElementExceptionAfterLastElement(){
        StubNode node = getTreeOfTwelveElements();

        IterableTree<StubNode> iterableTree = new IterableTree<>(node);
        Iterator<StubNode> iterator = iterableTree.iterator();

        while(iterator.hasNext())
            iterator.next();

        Assertions.assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
    }

    private static class StubNode implements Node<StubNode> {

        String id;

        LinkedList<StubNode> children;

        StubNode(LinkedList<StubNode> children, String id){
            this.children = children;
            this.id = id;
        }

        @Override
        public LinkedList<StubNode> getChildren() {
            return children;
        }

    }

}