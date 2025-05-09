package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.CardNewsDTO;
import com.yakddok.k_medi_guide.dto.response.TranslateCardDTO;
import com.yakddok.k_medi_guide.service.impl.TranslateCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TranslateCardController {

    private final TranslateCardService translateCardService;

    // 번역카드 작성
    @PostMapping("/translateCard")
    public ResponseEntity<String> uploadTranslateCard(@RequestParam("K_symptoms") String K_symptoms, @RequestParam("E_symptoms") String E_symptoms, @RequestParam("J_symptoms") String J_symptoms, @RequestParam("C_symptoms") String C_symptoms, @RequestParam("imageAddress") String imageAddress) {
        translateCardService.saveTranslateCard(K_symptoms, E_symptoms, C_symptoms, J_symptoms, imageAddress);
        return ResponseEntity.ok("번역카드가 성공적으로 저장되었습니다.");
    }

    // 전체 번역카드 조회
    @GetMapping("/translateCard")
    public ResponseEntity<List<TranslateCardDTO>> getAllTranslateCards() {
        List<TranslateCardDTO> cards = translateCardService.getAllTranslateCards();
        return ResponseEntity.ok(cards);
    }

    // 특정 번역카드 조회
    @GetMapping("/translateCard/{id}")
    public ResponseEntity<TranslateCardDTO> getTranslateCardById(@PathVariable String id) {
        TranslateCardDTO card = translateCardService.getTranslateCard(id);
        return ResponseEntity.ok(card);
    }
}
