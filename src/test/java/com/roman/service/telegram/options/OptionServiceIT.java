package com.roman.service.telegram.options;

import com.roman.dao.redis.RedisRepository;
import com.roman.service.MeetingService;
import com.roman.service.stage.OptionsState;
import com.roman.service.telegram.DockerInitializer;
import com.roman.service.telegram.TelegramMessageSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql", "classpath:sql/load.sql"})
public class OptionServiceIT extends DockerInitializer {

    private final OptionsService optionsService;

    @Mock
    private AbsSender absSender;
    @MockBean
    private TelegramMessageSender messageSender;
    @SpyBean
    private OptionsActions optionsActions;
    @MockBean
    private RedisRepository redisRepository;
    @MockBean
    private MeetingService meetingService;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    private static final String CHAT_ID = "11";

    @Autowired
    public OptionServiceIT(OptionsService optionsService) {
        this.optionsService = optionsService;
    }


    @Test
    @DisplayName("Testing /workers command")
    void workersCommandTest() {
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message message = messageFactory("/workers");
        optionsService.checkOptionsState(message, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());

        SendMessage currentMessage = sendMessageArgumentCaptor.getValue();

        assertNotNull(currentMessage);
        assertEquals(currentMessage.getChatId(), CHAT_ID);
    }

    @Test
    @DisplayName("Testing /worker n command")
    void showOneWorkerCommandTest() {
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message message = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(message, OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());

        SendMessage currentMessage = sendMessageArgumentCaptor.getValue();

        assertNotNull(currentMessage);
        assertEquals(currentMessage.getChatId(), CHAT_ID);
    }

    @Test
    @DisplayName("Testing /goToMe n without time")
    void callingWorkerToDirectorWithoutTimeTest() {
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message watchMessage = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(watchMessage, OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step third
        Mockito.reset(messageSender);

        Message goToMeMessage = messageFactory("/goToMe Petya_1732");
        optionsService.checkOptionsState(goToMeMessage, OptionsState.OBSERVED_WORKER);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(sendMessageArgumentCaptor.capture());

        List<SendMessage> allMessages = sendMessageArgumentCaptor.getAllValues();

        allMessages.forEach(mes -> System.out.println(mes.getText()));

    }

    @Test
    @DisplayName("Testing /goToMe n with time")
    void callingWorkerToDirectorWithTimeTest() {
        //Step first
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message workersCommand = messageFactory("/workers");
        optionsService.checkOptionsState(workersCommand, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step second
        Mockito.reset(messageSender);
        Message watchMessage = messageFactory("/worker Petya_1732");
        optionsService.checkOptionsState(watchMessage, OptionsState.OBSERVED_WORKERS);

        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));

        //Step third
        Mockito.reset(messageSender);

        Message goToMeMessage = messageFactory("/goToMe Petya_1732 2024-10-24 13:30");
        optionsService.checkOptionsState(goToMeMessage, OptionsState.OBSERVED_WORKER);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(sendMessageArgumentCaptor.capture());

        List<SendMessage> allMessages = sendMessageArgumentCaptor.getAllValues();

        allMessages.forEach(mes -> System.out.println(mes.getText()));

    }

    /*
    Meeting method test block
     */

    @Test
    @DisplayName("Testing the /meetings command")
    void wantToWatchMeetingMenuMethodTest() {
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message wantToMeetingsMessage = messageFactory("/meetings");

        optionsService.checkOptionsState(wantToMeetingsMessage, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.never()).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingMeetingMenu();


    }

    @Test
    @DisplayName("Testing the /addMeeting command")
    void wantToAddNewMeetingMethodTest(){

        //step one
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message wantToMeetingsMessage = messageFactory("/meetings");

        optionsService.checkOptionsState(wantToMeetingsMessage, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.never()).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingMeetingMenu();

        //step two
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message addMeetingMessage = messageFactory("/addMeeting");
        optionsService.checkOptionsState(addMeetingMessage, OptionsState.CHOOSE_MEETING_OPERATION);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(sendMessageArgumentCaptor.capture());
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        SendMessage responseMessage = sendMessageArgumentCaptor.getValue();

        assertNotNull(responseMessage);
        String text = responseMessage.getText();
        assertNotNull(text);
    }

