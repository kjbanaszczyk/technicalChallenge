package com.gft.technicalchallenge.controller.session;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import static org.junit.Assert.*;

public class SessionManagerTest {

    @Test
    public void shouldSetUpSessionIntervalOnSessionCreated() throws Exception {
        HttpSession session = new MockHttpSession();

        HttpSessionListener sessionListener = new SessionManager();
        sessionListener.sessionCreated(new HttpSessionEvent(session));

        Assertions.assertThat(session.getMaxInactiveInterval()).isEqualTo(300);
    }

}