package com.code065.alquilervehiculos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI alquilerVehiculosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Alquiler de Vehículos")
                        .version("1.0")
                        .description("Documentación de la API REST desarrollada en el Producto 4"));
    }
}
