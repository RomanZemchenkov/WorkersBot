package com.roman.service.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component(value = "telegramBot")
public class TelegramBot extends TelegramLongPollingBot {

    private static final String BOT_NAME = "Spring_workers_bot";
    private final TelegramCommand telegramCommand;

    public TelegramBot(@Value("${bot.token}") String token,
                       TelegramCommand telegramCommand) {
        super(token);
        this.telegramCommand = telegramCommand;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();
        checkQueryFromUser(message);
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public void checkQueryFromUser(Message message) {
        String messageText = message.getText();
        Long chatId = message.getChatId();
        switch (messageText) {
            case "/start" -> telegramCommand.startCommand(chatId);
            case "/help" -> telegramCommand.helpCommand(chatId);
            case "/registration" -> telegramCommand.registration(message);
            default ->  telegramCommand.anotherMessage(message);
        }
    }
}
