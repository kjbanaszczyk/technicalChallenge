package com.gft.technicalchallenge.controller.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.*;
import java.util.logging.Logger;

@Component
public class SessionManager implements HttpSessionListener {

    private static Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    private static final int SESSION_TIMEOUT_IN_SECONDS = 300;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.info("Starting session: " + se.getSession().getId());
        se.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        LOGGER.info("Ending session: " + se.getSession().getId());
    }

}