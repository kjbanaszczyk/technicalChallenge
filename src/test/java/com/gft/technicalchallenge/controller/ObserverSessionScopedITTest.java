//package com.gft.technicalchallenge.controller;
//
//import com.gft.technicalchallenge.controller.session.Subscriptions;
//import org.assertj.core.api.Assertions;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TemporaryFolder;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import rx.Subscription;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//public class ObserverSessionScopedITTest {
//
//    @Autowired
//    private
//    ObserverController controller;
//
//    @Autowired
//    private
//    MockHttpSession mockHttpSession;
//
//    @Rule
//    public TemporaryFolder temporaryFolder = new TemporaryFolder();
//
//    @Test
//    public void shouldSubscriptionBeCorrectlyClosedAndRemovedOnStop() throws Exception {
//
//        String websocketEndPoint = controller.startObserving(temporaryFolder.getRoot().getAbsolutePath(), mockHttpSession).getBody();
//        Subscriptions subscriptionsFirst = (Subscriptions) mockHttpSession.getAttribute("Subscriptions");
//        Subscription subscription = subscriptionsFirst.getSub(websocketEndPoint);
//
//        controller.stopObservingOnEndPoint(mockHttpSession, websocketEndPoint);
//
//        Assertions.assertThat(subscriptionsFirst.getSub(websocketEndPoint)).isNull();
//        Assertions.assertThat(subscription.isUnsubscribed()).isTrue();
//    }
//
//    @Test
//    public void sessionScope() throws Exception {
//
//        MockHttpSession sessionFirst = new MockHttpSession();
//        MockHttpSession sessionSecond = new MockHttpSession();
//
//        sessionFirst.changeSessionId();
//        sessionSecond.changeSessionId();
//
//        controller.startObserving(temporaryFolder.getRoot().getAbsolutePath(), sessionFirst);
//        controller.startObserving(temporaryFolder.getRoot().getAbsolutePath(), sessionSecond);
//
//        Subscriptions subscriptionsFirst = (Subscriptions) sessionFirst.getAttribute("scopedTarget.subscriptions");
//        Subscriptions subscriptionsSecond = (Subscriptions) sessionSecond.getAttribute("scopedTarget.subscriptions");
//
//        System.out.println(subscriptionsFirst+"SDCAS");
//        System.out.println(subscriptionsSecond+"FRST");
//
//        Assertions.assertThat(subscriptionsFirst).isNotSameAs(subscriptionsSecond);
//
//    }
//}