    @Test
    @DisplayName("Testing the /addMeeting command with add few participants")
    void wantToAddFewParticipantsToMeetingCommandTest(){

        //step one
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message wantToMeetingsMessage = messageFactory("/meetings");

        optionsService.checkOptionsState(wantToMeetingsMessage, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.never()).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingMeetingMenu();

        //step two
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message addMeetingMessage = messageFactory("/addMeeting");
        optionsService.checkOptionsState(addMeetingMessage, OptionsState.CHOOSE_MEETING_OPERATION);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step three
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        String participants = "1, 11, 21, 32";
        Message partisipantsMessage = messageFactory(participants);

        optionsService.checkOptionsState(partisipantsMessage,OptionsState.CREATE_MEETING);
        ArgumentCaptor<String> participantsCapture = ArgumentCaptor.forClass(String.class);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());
        Mockito.verify(redisRepository, Mockito.times(1)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),participantsCapture.capture());
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        SendMessage responseMessage = sendMessageArgumentCaptor.getValue();
        String participantsInRedis = participantsCapture.getValue();
        assertNotNull(participantsInRedis);
        assertEquals(participantsInRedis,"1,11,21,32");

        assertNotNull(responseMessage);
        String text = responseMessage.getText();
        assertEquals(text,"Введите, пожалуйста, время встречи в одном из двух форматов:\n" +
                          "1. yyyy-MM-dd HH:mm Пример: 2024-10-10 13:30 - встреча будет назначена на определённую дату\n" +
                          "2. HH:mm Пример: 13:30 - встреча будет назначена на сегодня\n");
    }

    @Test
    @DisplayName("Testing the /addMeeting command with add time")
    void wantToAddTimeToMeetingCommandTest(){

        //step one
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message wantToMeetingsMessage = messageFactory("/meetings");

        optionsService.checkOptionsState(wantToMeetingsMessage, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.never()).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingMeetingMenu();

        //step two
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message addMeetingMessage = messageFactory("/addMeeting");
        optionsService.checkOptionsState(addMeetingMessage, OptionsState.CHOOSE_MEETING_OPERATION);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step three
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        Message partisipantsMessage = messageFactory("1, 11, 21, 32");

        optionsService.checkOptionsState(partisipantsMessage,OptionsState.CREATE_MEETING);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(redisRepository, Mockito.times(1)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step four
        Mockito.reset(messageSender, redisRepository);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        String meetingTime = "13:30";
        Message timeMessage = messageFactory(meetingTime);

        ArgumentCaptor<String> timeCapture = ArgumentCaptor.forClass(String.class);

        optionsService.checkOptionsState(timeMessage,OptionsState.ADD_MEETING_PARTICIPANTS);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());
        Mockito.verify(redisRepository, Mockito.times(2)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),timeCapture.capture());
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        SendMessage responseMessage = sendMessageArgumentCaptor.getValue();
        List<String> timeInRedis = timeCapture.getAllValues();
        assertEquals(timeInRedis.size(),2);
        assertEquals(timeInRedis.get(0), LocalDate.now().toString());
        assertEquals(timeInRedis.get(1), meetingTime);

        assertNotNull(responseMessage);
        String text = responseMessage.getText();
        assertEquals(text,"Введите, пожалуйста, название или тему встречи.");
    }

    @Test
    @DisplayName("Testing the /addMeeting command with add title")
    void wantToAddTitleToMeetingCommandTest(){

        //step one
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message wantToMeetingsMessage = messageFactory("/meetings");

        optionsService.checkOptionsState(wantToMeetingsMessage, OptionsState.CHOOSE);

        Mockito.verify(messageSender, Mockito.never()).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingMeetingMenu();

        //step two
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message addMeetingMessage = messageFactory("/addMeeting");
        optionsService.checkOptionsState(addMeetingMessage, OptionsState.CHOOSE_MEETING_OPERATION);

        Mockito.verify(messageSender, Mockito.times(2)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step three
        Mockito.reset(messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        Message partisipantsMessage = messageFactory("1, 11, 21, 32");

        optionsService.checkOptionsState(partisipantsMessage,OptionsState.CREATE_MEETING);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(redisRepository, Mockito.times(1)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step four
        Mockito.reset(messageSender, redisRepository);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));

        Message timeMessage = messageFactory("13:30");

        optionsService.checkOptionsState(timeMessage,OptionsState.ADD_MEETING_PARTICIPANTS);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(Mockito.any(SendMessage.class));
        Mockito.verify(redisRepository, Mockito.times(2)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        //step five
        Mockito.reset(messageSender, redisRepository);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));
        Mockito.doNothing().when(redisRepository).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),Mockito.any(String.class));
        Mockito.when(meetingService.createMeeting(Mockito.any(Long.class))).thenReturn(null);

        String meetingTitle = "Some meeting";
        Message titleMessage = messageFactory(meetingTitle);
        ArgumentCaptor<String> titleCapture = ArgumentCaptor.forClass(String.class);

        optionsService.checkOptionsState(titleMessage,OptionsState.ADD_MEETING_TIME);
        Mockito.verify(messageSender, Mockito.times(1)).sendResponse(sendMessageArgumentCaptor.capture());
        Mockito.verify(meetingService, Mockito.times(1)).createMeeting(Mockito.any(Long.class));
        Mockito.verify(redisRepository, Mockito.times(1)).saveMeetingPart(Mockito.any(Long.class),Mockito.any(String.class),titleCapture.capture());
        Mockito.verify(optionsActions, Mockito.times(1)).callingCreateMeetingAction();

        SendMessage responseMessage = sendMessageArgumentCaptor.getValue();
        String titleInRedis = titleCapture.getValue();
        assertNotNull(titleInRedis);
        assertEquals(titleInRedis,meetingTitle);

        assertNotNull(responseMessage);
        String text = responseMessage.getText();
        assertEquals(text,"Встреча создана, приглашения отправлены.");
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
