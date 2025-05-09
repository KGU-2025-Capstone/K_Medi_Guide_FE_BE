package com.yakddok.k_medi_guide.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "TranslateCard")
public class TranslateCard {
    @Id
    private String id;
    private String K_symptoms;
    private String E_symptoms;
    private String J_symptoms;
    private String C_symptoms;
    private String imageAddress;
}
