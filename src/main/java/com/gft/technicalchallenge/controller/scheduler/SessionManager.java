package com.gft.technicalchallenge.controller.scheduler;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
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
        if (session.getAttribute("Subscriptions") instanceof Subscriptions) {
            ((Subscriptions) session.getAttribute("Subscriptions")).getWSSubscriptions().forEach((key, sub) -> {sub.unsubscribe(); LOGGER.info("Destroy " + key);});
        }
    }

}