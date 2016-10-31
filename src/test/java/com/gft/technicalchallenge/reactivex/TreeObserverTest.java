package com.gft.technicalchallenge.reactivex;

import com.gft.technicalchallenge.filetree.FileTree;
import org.junit.Test;

import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static org.mockito.Mockito.*;

public class TreeObserverTest {

    private FileTree tree = mock(FileTree.class);
    private WatchEvent<?> eventRemove = mock(WatchEvent.class);
    private WatchEvent<?> eventAdd = mock(WatchEvent.class);
    private Kind kind = mock(Kind.class);

    @Test
    public void shouldRemoveNodeFromTreeWhenRemoveIsCalled(){
        when(tree.obtainEvents()).thenReturn(new ArrayList<>(Collections.singletonList(eventRemove)));
        when(tree.toString()).thenReturn("nothing");
        when(eventRemove.kind()).thenReturn(kind);
        when(kind.name()).thenReturn("ENTRY_DELETE");

        TreeObserver observer = new TreeObserver();

        observer.onNext(tree);

        verify(tree, times(1)).removeChildrenNode(any());
    }

    @Test
    public void shouldAddNodeToTreeWhenCreateIsCalled(){
        when(tree.obtainEvents()).thenReturn(new ArrayList<>(Collections.singletonList(eventAdd)));
        when(tree.toString()).thenReturn("nothing");
        when(eventAdd.kind()).thenReturn(kind);
        when(kind.name()).thenReturn("ENTRY_CREATE");

        TreeObserver observer = new TreeObserver();

        observer.onNext(tree);

        verify(tree, times(1)).addChildrenNode(any());
    }

}