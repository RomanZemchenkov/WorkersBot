package com.roman.service.telegram.registration;

import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.PersonalToken;
import com.roman.service.CompanyService;
import com.roman.service.PersonalInfoService;
import com.roman.service.PersonalTokenService;
import com.roman.service.WorkerService;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import com.roman.service.telegram.TelegramMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import static com.roman.service.dto.ResponseMessage.REGISTRATION_MESSAGE;

@Component
public class RegistrationActions {

    private final TelegramMessageSender sender;
    private final PersonalInfoService personalInfoService;
    private final PersonalTokenService personalTokenService;
    private final CompanyService companyService;
    private final WorkerService workerService;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationActions.class);

    public RegistrationActions(@Lazy TelegramMessageSender sender,
                               PersonalInfoService personalInfoService,
                               PersonalTokenService personalTokenService,
                               CompanyService companyService,
                               WorkerService workerService) {
        this.sender = sender;
        this.personalInfoService = personalInfoService;
        this.personalTokenService = personalTokenService;
        this.companyService = companyService;
        this.workerService = workerService;
    }

    //Логика по созданию пользователя
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Action<RegistrationState, RegistrationEvent> afterRegistrationAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            SendMessage response = createWorker(message);
            sender.sendResponse(response);
        };
    }

    //логика по назначению должности
    public Action<RegistrationState, RegistrationEvent> afterEnterPostAction(String responseMessage) {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            String post = message.getText();
            Long workerId = message.getFrom().getId();
            personalInfoService.updatePersonalInfoPost(workerId, post);

            String chatId = String.valueOf(message.getChatId());

            logger.info("Worker with id {} put post {}", workerId, post);

            SendMessage sendMessage = new SendMessage(chatId, responseMessage);
            sender.sendResponse(sendMessage);
        };

    }
    //Логика по созданию компании после регистрации должности директора
    public Action<RegistrationState, RegistrationEvent> afterDirectorEnterCompanyName() {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            String companyName = message.getText();
            companyService.findCompanyOrCreate(companyName);
            Long workerId = message.getFrom().getId();
            String chatId = String.valueOf(message.getChatId());

            String messageText = "Пожалуйста, введи ваш день рождения.";
            updateCompany(companyName,workerId,chatId,messageText);
        };
    }

    //Данный метод срабатывает, если выбраная должность не директор
    public Action<RegistrationState, RegistrationEvent> sendNotificationToUserAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            Long userId = message.getFrom().getId();

            String chatId = String.valueOf(message.getChatId());

            logger.info("Worker with id {} take notification",userId);

            SendMessage sendMessage = new SendMessage(chatId, "Пожалуйста, введи username вашего директора в форме 'username' or '@username'.");
            sender.sendResponse(sendMessage);
        };
    }

    //Данные метод ищет компанию по нику директора и присваивет её пользователю
    public Action<RegistrationState, RegistrationEvent> afterEnterDirectorUsername() {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            Long workerId = message.getFrom().getId();
            String directorUsername = message.getText();
            if(directorUsername.startsWith("@")){
                directorUsername = directorUsername.substring(1);
            }
            PersonalInfo directorPersonalInfo = personalInfoService.findPersonalInfoWithCompany(directorUsername);
            String companyName = directorPersonalInfo.getCompany().getName();
            String chatId = String.valueOf(message.getChatId());

            String messageText = "Пожалуйста, введите вашу должность.";
            updateCompany(companyName,workerId,chatId,messageText);
        };
    }



    //Логика по обновлению дня рождения
    public Action<RegistrationState, RegistrationEvent> enterBirthdayAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader("message");
            String birthday = message.getText();
            Long workerId = message.getFrom().getId();

            personalInfoService.updatePersonalInfoBirthday(workerId,birthday);

            String chatId = String.valueOf(message.getChatId());
            logger.info("Worker with id {} put birthday {}", workerId, birthday);

            SendMessage sendMessage = new SendMessage(chatId, "Спасибо за регистрацию!");
            sender.sendResponse(sendMessage);
        };
    }

    private SendMessage createWorker(Message message) {
        User user = message.getFrom();
        RegistrationWorkerDto workerDto = createWorkerDto(user);
        workerService.createWorker(workerDto);
        System.out.println("1");
        personalInfoService.createPersonalInfo(workerDto);
        System.out.println("2");
        PersonalToken savedToken = personalTokenService.createToken(user.getId());
        System.out.println("3");
        String responseMessage = REGISTRATION_MESSAGE.formatted(savedToken.getToken(), savedToken.getPassword());
        logger.info("New worker was create");
        return new SendMessage(String.valueOf(message.getChatId()), responseMessage);
    }

    private void updateCompany(String companyName, Long workerId, String chatId, String message){
        personalInfoService.updatePersonalInfoCompany(workerId,companyName);

        logger.info("Worker with id {} put company {}", workerId, companyName);

        SendMessage sendMessage = new SendMessage(chatId, message);
        sender.sendResponse(sendMessage);
    }

    private RegistrationWorkerDto createWorkerDto(User user) {
        String workerId = String.valueOf(user.getId());
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userName = user.getUserName();
        return new RegistrationWorkerDto(workerId, firstName, lastName, userName);
    }
}
