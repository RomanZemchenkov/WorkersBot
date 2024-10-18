package com.roman.service.dto.token;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ShowTokenDto {

    private final Long id;
    private final String token;
    private final String password;

    public ShowTokenDto(Long id, String token, String password) {
        this.id = id;
        this.token = token;
        this.password = password;
    }
}
