package com.gft.technicalchallenge;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FileTreeTest {

    FileTree tree;

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
        String expected = "\t1\n" +
                "\t\t2\n" +
                "\t\t\t3\n" +
                "\t\t\t\t31\n" +
                "\t\t\t\t32\n" +
                "\t\t\t\t33\n" +
                "\t\t22\n" +
                "\t\t23\n" +
                "\t\t22\n" +
                "\t\t\t23\n";
        String returned=tree.toString();

        assertThat(returned,is(expected));
    }

}