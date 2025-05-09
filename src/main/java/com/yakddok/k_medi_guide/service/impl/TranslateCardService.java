package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.response.TranslateCardDTO;
import com.yakddok.k_medi_guide.entity.TranslateCard;
import com.yakddok.k_medi_guide.repository.TranslateCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslateCardService {

    private final TranslateCardRepository translateCardRepository;

    // 번역카드 전체 조회
    public List<TranslateCardDTO> getAllTranslateCards() {
        return translateCardRepository.findAll().stream().map(TranslateCardDTO::new).collect(Collectors.toList());
    }

    // 특정 번역카드 조회
    public TranslateCardDTO getTranslateCard(String id) {
        TranslateCard translateCard = translateCardRepository.findById(id).orElseThrow();

        return new TranslateCardDTO(translateCard);
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
