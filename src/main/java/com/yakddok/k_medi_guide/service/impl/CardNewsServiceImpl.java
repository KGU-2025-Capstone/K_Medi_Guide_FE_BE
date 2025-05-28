package com.yakddok.k_medi_guide.service.impl;

import com.yakddok.k_medi_guide.dto.CardNewsDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardNewsServiceImpl {

    private final CardNewsRepository cardNewsRepository;

    @Value("${file.upload-dir}") // application.properties 파일에서 경로 가져오기
    private String uploadDir;

    // 카드 뉴스 저장
    public void saveCardNews(List<MultipartFile> files, String title, String description, String author) throws IOException {
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
                .author(author)
                .description(description)
                .build();
        cardNewsRepository.save(cardNews);
    }

    // 카드 뉴스 조회
    public CardNewsDTO getCardNewsById(String id) {
        CardNews cardNews = cardNewsRepository.findById(id).orElseThrow();

        return new CardNewsDTO(cardNews);
    }

    public List<CardNewsDTO> getAllCardNews() {
        List<CardNews> cardNewsList = cardNewsRepository.findAll();
        return cardNewsList.stream()
                .map(CardNewsDTO::new)
                .collect(Collectors.toList());
    }

    // 카드 뉴스 삭제
    public void deleteCardNews(String id) {
        CardNews cardNews = cardNewsRepository.findById(id).orElseThrow();

        List<String> imagePaths = cardNews.getImages();

        // 각 이미지 파일 삭제
        for (String imagePath : imagePaths) {
            // 절대 경로 변환
            String absolutePath = Paths.get(uploadDir, new File(imagePath).getName()).toString();
            File file = new File(absolutePath);

            // 파일 삭제
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("🟢 파일 삭제 성공: " + absolutePath);
                } else {
                    System.out.println("🚨 파일 삭제 실패: " + absolutePath);
                }
            } else {
                System.out.println("❌ 파일이 존재하지 않음: " + absolutePath);
            }
        }

        cardNewsRepository.deleteById(id);
    }
}
