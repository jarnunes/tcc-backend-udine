package com.pucminas.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//@Configuration
public class CorsConfig {

   // @Bean todo: deixar desabilitado
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // Permite qualquer origem, ajuste conforme necessário
        config.addAllowedMethod("*");       // Permite todos os métodos HTTP
        config.addAllowedHeader("*");       // Permite todos os cabeçalhos
        config.setAllowCredentials(true);   // Permite credenciais (cookies, autenticação)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica a todas as rotas

        return new CorsFilter(source);
    }
}