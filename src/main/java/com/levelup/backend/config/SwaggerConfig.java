package com.levelup.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LevelUp Gamer Backend API")
                        .version("1.0.0")
                        .description("API REST monolítica para aplicación de e-commerce gaming")
                        .contact(new Contact()
                                .name("LevelUp Gamer Team")
                                .email("contact@levelupgamer.com")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresar el token JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
