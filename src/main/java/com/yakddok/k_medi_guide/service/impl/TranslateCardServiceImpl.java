package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.response.ResponseTranslateCardDTO;
import com.yakddok.k_medi_guide.entity.TranslateCard;
import com.yakddok.k_medi_guide.repository.TranslateCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslateCardServiceImpl {

    private final TranslateCardRepository translateCardRepository;

    // 번역카드 전체 조회
    public List<ResponseTranslateCardDTO> getAllTranslateCards() {
        return translateCardRepository.findAll().stream().map(ResponseTranslateCardDTO::new).collect(Collectors.toList());
    }

    // 특정 번역카드 조회
    public ResponseTranslateCardDTO getTranslateCard(String id) {
        TranslateCard translateCard = translateCardRepository.findById(id).orElseThrow();
        return new ResponseTranslateCardDTO(translateCard);
    }

    // 번역카드 저장
    public void saveTranslateCard(String K_symptoms, String E_symptoms, String J_symptoms, String C_symptoms, String imageAddress) {
        TranslateCard translateCard = TranslateCard.builder()
                .K_symptoms(K_symptoms)
                .E_symptoms(E_symptoms)
                .J_symptoms(J_symptoms)
                .C_symptoms(C_symptoms)
                .imageAddress(imageAddress)
                .build();
        translateCardRepository.save(translateCard);
    }
}
