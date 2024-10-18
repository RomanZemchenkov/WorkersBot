package com.roman.service.telegram;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramMessageSender {

    private final TelegramBot telegramBot;
    private static final Logger logger = LoggerFactory.getLogger(TelegramMessageSender.class);

    public void sendResponse(SendMessage sendMessage){
        try {
            logger.info("Message {} send",sendMessage.getText());
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
