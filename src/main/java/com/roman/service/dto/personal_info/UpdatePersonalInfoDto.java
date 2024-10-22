package com.roman.service.dto.personal_info;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdatePersonalInfoDto {

    private final String workerId;
    private final String firstname;
    private final String lastname;
    private final String patronymic;
    private final String username;
    private final String birthday;
    private final String post;

    public UpdatePersonalInfoDto(String workerId, String firstname, String lastname, String patronymic, String username, String birthday, String post) {
        this.workerId = workerId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.username = username;
        this.birthday = birthday;
        this.post = post;
    }
}
