package com.yakddok.k_medi_guide.controller;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Locale;

@RestController
public class LocaleController {

    @GetMapping("/api/current-locale")
    public String getCurrentLocale() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return currentLocale.getLanguage(); // "ko", "en", "zh" 등 반환
    }
}

