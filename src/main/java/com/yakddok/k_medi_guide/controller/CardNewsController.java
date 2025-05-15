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
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardNewsController {

    private final CardNewsServiceImpl cardNewsService;

    // 카드 뉴스 작성
    @PostMapping("/posts")
    public ResponseEntity<String> uploadCardNews(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("title") String title) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("최소 하나 이상의 이미지를 업로드해야 합니다.");
        }

        try {
            cardNewsService.saveCardNews(files, title);
            return ResponseEntity.ok("카드뉴스가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 저장 중 오류가 발생했습니다.");
        }
    }

    // 카드 뉴스 조회
    @GetMapping("/posts/{id}")
    @Operation(summary = "Get post", description = "카드뉴스 조회 api 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "4xx", description = "실패")
    })
    @Parameters({
            @Parameter(name = "id", description = "아이디"),
            @Parameter(name = "model", description = "카드뉴스 모델")
    })
    public String getCardNews(@PathVariable String id, Model model) {
        CardNewsDTO cardNewsDTO = cardNewsService.getCardNewsById(id);
        model.addAttribute("cardNews", cardNewsDTO);
        return "cardNewsTest";
    }


    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deleteCardNews(@PathVariable String id) {
        cardNewsService.deleteCardNews(id);
        return ResponseEntity.ok("카드 뉴스가 삭제되었습니다.");
    }
}
