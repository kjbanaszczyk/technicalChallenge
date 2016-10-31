package com.gft.technicalchallenge.filetree;

import com.gft.technicalchallenge.nodeabstraction.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileTree implements Node<FileTree> {

    private File file;
    private Path path;
    private WatchKey key;
    private ConcurrentLinkedQueue<FileTree> children;

    public FileTree(String path){
        this.path = Paths.get(path);
        this.file = this.path.toFile();
        children = new ConcurrentLinkedQueue<>();
    }

    private FileTree(File file) {
        this.file=file;
        this.path=file.toPath();
        children = new ConcurrentLinkedQueue<>();
    }

    public boolean isRegistered(){
        return this.key!=null;
    }

    public FileTree registerFileTree(WatchService service) throws IOException {
            if(file.isDirectory() && key==null) {
                try{key = path.register(service, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);}
                catch(AccessDeniedException e){
                    System.out.println("Acces denied to: " + e.getMessage().split(":")[1]);
                }
            }
            return this;
    }

    public List<WatchEvent<?>> obtainEvents(){

        if(key==null)
            return new ArrayList<>();

        List<WatchEvent<?>> list = key.pollEvents();
        key.reset();

        return list;
    }

    public void removeChildrenNode(FileTree node){
        this.children.remove(node);
    }

    public void addChildrenNode(FileTree node){
        this.children.add(node);
    }

    @Override
    public String toString(){
        return file.toString();
    }

    @Override
    public Iterable<FileTree> getChildren() {
        File[] files = file.listFiles();
        if(file.isDirectory() && files!=null) {
            this.children.clear();
            for(File children : files) {
                this.children.add(new FileTree(children));
            }
        }
        return this.children;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null)
            return false;
        if(!(o instanceof FileTree))
            return false;
        FileTree other = (FileTree) o;
        return this.path.toString().equals(other.path.toString());
    }

}
