package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponsePharmacyDTO;
import com.yakddok.k_medi_guide.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor

public class MapController {

    private final MapService mapService;

    @GetMapping(value = "/getPharmacy")
    @Operation(summary = "Get Pharmacy list", description = "인근 약국 정보를 가져오는 API 입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "4xx", description = "실패")
    })
    @Parameters({
        @Parameter(name = "latitude", description = "latitude 좌표 값", example = "37.23414"),
        @Parameter(name = "longitude", description = "longitude 좌표 값", example = "125.12343")
    })
    public ResponseEntity<ResponsePharmacyDTO> getPharmacy(
        @RequestParam("latitude") BigDecimal latitude,
        @RequestParam("longitude") BigDecimal longitude) {

        ResponsePharmacyDTO respDTO = mapService.getPharmacy(latitude, longitude);

        if(respDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respDTO);
    }
}
