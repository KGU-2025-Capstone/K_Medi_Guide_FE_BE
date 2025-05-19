package com.yakddok.k_medi_guide.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // 웹에서 /uploads/로 접근 가능
                .addResourceLocations("file:" + uploadPath);  // 로컬 폴더와 매핑
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {

            private static final Locale DEFAULT_LOCALE = Locale.KOREA;

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                // 1. lang 파라미터 우선
                String langParam = request.getParameter("lang");
                if (langParam != null && !langParam.isEmpty()) {
                    Locale locale = Locale.forLanguageTag(langParam);
                    request.getSession().setAttribute("LOCALE", locale);
                    return locale;
                }

                // 2. 세션에 저장된 언어
                Locale sessionLocale = (Locale) request.getSession().getAttribute("LOCALE");
                if (sessionLocale != null) {
                    return sessionLocale;
                }

                // 3. 브라우저 헤더
                String acceptLang = request.getHeader("Accept-Language");
                if (acceptLang != null && !acceptLang.isEmpty()) {
                    return Locale.forLanguageTag(acceptLang.split(",")[0]);
                }

                return DEFAULT_LOCALE;
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
                request.getSession().setAttribute("LOCALE", locale);
            }
        };
    }

}

