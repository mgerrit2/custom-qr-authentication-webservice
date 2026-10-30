package com.qr.custom_qr_authentication.config.twoway.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Primary
    @Bean
    public OpenAPI customOpenAPI(){
            return new OpenAPI()
                    .info(new Info().title("QR authentication API").description("Custom QR Authentication API")
                            .version("1.0")
                            .description("""
                                    API documentation for the Spring Boot security aplication.
                                    Specifications:
                                    java version: java 25
                                    """))
                    .addSecurityItem(new SecurityRequirement()
                            .addList("JWT_Auth")
                    )
                    .components(new Components()
                            // Documenting your internal/fallback JWT mechanism
                            .addSecuritySchemes("JWT_Auth", new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Provide your application-issued bearer token.")))
                    // Enforced with absolute package paths to prevent "Server is abstract" collisions
                    .servers(List.of(
                            new io.swagger.v3.oas.models.servers.Server()
                                    .url("http://172.22.133.5:8092")
                                    .description("WSL localhost")
                    ));
    }
    @Bean
    public GroupedOpenApi publicApi() {
        OpenApiCustomizer additionalPropsFalse = openApi -> {
            Components components = openApi.getComponents();
            if (components != null && components.getSchemas() != null) {
                // Stream directly from the map values to avoid variable assignment errors
                components.getSchemas().values().forEach(schema -> {
                    if (schema != null && schema.getAdditionalProperties() == null) {
                        schema.setAdditionalProperties(false);
                    }
                });
            }
        };

        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(additionalPropsFalse)
                .build();
    }

}
