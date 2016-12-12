package com.gft.technicalchallenge.filetree;

import com.gft.technicalchallenge.nodeabstraction.Node;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.LinkedList;

public class FileTree implements Node<FileTree> {

    private Path path;

    public Path getPath() {
        return path;
    }

    public FileTree(Path path){
        this.path = path;
    }

    @Override
    public String toString(){
        return path.toAbsolutePath().toString();
    }

    @Override
    public Iterable<FileTree> getChildren() {
        File[] files = path.toFile().listFiles();
        LinkedList<FileTree> fileTrees = new LinkedList<>();
        if(files != null)
        for(File children : files) {
                fileTrees.add(new FileTree(children.toPath()));
            }

        return fileTrees;
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
