package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.ChatBotDTO;
import com.yakddok.k_medi_guide.service.impl.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/summary")
    public Mono<ChatBotDTO> getSummary(@RequestParam String query) {
        return chatBotService.getMedicineSummary(query);
    }
}
