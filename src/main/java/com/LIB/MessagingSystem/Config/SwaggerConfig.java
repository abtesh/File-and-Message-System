package com.LIB.MessagingSystem.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI()
//                .servers(List.of(new Server()))
                .info(new Info().title("HR ICS Service").version("1.0"));
    }
}
