package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.CardNewsDTO;
import com.yakddok.k_medi_guide.service.impl.CardNewsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardNewsController {

    private final CardNewsServiceImpl cardNewsService;

    // 카드 뉴스 작성
    @PostMapping("/cardNews")
    public ResponseEntity<String> uploadCardNews(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("최소 하나 이상의 이미지를 업로드해야 합니다.");
        }

        try {
            cardNewsService.saveCardNews(files, title, author, description);
            return ResponseEntity.ok("카드뉴스가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 저장 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/cardNews")
    public String getCardNewsList(Model model) {
        // 저장된 모든 카드뉴스 가져오기
        List<CardNewsDTO> cardNewsList = cardNewsService.getAllCardNews();
        model.addAttribute("cardNewsList", cardNewsList);
        return "cardNewsList";
    }

    @GetMapping("/cardNews/{id}")
    public String getCardNewsDetail(@PathVariable String id, Model model) {
        CardNewsDTO cardNews = cardNewsService.getCardNewsById(id);
        model.addAttribute("cardNews", cardNews);
        return "cardNewsDetail"; // 상세 페이지 템플릿 (templates/cardnews/detail.html)
    }


    @DeleteMapping("/cardNews/{id}")
    public ResponseEntity<String> deleteCardNews(@PathVariable String id) {
        cardNewsService.deleteCardNews(id);
        return ResponseEntity.ok("카드 뉴스가 삭제되었습니다.");
    }
}
