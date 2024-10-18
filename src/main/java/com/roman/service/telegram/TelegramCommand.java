package com.roman.service.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramCommand{

    void startCommand(Long chatId);

    void helpCommand(Long chatId);

    void registration(Message message);

    void anotherMessage(Message message);

    SendMessage sendResponse(Long chatId,String responseMessage);
}
