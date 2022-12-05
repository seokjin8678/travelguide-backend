package com.seokjin.travelguide.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@PropertySource("classpath:ip.properties")
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${frontend-ip-address}") String ipAddress) {
        return request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of(ipAddress));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setMaxAge(3600L);
            configuration.setAllowCredentials(true);
            return configuration;
        };
    }
}
