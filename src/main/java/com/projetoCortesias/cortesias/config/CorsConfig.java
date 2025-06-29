package com.projetoCortesias.cortesias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*") // ou especifique: "http://seu-front.com"
//                .allowedMethods("*")
//                .allowedHeaders("*");
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Garanta que esse path está certo
//                        .allowedOrigins("http://localhost:8080")
                        .allowedOrigins("https://luizcapel.github.io/cortesias")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

//    @RestController
//    @RequestMapping("/teste")
//    public class TesteController {
//        @GetMapping
//        public ResponseEntity<String> testar() {
//            return ResponseEntity.ok("CORS liberado!");
//        }
//    }
}
