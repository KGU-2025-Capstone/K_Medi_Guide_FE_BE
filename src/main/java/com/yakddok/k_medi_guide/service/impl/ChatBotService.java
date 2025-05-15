package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.ChatBotDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatBotService {

    private final WebClient webClient;

    public ChatBotService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:5000") // Flask 서버 주소
                .build();
    }

    public Mono<ChatBotDTO> getMedicineSummary(String query) {
        return webClient.post()
                .uri("/medicine/summary")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("query", query))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody ->
                                Mono.error(new RuntimeException("에러 응답: " + errorBody))
                        )
                )
                .bodyToMono(ChatBotDTO.class)
                .doOnError(e -> System.out.println("에러 발생: " + e.getMessage()));
    }
}
