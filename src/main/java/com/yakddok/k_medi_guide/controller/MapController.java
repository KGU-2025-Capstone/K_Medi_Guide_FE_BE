package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.response.ResponsePhamarcyDTO;
import com.yakddok.k_medi_guide.service.MapService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor

public class MapController {

    private final MapService mapService;

    @PostMapping(value = "/getPhamarcy")
    public ResponseEntity<ResponsePhamarcyDTO> getPhamarcy(
        @RequestParam("latitude") BigDecimal latitude,
        @RequestParam("longitude") BigDecimal longitude) {

        ResponsePhamarcyDTO respDTO = mapService.getPhamarcy(latitude, longitude);

        if(respDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respDTO);
    }
}
