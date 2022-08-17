package org.bd.notify.bot;

import org.bd.notify.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public enum BotState {
    Start() {
        @Override
        public void enter(Bot bot) {
            bot.sendMessage("Привет");
        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            SendMessage sm = new SendMessage();
            bot.sendMessage(bot.sendKeyBoardMessage(bot.getChatId()));
            String command = update.getMessage().getText();
            bot.getLogger().info(command);

            String[] seq = command.split(" ");

            switch (seq[0]) {
                case "Узнать":
                    userService.getBirthday(clockUrl);
                    break;
                case "Показать":
                    sm.setText(userService.getAllUsers());
                    bot.sendMessage(sm);
                    break;
                case "Добавить":
                    bot.createNewUser();
                    break;
                case "Удолить":
                    bot.setIdForDelete();
                    break;
                default:
                    System.out.println("Oooops, something wrong !");
            }
        }

        @Override
        public void setUser(Bot bot, String data) {

        }

        @Override
        public BotState nextState() {
            return FillName;
        }
    },
    FillName() {
        @Override
        public void enter(Bot bot) {
            bot.sendMessage("Create");
        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillName(data);
        }

        @Override
        public BotState nextState() {
            return FillYear;
        }
    },
    FillYear() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillYear(data);
        }

        @Override
        public BotState nextState() {
            return FillMonth;
        }
    },
    FillMonth() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            bot.createNewUser();
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillMonth(data);
        }

        @Override
        public BotState nextState() {
            return FillDay;
        }
    },
    FillDay() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            bot.sendUser(bot.fillAge(bot.createNewUser()));
            bot.setBotState(bot.getBotState().nextState());
            bot.sendMessage("Success! Новый дружок успешно добавлен в базу :)");
        }

        @Override
        public void setUser(Bot bot, String data) {
            bot.fillDay(data);
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    },
    Delete() {
        @Override
        public void enter(Bot bot) {

        }

        @Override
        public void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl) {
            bot.deleteUser(update.getMessage().getText());
            bot.sendMessage("Success! Пользователь успешно удалён.");
        }

        @Override
        public void setUser(Bot bot, String data) {

        }

        @Override
        public BotState nextState() {
            return null;
        }
    };

    private static BotState[] states;

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    public abstract void enter(Bot bot);
    public abstract void handleInput(Bot bot, BotState botState, Update update, UserService userService, String clockUrl);
    public abstract void setUser(Bot bot, String data);
    public abstract BotState nextState();
}