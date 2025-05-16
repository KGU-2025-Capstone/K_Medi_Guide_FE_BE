package com.yakddok.k_medi_guide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PharmacyController {

    @GetMapping("/pharmacies")
    public String showPharmacyTestPage(Model model) {
        return "pharmacies";
    }

}