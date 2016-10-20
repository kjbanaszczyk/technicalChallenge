package com.gft.technicalchallenge;

/**
 * Created by klbk on 20/10/2016.
 */
public class Main {

    public static void main(String... args){

        FileTree tree = new FileTree.NodeBuilder("1").withChildren(
                                        new FileTree.NodeBuilder("2").withChildren(
                                                new FileTree.NodeBuilder("3")
                                                                .withChildren("31").withChildren("32").withChildren("33").build()
                                        ).build()
        ).withChildren("22").withChildren("23").withChildren(
                                        new FileTree.NodeBuilder("22").withChildren("23").build()
        ).build();

    }

}
