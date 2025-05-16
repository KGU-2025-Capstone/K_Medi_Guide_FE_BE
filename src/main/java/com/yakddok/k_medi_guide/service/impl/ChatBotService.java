package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.response.ResponseChatBotDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatBotService {

    private final WebClient webClient;
    private final Builder webClientBuilder;

    public ChatBotService(
        @Qualifier("webClientBuilder") Builder webClientBuilder) {
        this.webClient = WebClient.builder()
            .baseUrl("http://localhost:5000") // Flask 서버 주소
            .build();
        this.webClientBuilder = webClientBuilder;
    }

    public ResponseEntity<ResponseChatBotDTO> send(String next, String input) {

        ResponseChatBotDTO response = webClient.post()
            .uri("api/medicine/" + next)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(input)
            .retrieve()
            .bodyToMono(ResponseChatBotDTO.class)
            .block();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
