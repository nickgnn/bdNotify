package org.bd.notify.controller;

import org.bd.notify.dto.TimeDto;
import org.bd.notify.exception.DBException;
import org.bd.notify.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/getTime")
public class MatcherController {
    private final Logger logger = Logger.getLogger(MatcherController.class.getName());

    @Autowired
    private TimeService timeService;

    @PostMapping(value = "/currentTime")
    public void getMessage(@RequestBody TimeDto timeDto) throws DBException {
        logger.info("Time successfully received from Clock at " + timeDto.getTime());

        timeService.compareTime(timeDto);
    }

    @PostMapping(value = "/onRequest")
    public void getMessageOnRequest(@RequestBody TimeDto timeDto) throws DBException {
        logger.info("Time successfully received from Clock at " + timeDto.getTime());

        timeService.compareOnRequest(timeDto);
    }
}