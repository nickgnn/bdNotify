package org.bd.notify.controller;

import org.bd.notify.service.SendTimeService;
import org.bd.notify.service.impl.SendTimeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * http://localhost:8010/api/seebd/ask
 */
@RestController
@RequestMapping(value = "/api/seebd")
public class ClockController {
    private final Logger logger = Logger.getLogger(SendTimeServiceImpl.class.getName());

    @Autowired
    private SendTimeService service;

    @GetMapping(value = "/ask")
    public void getMessage() {
        service.onRequest();
        logger.info("(OnRequest)Time successfully sent to Matcher at " + Calendar.getInstance(Locale.getDefault()).getTime());
    }
}