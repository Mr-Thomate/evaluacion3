package Evaluacion3.MS1.config;

import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Evaluacion 3 Biblioteca MS1")
                        .version("0.5.0")
                        .description("Api de la tercera evauacion de nuestro proyecto de biblioteca con el primer microservicio"));
    }

}
