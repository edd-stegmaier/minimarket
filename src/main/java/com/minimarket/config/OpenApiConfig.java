package com.minimarket.config;

import com.minimarket.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;

import io.swagger.v3.oas.annotations.security.SecurityScheme;

import java.util.Set;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {

    private static final Set<String> ERROR_RESPONSE_CODES = Set.of("400", "401", "403", "404", "500");

    @Bean
    public OpenApiCustomizer protectedOperationsErrorCustomizer() {
        return openApi -> {
            ensureErrorSchema(openApi);

            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().values().forEach(pathItem ->
                    pathItem.readOperations().forEach(operation -> {
                        if (operation.getResponses() == null) {
                            return;
                        }

                        if (operation.getSecurity() != null && !operation.getSecurity().isEmpty()) {
                            operation.getResponses().putIfAbsent("401",
                                    new ApiResponse().description("Autenticacion requerida").content(errorContent(openApi)));
                            operation.getResponses().putIfAbsent("403",
                                    new ApiResponse().description("Acceso denegado").content(errorContent(openApi)));
                        }

                        ERROR_RESPONSE_CODES.forEach(responseCode -> {
                            ApiResponse response = operation.getResponses().get(responseCode);
                            if (response != null) {
                                response.setContent(errorContent(openApi));
                            }
                        });
                    })
            );
        };
    }

    private void ensureErrorSchema(OpenAPI openApi) {
        Components components = openApi.getComponents();
        if (components == null) {
            components = new Components();
            openApi.setComponents(components);
        }

        if (components.getSchemas() == null || !components.getSchemas().containsKey(ApiErrorResponse.class.getSimpleName())) {
            components.addSchemas(ApiErrorResponse.class.getSimpleName(), buildApiErrorResponseSchema());
        }
    }

    private Schema<?> buildApiErrorResponseSchema() {
        return new ObjectSchema()
                .addProperty("timestamp", new DateTimeSchema().example("2026-07-18T18:30:00Z"))
                .addProperty("status", new IntegerSchema().example(401))
                .addProperty("error", new StringSchema().example("Unauthorized"))
                .addProperty("message", new StringSchema().example("Autenticacion requerida"))
                .addProperty("path", new StringSchema().example("/api/productos/1"))
                .addProperty("validationErrors", new MapSchema().additionalProperties(new StringSchema()).example(null));
    }

    private Content errorContent(OpenAPI openApi) {
        ensureErrorSchema(openApi);

        return new Content().addMediaType(
                org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse"))
        );
    }

}
