package com.yakddok.k_medi_guide.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PharmacyController {

    @Value("${google.map.client-secret}")
    private String googleMapApiKey;

    @GetMapping("/pharmacies")
    public String showPharmacyTestPage(Model model) {
        model.addAttribute("googleMapsApiKey", googleMapApiKey);
        return "pharmacies";
    }
}