package com.roman.service.dto.worker;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShowFullInfoWorkerDto {

    private final String id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String state;
    private final String post;
    private final String chatId;

    public ShowFullInfoWorkerDto(String id, String username, String firstname, String lastname, String state, String post, String chatId) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.state = state;
        this.post = post;
        this.chatId = chatId;
    }
}
