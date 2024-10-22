package com.roman.service.telegram.options;

import com.roman.service.PersonalInfoService;
import com.roman.service.WorkerService;
import com.roman.service.dto.worker.ShowFullInfoWorkerDto;
import com.roman.service.dto.worker.ShowWorkerDto;
import com.roman.service.exception.MessageFormatException;
import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
import com.roman.service.telegram.TelegramMessageSender;
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
import java.util.Arrays;
import java.util.List;

@Component
public class OptionsActions {

    private final TelegramMessageSender messageSender;
    private final WorkerService workerService;
    private final PersonalInfoService personalInfoService;
    private static final String OPTIONS_ACTION_KEY = "message";

    @Autowired
    public OptionsActions(@Lazy TelegramMessageSender messageSender,
                          WorkerService workerService,
                          PersonalInfoService personalInfoService) {
        this.messageSender = messageSender;
        this.workerService = workerService;
        this.personalInfoService = personalInfoService;
    }

    public Action<OptionsState, OptionEvent> watchWorkersAction(){
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            User currentUser = message.getFrom();
            Long directorId = currentUser.getId();

            List<ShowWorkerDto> allWorkers = workerService.findAllWorkers(directorId);
            StringBuilder sb = new StringBuilder();
            allWorkers.forEach(worker -> sb.append(worker.toString()).append("\n"));
            send(message,sb.toString());
        };
    }

    public Action<OptionsState, OptionEvent> watchOneWorkerAction(){
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String workerUsername;
            try {
                workerUsername = message.getText().split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e){
                throw new MessageFormatException("Вы не ввели юзернейм пользователя");
            }
            ShowFullInfoWorkerDto fullInfo = personalInfoService.findFullInfo(workerUsername);
            send(message,fullInfo.toString());
        };
    }

    public Action<OptionsState, OptionEvent> callingWorkerAction(){
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String workerUsername;
            try {
                workerUsername = message.getText().split(" ")[1];
            } catch (ArrayIndexOutOfBoundsException e){
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
            send(message,sb.toString());
        };
    }

    public Action<OptionsState, OptionEvent> callingWorkerWithTimeAction(){
        return context -> {
            Message message = (Message) context.getMessageHeader(OPTIONS_ACTION_KEY);
            String[] commandBySpit = message.getText().split(" ");
            String workerUsername;
            try {
                workerUsername = commandBySpit[1];
            } catch (Throwable e){
                throw new MessageFormatException("Вы не ввели юзернейм пользователя");
            }
            LocalDateTime time;
            try {
                 time = commandBySpit.length == 3 ?
                         formatToLocalDateTime(commandBySpit[2]) :
                         formatToLocalDateTime(commandBySpit[2],commandBySpit[3]);
            } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e){
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
            send(message,sb.toString());
        };
    }

    private LocalDateTime formatToLocalDateTime(String... time) throws ArrayIndexOutOfBoundsException, DateTimeParseException {
        LocalDateTime meetingTime;
        if(time.length == 1){
            String[] splitTime = time[0].split(":");
            LocalTime localTime = LocalTime.of(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
            meetingTime = LocalDateTime.of(LocalDate.now(),localTime);
        } else {
            String[] splitDate = time[0].split("-");
            String[] splitTime = time[1].split(":");
            meetingTime = LocalDateTime.of(Integer.parseInt(splitDate[0]),Integer.parseInt(splitDate[1]),Integer.parseInt(splitDate[2]),Integer.parseInt(splitTime[0]),Integer.parseInt(splitTime[1]));
        }
        return meetingTime;
    }

    private void send(Message message, String responseMessage){
        String chatId = String.valueOf(message.getChatId());
        SendMessage response = new SendMessage(chatId, responseMessage);
        messageSender.sendResponse(response);
    }

}
