package org.bd.notify.bot;

import org.bd.notify.entity.User;
import org.bd.notify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Component
@PropertySource("classpath:telegram.properties")
public class Bot extends TelegramLongPollingBot {
    private final Logger logger = Logger.getLogger(Bot.class.getName());
    private User user = new User();
    private String chatId;
    private BotState botState;

    @Autowired
    private final UserService userService;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Value("${clockUrl}")
    private String clockUrl;

    public Bot(UserService userService) {
        this.userService = userService;
        this.botState = BotState.getInitialState();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("/start")) {
                this.chatId = update.getMessage().getChatId().toString();
                this.botState = BotState.getInitialState();

                this.user = new User();

                botState.enter(this);
                botState.handleInput(this, botState, update, userService, clockUrl);
            } else {
                if (botState.equals(BotState.Start)) {
                    botState.handleInput(this, botState, update, userService, clockUrl);
                    return;
                }
                if (botState.equals(BotState.FillName)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService, clockUrl);
                    return;
                }
                if (botState.equals(BotState.FillYear)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService, clockUrl);
                    return;
                }
                if (botState.equals(BotState.FillMonth)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService, clockUrl);
                    return;
                }
                if (botState.equals(BotState.FillDay)) {
                    botState.setUser(this, update.getMessage().getText().trim());
                    botState.handleInput(this, botState, update, userService, clockUrl);

                    this.user = new User();

                    return;
                }
                if (botState.equals(BotState.Delete)) {
                    botState.handleInput(this, botState, update, userService, clockUrl);
                    return;
                }
            }
        }
    }

    public User createNewUser() {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);

        try {
            if (user.getName() == null) {

                sm.setText("Введите имя:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getYear() == null) {

                sm.setText("Введите год:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getMonth() == null) {

                sm.setText("Введите месяц:");
                this.botState = botState.nextState();
                execute(sm);

            } else if (user.getDay() == null) {

                sm.setText("Введите число:");
                this.botState = botState.nextState();
                execute(sm);

            }
        } catch (TelegramApiException e) {
            logger.info(e.getMessage() + " : " + e.getCause());
        }

        return user;
    }

    public User fillName(String data) {
        if (!data.equals("")) {
            this.user.setName(data);
        }

        return this.user;
    }

    public User fillYear(String data) {
        if (!data.equals("")) {
            this.user.setYear(Integer.valueOf(data));
        }

        return this.user;
    }

    public User fillMonth(String data) {
        if (!data.equals("")) {
            this.user.setMonth(data);
        }

        return this.user;
    }

    public User fillDay(String data) {
        if (!data.equals("")) {
            this.user.setDay(Long.valueOf(data));
        }

        return this.user;
    }

    public User fillAge(User user) {
        this.user.setAge((long) (Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - user.getYear()));

        return this.user;
    }

    public User sendUser(User user) {
        return userService.createUser(user);
    }

    public void setIdForDelete() {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);

        sm.setText("Введите ID бывшего \"друга\":");
        this.botState = BotState.byId(5);

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage() + " : " + e.getCause());
        }
    }

    public void deleteUser(String id) {
        userService.deleteUser(Long.valueOf(id));
        this.botState = BotState.byId(0);
    }

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        logger.info("CHAT_ID IS: " + this.getChatId());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage() + " : " + e.getCause());
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        sendMessage.setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.info(e.getMessage() + " : " + e.getCause());
        }
    }

    public SendMessage sendKeyBoardMessage(String chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton startButton = new KeyboardButton();
        KeyboardButton findOutButton = new KeyboardButton();
        KeyboardButton showButton = new KeyboardButton();
        KeyboardButton addButton = new KeyboardButton();
        KeyboardButton deleteButton = new KeyboardButton();

        startButton.setText("/start");
        findOutButton.setText("Узнать у кого сегодня ДР");
        showButton.setText("Показать список всех друзей");
        addButton.setText("Добавить новый ДР");
        deleteButton.setText("Удолить бывшего \"друга\"");

        KeyboardRow keyboardButtonsRow1 = new KeyboardRow();
        KeyboardRow keyboardButtonsRow2 = new KeyboardRow();

        keyboardButtonsRow1.add(startButton);
        keyboardButtonsRow1.add(findOutButton);
        keyboardButtonsRow1.add(showButton);
        keyboardButtonsRow2.add(addButton);
        keyboardButtonsRow2.add(deleteButton);

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        keyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Начинается поиск ДР-ов...");
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    public String getChatId() {
        return chatId;
    }

    public Logger getLogger() {
        return logger;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }
}