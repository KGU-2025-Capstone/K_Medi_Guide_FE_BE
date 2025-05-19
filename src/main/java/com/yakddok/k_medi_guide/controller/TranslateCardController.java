package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponseTranslateCardDTO;
import com.yakddok.k_medi_guide.service.impl.TranslateCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TranslateCardController {

    private final TranslateCardServiceImpl translateCardServiceImpl;

    // 번역카드 작성
    @PostMapping("/translateCard")
    public ResponseEntity<String> uploadTranslateCard(@RequestParam("K_symptoms") String K_symptoms, @RequestParam("E_symptoms") String E_symptoms, @RequestParam("J_symptoms") String J_symptoms, @RequestParam("C_symptoms") String C_symptoms, @RequestParam("imageAddress") String imageAddress) {
        translateCardServiceImpl.saveTranslateCard(K_symptoms, E_symptoms, C_symptoms, J_symptoms, imageAddress);
        return ResponseEntity.ok("번역카드가 성공적으로 저장되었습니다.");
    }

    // 전체 번역카드 조회
    @GetMapping("/translateCard")
    public ResponseEntity<List<ResponseTranslateCardDTO>> getAllTranslateCards() {
        List<ResponseTranslateCardDTO> cards = translateCardServiceImpl.getAllTranslateCards();
        return ResponseEntity.ok(cards);
    }

    // 특정 번역카드 조회
    @GetMapping("/translateCard/{id}")
    public ResponseEntity<ResponseTranslateCardDTO> getTranslateCardById(@PathVariable String id) {
        ResponseTranslateCardDTO card = translateCardServiceImpl.getTranslateCard(id);
        return ResponseEntity.ok(card);
    }
}
