package com.yakddok.k_medi_guide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

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

    private List<Medicine> medicineList = Arrays.asList(
            new Medicine(1, "Aspirin", "Used for pain relief"),
            new Medicine(2, "Paracetamol", "Used to treat fever"),
            new Medicine(3, "Ibuprofen", "Anti-inflammatory drug")
    );


    @GetMapping("/drugSearch")
    public String drugSearch(Model model) {
        model.addAttribute("medicineList", medicineList);
        return "drugSearch";
    }

    @GetMapping("/drugSpecify/{id}")
    public String drugSpecify(@PathVariable("id") int id, Model model) {
        Medicine medicine = medicineList.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);

        model.addAttribute("medicine", medicine);
        return "drugSpecify";
    }
}
