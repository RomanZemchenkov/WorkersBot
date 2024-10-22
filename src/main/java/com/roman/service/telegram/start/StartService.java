package com.roman.service.telegram.start;

import com.roman.service.stage.Stages;
import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.Post;
import com.roman.dao.entity.Worker;
import com.roman.service.StateService;
import com.roman.service.WorkerService;
import com.roman.service.stage.States;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StartService {

    private final WorkerService workerService;
    private final StateService stateService;


    @Transactional(readOnly = true)
    public String checkWorker(Long userId){
        Optional<Worker> mayBeWorker = workerService.findAllWorkerInformation(userId);
        return mayBeWorker.isEmpty() ? ifWorkerEmpty() : ifWorkerPresent(mayBeWorker);
    }

    private String ifWorkerEmpty(){
        return StartMessage.WORKER_DOESNT_REGISTERED;
    }

    private String ifWorkerPresent(Optional<Worker> worker){
        Worker currentWorker = worker.get();
        PersonalInfo personalInfo = currentWorker.getPersonalInfo();
        Post workerPost = personalInfo.getPost();
        StringBuilder sb = new StringBuilder();
        sb.append(StartMessage.WORKER_ALREADY_EXIST.formatted(personalInfo.getFirstname()));

        String response;
        if(workerPost.getTitle().equals("director")){
            //тут мы проверяем, есть ли у директора какие-то встречи и выбираем варианты - если есть, то в метод, если нет, то так и пишем
            response = ifDirectorHasMeeting(sb,currentWorker).toString();
        } else {
            //логика для обычного сотрудника примерно такая же, но мы показываем задачи
            response = ifWorkerHasTask(sb,currentWorker).toString();
        }
        stateService.updateOrCreateWorkerState(currentWorker.getId(), Stages.ONLINE, States.CHOOSE.name());
        return response;
    }

    private StringBuilder ifDirectorHasMeeting(StringBuilder sb,Worker worker){
        //тут нужно как-то получать список задач откуда-то и в цикле добавлять в билдер
        sb.append("Встреча 1 ");
        sb.append("Встреча 2 ");
        sb.append("Встреча 3 ");
        return sb;
    }

    private StringBuilder ifWorkerHasTask(StringBuilder sb, Worker worker){
        sb.append("Задача 1 ");
        sb.append("Задача 2 ");
        sb.append("Задача 3 ");
        return sb;
    }

}
