package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponseChatBotDTO;
import com.yakddok.k_medi_guide.service.impl.ChatBotService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    @PostMapping("/api/chatbot")
    public ResponseEntity<ResponseChatBotDTO> sendChatBot(@RequestBody Map<String, String> json) {
        String next = json.get("next");
        String input = json.get("input");
        if(next.charAt(0) == '/'){
            next = next.substring(1);
        }
        try {
            ResponseEntity<ResponseChatBotDTO> response = chatBotService.send(next, input);
            return response;
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
