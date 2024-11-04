package com.roman.service.stage;

public enum OptionsState {

    CHOOSE, //выбирает действие
    OBSERVED_WORKERS, //смотрит на сотрудников
    OBSERVED_WORKER,// смотрит на одного сотрудника
    CHOOSE_WORKER_FOR_CALLING, //выбирает сотрудника для того, чтобы позвать

    CHOOSE_MEETING_OPERATION, //выбирает операцию в меню встреч
    CREATE_MEETING, //создает новую встречу
    ADD_MEETING_PARTICIPANTS,
    ADD_MEETING_TIME,
//    ADD_MEETING_TITLE,
    OBSERVED_MEETINGS, //смотрит список существующих встреч
    OBSERVED_ONE_MEETING, //смотрит всю информацию об одной встрече
    CHANGE_MEETING, // меняет информацию о встрече
    CHANGE_PARTICIPANTS, // меняет информацию об участниках встречи

    WILL_CHANGE_WORKER_INFORMATION, //будет менять информацию о сотруднике
    EMPTY
}
