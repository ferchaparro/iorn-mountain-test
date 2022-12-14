package com.ironmountain.imtest.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("Origin",
                                "Accept",
                                "Content-Type",
                                "Access-Control-Request-Method",
                                "Access-Control-Request-Headers",
                                "Access-Control-Allow-Headers",
                                "Access-Control-Allow-Origin",
                                "x-requested-with",
                                "X-Custom-Header",
                                "Authorization")
                        .allowedMethods(
                                "POST",
                                "GET",
                                "PUT",
                                "DELETE")
                        .allowedOriginPatterns("*")
                        .maxAge(3600)
                        .allowCredentials(true);
            }
        };
    }
}
