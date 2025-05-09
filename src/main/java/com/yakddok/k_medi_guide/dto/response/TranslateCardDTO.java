package com.yakddok.k_medi_guide.dto.response;

import com.yakddok.k_medi_guide.entity.TranslateCard;
import lombok.Getter;

@Getter
public class TranslateCardDTO {
    private final String id;
    private final String K_symptoms;
    private final String E_symptoms;
    private final String J_symptoms;
    private final String C_symptoms;
    private final String imageAddress;

    public TranslateCardDTO(TranslateCard translateCard) {
        this.id = translateCard.getId();
        this.K_symptoms = translateCard.getK_symptoms();
        this.E_symptoms = translateCard.getE_symptoms();
        this.J_symptoms = translateCard.getJ_symptoms();
        this.C_symptoms = translateCard.getC_symptoms();
        this.imageAddress = translateCard.getImageAddress();
    }
}
