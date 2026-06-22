package evaluacion3.MS2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
            new Info()
            .title("API 2026 Evaluacion 3 Biblioteca MS2")
            .version("0.5.0")
            .description("Api de la tercera evaluacion de nuestro proyecto de biblioteca con el segundo microservicio")
        );
    }
}
