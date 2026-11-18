package com.squad.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Permite qualquer origem (Live Server, etc)
        config.addAllowedHeader("*"); // Permite todos os headers (Content-Type, etc)
        config.addAllowedMethod("*"); // Permite GET, POST, PUT, DELETE, OPTIONS, etc
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}