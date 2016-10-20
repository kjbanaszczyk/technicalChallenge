package com.gft.technicalchallenge;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import javax.swing.tree.TreeNode;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FileTreeTest {

    private FileTree tree;

    @Before
    public void setupTree(){
        tree = new FileTree.NodeBuilder("1").withChildren(
                new FileTree.NodeBuilder("2").withChildren(
                        new FileTree.NodeBuilder("3")
                                .withChildren("31").withChildren("32").withChildren("33").build()
                ).build()
        ).withChildren("22").withChildren("23").withChildren(
                new FileTree.NodeBuilder("22").withChildren("23").build()
        ).build();
    }

    @Test
    public void shouldPrintTreeAsExpected(){
        String expected = "\t-folder:1\n" +
                "\t\t-folder:2\n" +
                "\t\t\t-folder:3\n" +
                "\t\t\t\t-file:31\n" +
                "\t\t\t\t-file:32\n" +
                "\t\t\t\t-file:33\n" +
                "\t\t-file:22\n" +
                "\t\t-file:23\n" +
                "\t\t-folder:22\n" +
                "\t\t\t-file:23\n";

        String returned=tree.toString();

        assertThat(returned,is(expected));
    }

    @Test
    public void shouldReturnExpectedSizeOfTree(){
        int expected = 10;

        int returned = tree.size();

        assertThat(returned,is(expected));
    }

    @Test
    public void shouldRemoveGivenElementWhichIsNotReference(){
        FileTree toRemove = new FileTree.NodeBuilder("3")
                .withChildren("31").withChildren("32").withChildren("33").build();
        int expected = 6;


        tree.removeNode(toRemove);
        int returned = tree.size();

        assertThat(returned,is(expected));

    }

    @Test
    public void shouldReturnSameIfElementToRemoveDontExist(){
        FileTree toRemove = new FileTree.NodeBuilder("2")
                .withChildren("31").withChildren("32").withChildren("33").build();

        int expected = 10;
        tree.removeNode(toRemove);
        int actual = tree.size();

        assertThat(actual,is(expected));

    }

    @Test
    public void shouldReturnTrueIfGivenTreeIsNode(){
        FileTree Node = new FileTree.NodeBuilder("2")
                .withChildren("31").withChildren("32").withChildren("33").build();

        boolean actual = Node.isNode();

        assertThat(true, is(actual));

    }


    @Test
    public void shouldReturnTrueIfGivenTreeIsLeaf(){
        FileTree Node = new FileTree.NodeBuilder("2").build();
        
        boolean actual = Node.isLeaf();

        assertThat(true, is(actual));

    }

}