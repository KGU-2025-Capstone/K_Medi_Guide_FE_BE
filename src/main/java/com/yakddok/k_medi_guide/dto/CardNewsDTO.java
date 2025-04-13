package com.yakddok.k_medi_guide.dto;

import com.yakddok.k_medi_guide.entity.CardNews;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardNewsDTO {
    private final String id;
    private final String title;
    private final List<String> images;
    private final String thumbnail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CardNewsDTO (CardNews cardNews) {
        this.id = cardNews.getId();
        this.title = cardNews.getTitle();
        this.images = cardNews.getImages();
        this.thumbnail = cardNews.getThumbnail();
        this.createdAt = cardNews.getCreatedAt();
        this.updatedAt = cardNews.getUpdatedAt();
    }
}
