package com.gft.technicalchallenge.filetree;

import java.util.LinkedList;


public final class FileNode implements AbstractFileNode<FileNode> {

    private String getName() {
        return name;
    }

    private String name;

    private FileNode(String name){
        this.name = name;
        children = new LinkedList<>();
    }

    private LinkedList<FileNode> children;

    public LinkedList<FileNode> getChildren() {
        return children;
    }

    public void addFolder(FileNode node){
        this.children.add(node);
    }

    public int size(){
        int size = 1;
        for(FileNode child : getChildren()){
            size+=child.size();
        }
        return size;
    }

    @Override
    public boolean removeFolder(FileNode node) {
        if(children.remove(node)) return true;
        else{
            for(FileNode child : children){
                if(child.removeFolder(node)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNode() {
        return !getChildren().isEmpty();
    }

    @Override
    public String toString(){
        return print(0);
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!this.getClass().equals(o.getClass())) return false;
        final FileNode other = (FileNode) o;
        return this.name.equals(other.getName()) && this.getChildren().equals(other.getChildren());
    }

    private String print(int depth){
        String tabs = "";
        for(int i=0; i<=depth; i++){
            tabs+="\t";
        }
        String returned=tabs + (isNode() ? "-folder:" : "-file:") + name+"\n";
        for(FileNode node : children)
            returned+=node.print(depth+1);
        return returned;
    }

    public static class NodeBuilder{

        FileNode rootNode;

        public NodeBuilder(String name){
            rootNode = new FileNode(name);
        }

        public NodeBuilder withChildren(FileNode node){
            rootNode.addFolder(node);
            return this;
        }

        public NodeBuilder withChildren(String name){
            rootNode.addFolder(new FileNode(name));
            return this;
        }

        public FileNode build(){
            return rootNode;
        }

    }


}
