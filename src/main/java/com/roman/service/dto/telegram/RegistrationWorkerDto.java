package com.roman.service.dto.telegram;

import lombok.Getter;

@Getter
public class RegistrationWorkerDto {

    private final String workerId;
    private final String firstname;
    private final String lastname;
    private final String username;

    public RegistrationWorkerDto(String workerId, String firstname, String lastname, String username) {
        this.workerId = workerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }

}
