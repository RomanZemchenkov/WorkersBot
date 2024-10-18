package com.roman.service.telegram;

import com.roman.dao.entity.Worker;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TelegramBotTest extends DockerInitializer {

    private final EntityManager entityManager;
    private final TelegramBot telegramBot;
    @SpyBean
    private TelegramMessageSender messageSender;
    @SpyBean
    private TelegramCommandImpl telegramCommand;

    private static final String CHAT_ID = "11";

    @Autowired
    public TelegramBotTest(EntityManager entityManager, TelegramBot telegramBot) {
        this.entityManager = entityManager;
        this.telegramBot = telegramBot;
    }

    @Test
    @Transactional
    void fullDirectorRegistrationTest() {

        Message registratioMessage = messageFactory("/registration");
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        telegramBot.checkQueryFromUser(registratioMessage);

        Mockito.verify(telegramCommand, Mockito.times(1)).registration(registratioMessage);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.argThat((SendMessage message) -> message.getChatId().equals(CHAT_ID)));

        //Second
        Mockito.reset(telegramCommand,messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message directorMessage = messageFactory("director");
        telegramBot.checkQueryFromUser(directorMessage);

        Mockito.verify(telegramCommand,Mockito.times(1)).anotherMessage(directorMessage);
        Mockito.verify(messageSender,Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));


        //third
        Mockito.reset(telegramCommand, messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message companyName = messageFactory("ООО'ооо'");
        telegramBot.checkQueryFromUser(companyName);

        Mockito.verify(telegramCommand, Mockito.times(1)).anotherMessage(companyName);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));


        //fourth
        Mockito.reset(telegramCommand, messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message birthdayMessage = messageFactory("2000-02-08");
        telegramBot.checkQueryFromUser(birthdayMessage);

        Mockito.verify(telegramCommand, Mockito.times(1)).anotherMessage(birthdayMessage);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));


        Worker worker = entityManager.createQuery("Select w FROM worker w WHERE w.id = :id", Worker.class)
                .setParameter("id", 1L)
                .getSingleResult();

        System.out.println(worker);

    }

    @Test
    @Transactional
    void fullWorkerRegistrationTest() {
        entityManager.createNativeQuery("INSERT INTO worker(id) VALUES(100)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO post(id, title) VALUES(10,'director')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO company(id, name) VALUES(1,'Работяги')").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO personal_info(worker_id, firstname, lastname, patronymic, username, birthday, company_id, post_id)" +
                                        " VALUES(100,'Roman','Test','Test','Username','2000-02-02',1,10)").executeUpdate();

        Message registratioMessage = messageFactory("/registration");
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        telegramBot.checkQueryFromUser(registratioMessage);

        Mockito.verify(telegramCommand, Mockito.times(1)).registration(registratioMessage);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.argThat((SendMessage message) -> message.getChatId().equals(CHAT_ID)));

        //Second
        Mockito.reset(telegramCommand,messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message anotherWorkerMessage = messageFactory("another");
        telegramBot.checkQueryFromUser(anotherWorkerMessage);

        Mockito.verify(telegramCommand,Mockito.times(1)).anotherMessage(anotherWorkerMessage);
        Mockito.verify(messageSender,Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));

        //third
        Mockito.reset(telegramCommand, messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message directorUsername = messageFactory("Username");
        telegramBot.checkQueryFromUser(directorUsername);

        Mockito.verify(telegramCommand, Mockito.times(1)).anotherMessage(directorUsername);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));

        //fourth
        Mockito.reset(telegramCommand, messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message postMessage = messageFactory("Работяга");
        telegramBot.checkQueryFromUser(postMessage);

        Mockito.verify(telegramCommand, Mockito.times(1)).anotherMessage(postMessage);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));

        //five
        Mockito.reset(telegramCommand, messageSender);
        Mockito.doNothing().when(messageSender).sendResponse(Mockito.any(SendMessage.class));

        Message birthdayMessage = messageFactory("2000-02-08");
        telegramBot.checkQueryFromUser(birthdayMessage);

        Mockito.verify(telegramCommand, Mockito.times(1)).anotherMessage(birthdayMessage);
        Mockito.verify(messageSender, Mockito.times(1))
                .sendResponse(Mockito.any(SendMessage.class));


        Worker worker = entityManager.createQuery("Select w FROM worker w WHERE w.id = :id", Worker.class)
                .setParameter("id", 1L)
                .getSingleResult();

        Long workers = entityManager.createQuery("SELECT count(w) FROM worker w WHERE w.personalInfo.post.title = :postTitle", Long.class)
                .setParameter("postTitle", "Работяга")
                .getSingleResult();

        Long directors = entityManager.createQuery("SELECT count(w) FROM worker w WHERE w.personalInfo.post.title = :postTitle", Long.class)
                .setParameter("postTitle", "director")
                .getSingleResult();

        assertThat(worker.getPersonalInfo().getFirstname()).isEqualTo("Roman");
        assertThat(workers).isEqualTo(1);
        assertThat(directors).isEqualTo(1);


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
