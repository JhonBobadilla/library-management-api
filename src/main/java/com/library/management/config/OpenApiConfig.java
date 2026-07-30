package com.library.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gesti\u00f3n de Biblioteca")
                        .description("API REST para administrar usuarios, libros, ejemplares f\u00edsicos y pr\u00e9stamos de una biblioteca.")
                        .version("1.0.0"));
    }
}
