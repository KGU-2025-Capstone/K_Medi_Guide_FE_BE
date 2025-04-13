package com.yakddok.k_medi_guide.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "CardNews")
public class CardNews {
    @Id
    private String id;
    private String title;
    private List<String> images;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
