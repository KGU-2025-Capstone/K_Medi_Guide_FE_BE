package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.request.RequestChatBotDTO;
import com.yakddok.k_medi_guide.dto.response.ResponseChatBotDTO;
import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ChatBotServiceImpl {

    private final WebClient webClient;

    @Setter
    private String sessionIdCookie; // 플라스크 세션 쿠키 저장 변수

    public ChatBotServiceImpl(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000") // Flask 서버 주소
            .build();
    }

    public ResponseEntity<ResponseChatBotDTO> send(String next, String input, String lang, String sessionId) {
        return webClient.post()
            .uri("/api/medicine/" + next)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new RequestChatBotDTO(lang, input))
            .cookie("session", sessionId != null ? sessionId : "")
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().is2xxSuccessful() || clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    return clientResponse.bodyToMono(ResponseChatBotDTO.class)
                        .map(response -> ResponseEntity.status(clientResponse.statusCode()).body(response));
                } else {
                    return clientResponse.createError(); // 다른 에러 상태 코드는 WebClientResponseException 발생
                }
            })
            .block();
    }

    // 플라스크 응답에서 세션 쿠키를 추출하는 메서드 (최초 요청 또는 갱신 시)
    public void updateSessionCookie(ResponseEntity<ResponseChatBotDTO> response) {
        // Set-Cookie 헤더에서 세션 ID 추출
        List<String> setCookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (setCookieHeaders != null) {
            for (String setCookieHeader : setCookieHeaders) {
                if (setCookieHeader.startsWith("session=")) {
                    this.sessionIdCookie = setCookieHeader.split(";")[0].substring("session=".length());
                    break;
                }
            }
        }
    }

    public ResponseEntity<String> newSession() {
        ResponseEntity<String> response = webClient.post()
            .uri("/api/medicine/makeSession")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(String.class)
            .block();

        if (response != null) {
            List<String> setCookieHeaders = response.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (setCookieHeaders != null && !setCookieHeaders.isEmpty()) {
                for (String setCookieHeader : setCookieHeaders) {
                    if (setCookieHeader.startsWith("session=")) {
                        this.sessionIdCookie = setCookieHeader.split(";")[0].substring("session=".length());
                        return response; // Set-Cookie 헤더를 포함한 ResponseEntity 반환
                    }
                }
            }
            return response; // Set-Cookie 헤더가 없어도 ResponseEntity 반환 (확인 필요)
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create new session");
        }
    }
}