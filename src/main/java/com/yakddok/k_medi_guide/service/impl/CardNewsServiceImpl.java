package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.entity.CardNews;
import com.yakddok.k_medi_guide.repository.CardNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardNewsServiceImpl {

    private final CardNewsRepository cardNewsRepository;

    @Value("${file.upload-dir}") // application.properties 파일에서 경로 가져오기
    private String uploadDir;

    public void saveCardNews(List<MultipartFile> files, String title) throws IOException {
        List<String> images = new ArrayList<>(); // 저장할 이미지 리스트
        String thumbnailUrl = null;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String originalName = file.getOriginalFilename();

            // 파일명 충돌 방지를 위해 UUID 추가
            String uniqueName = UUID.randomUUID() + "_" + originalName;
            String filePath = Paths.get(uploadDir, uniqueName).toString();

            // 파일 저장
            file.transferTo(new File(filePath));

            // 리스트에 저장할 파일 경로 추가
            String url = "/uploads/" + uniqueName;
            images.add(url);

            // 첫 번째 파일을 썸네일로 지정
            if (i == 0) {
                thumbnailUrl = url;
            }
        }

        // MongoDB에는 저장된 파일 경로만 저장
        CardNews cardNews = CardNews.builder()
                .images(images)  // URL 접근 가능하도록 경로 저장
                .title(title)
                .thumbnail(thumbnailUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        cardNewsRepository.save(cardNews);
    }
}
