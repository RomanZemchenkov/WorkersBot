package com.roman.service.telegram.options;

import com.roman.service.stage.OptionsState;
import com.roman.service.telegram.DockerInitializer;
import com.roman.service.telegram.TelegramBot;
import com.roman.service.telegram.TelegramMessageSender;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class OptionServiceIT extends DockerInitializer {

    private final OptionsService optionsService;

    @Mock
    private AbsSender absSender;
    @MockBean
    private TelegramMessageSender messageSender;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    private static final String CHAT_ID = "11";

    @Autowired
    public OptionServiceIT(OptionsService optionsService) {
        this.optionsService = optionsService;
    }


    @Test
    @DisplayName("Testing /workers command")
    void workersCommandTest(){
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message message = messageFactory("/workers");
        optionsService.checkOptionsState(message, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());

        SendMessage currentMessage = sendMessageArgumentCaptor.getValue();

        assertNotNull(currentMessage);
        assertEquals(currentMessage.getChatId(),CHAT_ID);
    }

    @Test
    @DisplayName("Testing /worker n command")
    void showOneWorkerCommandTest(){
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message message = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(message,OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());

        SendMessage currentMessage = sendMessageArgumentCaptor.getValue();

        assertNotNull(currentMessage);
        assertEquals(currentMessage.getChatId(),CHAT_ID);
    }

    @Test
    @DisplayName("Testing /goToMe n without time")
    void callingWorkerToDirectorWithoutTimeTest(){
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message watchMessage = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(watchMessage,OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step third
        Mockito.reset(messageSender);

        Message goToMeMessage = messageFactory("/goToMe Petya_1732");
        optionsService.checkOptionsState(goToMeMessage,OptionsState.OBSERVED_WORKER);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(sendMessageArgumentCaptor.capture());

        List<SendMessage> allMessages = sendMessageArgumentCaptor.getAllValues();

        allMessages.forEach(mes -> System.out.println(mes.getText()));

    }

    @Test
    @DisplayName("Testing /goToMe n with time")
    void callingWorkerToDirectorWithTimeTest(){
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message watchMessage = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(watchMessage,OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step third
        Mockito.reset(messageSender);

        Message goToMeMessage = messageFactory("/goToMe Petya_1732 2024-10-24 13:30");
        optionsService.checkOptionsState(goToMeMessage,OptionsState.OBSERVED_WORKER);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(sendMessageArgumentCaptor.capture());

        List<SendMessage> allMessages = sendMessageArgumentCaptor.getAllValues();

        allMessages.forEach(mes -> System.out.println(mes.getText()));

    }

    static Message messageFactory(String requestText) {
        User roman = new User(1L, "Roman", false);
        roman.setLastName("Zemchenkov");
        roman.setUserName("Roman_Zemchenkov");
        Chat chat = new Chat(Long.parseLong(CHAT_ID), "Chat");
        Message message = new Message();
        message.setFrom(roman);
        message.setText(requestText);
        message.setChat(chat);
        return message;
    }
}
