package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponseChatBotDTO;
import com.yakddok.k_medi_guide.service.impl.ChatBotServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
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

    private final ChatBotServiceImpl chatBotServiceImpl;

    @PostMapping("/api/chatbot/new")
    public ResponseEntity<String> newSession(HttpServletResponse response) {
        ResponseEntity<String> flaskResponse = chatBotServiceImpl.newSession();
        if (flaskResponse != null) {
            List<String> setCookieHeaders = flaskResponse.getHeaders().get("Set-Cookie");
            if (setCookieHeaders != null) {
                for (String setCookieHeader : setCookieHeaders) {
                    response.addHeader("Set-Cookie", setCookieHeader);
                }
            }
            return new ResponseEntity<>("New session created successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to create new session", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/chatbot")
    public ResponseEntity<ResponseChatBotDTO> sendChatBot(@RequestBody Map<String, String> json, HttpServletRequest request, HttpServletResponse response) {
        String next = json.get("next");
        String input = json.get("input");
        String lang = json.get("lang");
        String sessionId = getSessionIdCookie(request); // 요청에서 세션 쿠키 추출

        if (next != null && next.charAt(0) == '/') {
            next = next.substring(1);
        }

        try {
            ResponseEntity<ResponseChatBotDTO> flaskResponse = chatBotServiceImpl.send(next, input,lang, sessionId);

            List<String> setCookieHeaders = flaskResponse.getHeaders().get("Set-Cookie");
            if (setCookieHeaders != null) {
                for (String setCookieHeader : setCookieHeaders) {
                    response.addHeader("Set-Cookie", setCookieHeader);
                }
            }

            return flaskResponse;
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getSessionIdCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session".equals(cookie.getName())) { // 플라스크 세션 쿠키 이름 (확인 필요)
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}