package org.bd.notify.controllers;

import org.bd.notify.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * http://localhost:8030/api/telegramChat/sendMessage
 */
@RestController
@RequestMapping(value = "/api/telegramChat")
public class BotController {
    private final Logger logger = Logger.getLogger(BotController.class.getName());
    private Bot bot;

    @Autowired
    public BotController(Bot bot) {
        this.bot = bot;
    }

    @PostMapping(value = "/sendMessage")
    public void sendMessage(@RequestBody String message) {
        logger.info(message);

        if (!message.equals("Time hasn't come")) {
            bot.sendMessage(message);
        }
    }
}