//package com.gft.technicalchallenge.controller.session;
//
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.mockito.Mockito;
//import rx.Subscription;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//public class SubscriptionsTest {
//
//    private Subscription subscription1 = Mockito.mock(Subscription.class);
//    private Subscription subscription2 = Mockito.mock(Subscription.class);
//
//    @Test
//    public void shouldCorrectlyManageSubscriptions() throws Exception {
//        Subscriptions subscriptions = new Subscriptions();
//
//        subscriptions.addSub("1", subscription1);
//        subscriptions.addSub("2", subscription2);
//
//        Assertions.assertThat(subscriptions.getSub("1")).isEqualTo(subscription1);
//        Assertions.assertThat(subscriptions.getSub("2")).isEqualTo(subscription2);
//    }
//
//    @Test
//    public void shouldUnsubscribeAllSubscriptions() throws Exception {
//
//        Subscriptions subscriptions = new Subscriptions();
//
//        subscriptions.addSub("1", subscription1);
//        subscriptions.addSub("2", subscription2);
//
//        subscriptions.unsubscribe("1");
//        verify(subscription1, times(1)).unsubscribe();
//        verify(subscription2, times(0)).unsubscribe();
//
//    }
//
//    @Test
//    public void shouldCloseAllSubscriptions() throws Exception {
//        Subscriptions subscriptions = new Subscriptions();
//
//        subscriptions.addSub("1", subscription1);
//        subscriptions.addSub("2", subscription2);
//
//        subscriptions.close();
//        verify(subscription1, times(1)).unsubscribe();
//        verify(subscription2, times(1)).unsubscribe();
//    }
//
//}