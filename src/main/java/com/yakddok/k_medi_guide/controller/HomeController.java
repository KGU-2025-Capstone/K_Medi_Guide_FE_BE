package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponseTranslateCardDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class HomeController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private final TranslateCardController translateCardController;

    @GetMapping("/")
    public String home() {
        return "home"; // home.html을 보여줌
    }

    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }

    @GetMapping("/drugSearch")
    public String drugSearch(Model model) {
        return "drugSearch";
    }

    @GetMapping("/drugSpecify/{id}")
    public String drugSpecify(@PathVariable("id") int id, Model model) {
        return "drugSpecify";
    }

    @GetMapping("/translateCard")
    public String translateCard(Model model) {
        ResponseEntity<List<ResponseTranslateCardDTO>> response = translateCardController.getAllTranslateCards();

        List<ResponseTranslateCardDTO> cards = response.getBody(); // 카드 리스트 조회
        model.addAttribute("cards", cards);           // "cards" 이름으로 model에 추가
        return "Translation";
    }

    @GetMapping("/pharmacies")
    public String pharmaciesPage(Model model) {
        return "pharmacies";
    }

}
