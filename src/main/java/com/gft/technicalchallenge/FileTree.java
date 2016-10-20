package com.gft.technicalchallenge;

import java.util.LinkedList;


class FileTree implements Tree<FileTree> {

    private String name;

    private FileTree(String name){
        this.name = name;
        children = new LinkedList<>();
    }

    private LinkedList<FileTree> children;

    private LinkedList<FileTree> getChildren() {
        return children;
    }

    public void addNode(FileTree node){
        this.children.add(node);
    }

    @Override
    public void removeNode(FileTree node) {

    }

    @Override
    public boolean isNode() {
        return !getChildren().isEmpty();
    }

    @Override
    public boolean isLeaf() {
        return !isNode();
    }

    @Override
    public String toString(){
        return print(0);
    }

    private String print(int depth){
        String tabs = "";
        for(int i=0; i<=depth; i++){
            tabs+="\t";
        }
        String returned=tabs+name+"\n";
        for(FileTree node : children)
            returned+=node.print(depth+1);
        return returned;
    }

    static class NodeBuilder{

        FileTree rootNode;

        NodeBuilder(String name){
            rootNode = new FileTree(name);
        }

        NodeBuilder withChildren(FileTree node){
            rootNode.addNode(node);
            return this;
        }

        NodeBuilder withChildren(String name){
            rootNode.addNode(new FileTree(name));
            return this;
        }

        FileTree build(){
            return rootNode;
        }

    }


}
