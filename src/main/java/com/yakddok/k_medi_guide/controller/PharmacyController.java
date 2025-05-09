package com.yakddok.k_medi_guide.controller;

import com.yakddok.k_medi_guide.dto.PharmacyDTO;
import com.yakddok.k_medi_guide.service.impl.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping
    public String showPharmaciesPage(Model model) {
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "pharmacies";
    }

    @GetMapping("/nearby")
    @ResponseBody
    public List<PharmacyDTO> getNearbyPharmacies(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        return pharmacyService.searchNearbyPharmacies(latitude, longitude);
    }
}