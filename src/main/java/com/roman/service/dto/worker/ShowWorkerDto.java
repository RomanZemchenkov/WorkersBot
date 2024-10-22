package com.roman.service.dto.worker;

import lombok.Getter;

@Getter
public class ShowWorkerDto {

    private final String id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String post;

    public ShowWorkerDto(String id, String username, String firstname, String lastname, String post) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.post = post;
    }

    public String toString(){
        return "%s %s %s %s".formatted(username,firstname,lastname,post);
    }
}
