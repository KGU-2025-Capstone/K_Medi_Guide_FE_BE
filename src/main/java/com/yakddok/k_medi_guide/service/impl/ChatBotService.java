package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.request.RequestChatBotDTO;
import com.yakddok.k_medi_guide.dto.response.ResponseChatBotDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

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
        return webClient.post()
            .uri("/api/medicine/" + next)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new RequestChatBotDTO(input))
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful() || clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    return clientResponse.bodyToMono(ResponseChatBotDTO.class)
                        .map(response -> ResponseEntity.status(clientResponse.statusCode()).body(response));
                } else {
                    return clientResponse.createError(); // 다른 에러 상태 코드는 예외를 던집니다.
                }
            })
            .block();
    }

}
