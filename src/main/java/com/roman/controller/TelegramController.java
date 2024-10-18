package com.roman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramController {

//    private final TelegramBotService telegramBotService;

//    @PostMapping(value = "/registration/{chatId}")
//    public ResponseEntity<Response> registration(@PathVariable("chatId") String chatId,
//                                                @RequestBody DirectorRegistrationTelegramDto dto){
//        telegramBotService.registration(dto,chatId);
//        NormalResponse normalResponse = new NormalResponse(true, "Всё ок.");
//        return new ResponseEntity<>(normalResponse, HttpStatus.CREATED);
//    }
}
