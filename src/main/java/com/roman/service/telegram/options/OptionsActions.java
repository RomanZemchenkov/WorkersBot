package com.roman.service.telegram.options;

import com.roman.service.PersonalInfoService;
import com.roman.service.WorkerService;
import com.roman.service.dto.worker.ShowFullInfoWorkerDto;
import com.roman.service.dto.worker.ShowWorkerDto;
import com.roman.service.exception.MessageFormatException;
import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
import com.roman.service.telegram.TelegramMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class OptionsActions {

    private final TelegramMessageSender messageSender;
    private final WorkerService workerService;
    private final PersonalInfoService personalInfoService;
    private static final String OPTIONS_ACTION_KEY = "message";
    private static final Logger logger = LoggerFactory.getLogger(OptionsActions.class);

    @Autowired
    public OptionsActions(@Lazy TelegramMessageSender messageSender,
                          WorkerService workerService,
                          PersonalInfoService personalInfoService) {
        this.messageSender = messageSender;
        this.workerService = workerService;
        this.personalInfoService = personalInfoService;
    }

    public Action<OptionsState, OptionEvent> watchWorkersAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            User currentUser = message.getFrom();
            Long directorId = currentUser.getId();

            List<ShowWorkerDto> allWorkers = workerService.findAllWorkers(directorId);
            StringBuilder sb = new StringBuilder();
            allWorkers.forEach(worker -> sb.append(worker.toString()).append("\n"));
            send(message, sb.toString());
        };
    }

    public Action<OptionsState, OptionEvent> watchOneWorkerAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String workerUsername;
            try {
                workerUsername = message.getText().split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MessageFormatException("Вы не ввели юзернейм пользователя");
            }
            ShowFullInfoWorkerDto fullInfo = personalInfoService.findFullInfo(workerUsername);
            send(message, fullInfo.toString());
        };
    }

    public Action<OptionsState, OptionEvent> callingWorkerAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String workerUsername;
            try {
                workerUsername = message.getText().split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new MessageFormatException("Вы не ввели юзернейм пользователя");
            }
            //отправить сообщение сотруднику
            ShowFullInfoWorkerDto fullInfo = personalInfoService.findFullInfo(workerUsername);
            String workerChatId = fullInfo.getChatId();
            SendMessage messageToWorker = new SendMessage(workerChatId, "Директор приглашает вас в свой кабинет. Пройдите, пожалуйста.");
            messageSender.sendResponse(messageToWorker);
            //отправить сообщение директору со списком всех сотрудников и сообщением о том, что уведовление доставлено
            User currentUser = message.getFrom();
            Long directorId = currentUser.getId();
            List<ShowWorkerDto> allWorkers = workerService.findAllWorkers(directorId);
            StringBuilder sb = new StringBuilder("Приглашение успешно доставлено.\n");
            allWorkers.forEach(worker -> sb.append(worker.toString()).append("\n"));
            send(message, sb.toString());
        };
    }

    public Action<OptionsState, OptionEvent> callingWorkerWithTimeAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String[] commandBySpit = message.getText().split(" ");
            String workerUsername;
            try {
                workerUsername = commandBySpit[1];
            } catch (Throwable e) {
                throw new MessageFormatException("Вы не ввели юзернейм пользователя");
            }
            LocalDateTime time;
            try {
                time = commandBySpit.length == 3 ?
                        formatToLocalDateTime(commandBySpit[2]) :
                        formatToLocalDateTime(commandBySpit[2], commandBySpit[3]);
            } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
                throw new RuntimeException("Неправильно ввели дату");
            }
            //отправить сообщение сотруднику
            ShowFullInfoWorkerDto fullInfo = personalInfoService.findFullInfo(workerUsername);
            String workerChatId = fullInfo.getChatId();
            SendMessage messageToWorker = new SendMessage(workerChatId, "Директор приглашает вас в свой кабинет в %s. Поставьте себе напоминание."
                    .formatted(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            messageSender.sendResponse(messageToWorker);
            //отправить сообщение директору со списком всех сотрудников и сообщением о том, что уведовление доставлено
            User currentUser = message.getFrom();
            Long directorId = currentUser.getId();
            List<ShowWorkerDto> allWorkers = workerService.findAllWorkers(directorId);
            StringBuilder sb = new StringBuilder("Приглашение успешно доставлено.\n");
            allWorkers.forEach(worker -> sb.append(worker.toString()).append("\n"));
            send(message, sb.toString());
        };
    }

    /*
    Meetings block
     */

    public Action<OptionsState, OptionEvent> callingMeetingMenu() {
        return context -> {
            logger.info("User want to watch meetings menu.");
        };
    }

    public Action<OptionsState, OptionEvent> callingCreateMeetingAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            /*
            Нужно создать запись в редисе, которую мы дальше будем заполнять
            Ну и нужно будет вернуть список струдников с их номерами от 1 до n
            Для этого мы будем в кеш редиса сохранять id сотрудника под ключом directorId::number::workerId
            А дальше, когда номера сотрудников будут выбраны, мы просто будем из кеша доставать их и чистить данный кеш
            */

            send(message, "Вам представлен список ваших сотрудников. Введите, пожалуйста, их номера из этого списка через запятую.");
        };
    }


    public Action<OptionsState, OptionEvent> settingMeetingParticipantsAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String participantsList = message.getText();
            send(message, "Введите, пожалуйста, время встречи в одном из двух форматов:\n" +
                          "1. yyyy-MM-dd HH:mm Пример: 2024-10-10 13:30 - встреча будет назначена на определённую дату\n" +
                          "2. HH:mm Пример: 13:30 - встреча будет назначена на сегодня\n");
        };
    }


    public Action<OptionsState, OptionEvent> settingMeetingTimeAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String time = message.getText();
            send(message, "Введите, пожалуйста, название или тему встречи.");
        };
    }

    public Action<OptionsState, OptionEvent> settingMeetingTitleAction() {
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String title = message.getText();
            send(message, "Встреча создана, приглашения отправлены.");
        };
    }

    private LocalDateTime formatToLocalDateTime(String... time) throws ArrayIndexOutOfBoundsException, DateTimeParseException {
        LocalDateTime meetingTime;
        if (time.length == 1) {
            String[] splitTime = time[0].split(":");
            LocalTime localTime = LocalTime.of(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
            meetingTime = LocalDateTime.of(LocalDate.now(), localTime);
        } else {
            String[] splitDate = time[0].split("-");
            String[] splitTime = time[1].split(":");
            meetingTime = LocalDateTime.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
        }
        return meetingTime;
    }

    private void send(Message message, String responseMessage) {
        String chatId = String.valueOf(message.getChatId());
        SendMessage response = new SendMessage(chatId, responseMessage);
        messageSender.sendResponse(response);
    }


}
