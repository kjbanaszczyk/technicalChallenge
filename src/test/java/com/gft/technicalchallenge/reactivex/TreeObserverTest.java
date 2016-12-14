package com.gft.technicalchallenge.reactivex;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@SpringBootTest
public class TreeObserverTest {

    @Autowired
    private
    SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void shouldThrowUnsupportedOperationExceptionOnCompleted(){
        TreeObserver treeObserver = new TreeObserver("1", simpMessagingTemplate);

        try{
            treeObserver.onCompleted();
        }
        catch (Exception e){
            Assertions.assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

}
