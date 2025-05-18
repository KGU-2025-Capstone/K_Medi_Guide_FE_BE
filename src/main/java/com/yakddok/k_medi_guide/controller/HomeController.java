package com.yakddok.k_medi_guide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home"; // home.html을 보여줌
    }

    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }

    @GetMapping("/drugSearch")
    public String drugSearch(Model model) {
        return "drugSearch";
    }

    @GetMapping("/drugSpecify/{id}")
    public String drugSpecify(@PathVariable("id") int id, Model model) {
        return "drugSpecify";
    }
}
