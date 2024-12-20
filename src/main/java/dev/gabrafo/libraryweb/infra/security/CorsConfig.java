package dev.gabrafo.libraryweb.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**") // Configuração válida pra qualquer endpoint
                .allowedMethods("*")
                .maxAge(3600)
                .allowedOrigins("http://localhost:8080")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}