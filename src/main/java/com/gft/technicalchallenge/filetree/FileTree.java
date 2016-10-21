package com.gft.technicalchallenge.filetree;

import com.gft.technicalchallenge.TreeIterator;

import java.util.Iterator;
import java.util.LinkedList;


public final class FileTree implements AbstractFileTree<FileTree> {

    private String getName() {
        return name;
    }

    private String name;

    private FileTree(String name){
        this.name = name;
        children = new LinkedList<>();
    }

    private LinkedList<FileTree> children;

    public LinkedList<FileTree> getChildren() {
        return children;
    }

    public void addNode(FileTree node){
        this.children.add(node);
    }

    public int size(){
        int size = 1;
        for(FileTree child : getChildren()){
            size+=child.size();
        }
        return size;
    }

    @Override
    public boolean removeNode(FileTree node) {
        if(children.remove(node)) return true;
        else{
            for(FileTree child : children){
                if(child.removeNode(node)) return true;
            }
        }
        return false;
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

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!this.getClass().equals(o.getClass())) return false;
        final FileTree other = (FileTree) o;
        return this.name.equals(other.getName()) && this.getChildren().equals(other.getChildren());
    }

    private String print(int depth){
        String tabs = "";
        for(int i=0; i<=depth; i++){
            tabs+="\t";
        }
        String returned=tabs + (isNode() ? "-folder:" : "-file:") + name+"\n";
        for(FileTree node : children)
            returned+=node.print(depth+1);
        return returned;
    }

    @Override
    public Iterator<FileTree> iterator() {
        return new TreeIterator<>(this);
    }

    public static class NodeBuilder{

        FileTree rootNode;

        public NodeBuilder(String name){
            rootNode = new FileTree(name);
        }

        public NodeBuilder withChildren(FileTree node){
            rootNode.addNode(node);
            return this;
        }

        public NodeBuilder withChildren(String name){
            rootNode.addNode(new FileTree(name));
            return this;
        }

        public FileTree build(){
            return rootNode;
        }

    }


}
