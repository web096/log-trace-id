package com.trace.id.demo.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Value("${springdoc.swagger-ui.server}")
    private String serverUrl;

    @Value("${springdoc.swagger-ui.description}")
    private String description;

    // API 보안 설정
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl))
                .info(info())
                ;
    }

    public Info info() {
        return new Info()
                .title("Log Trace Id Demo API")
                .description("Log Trace Id Demo API")
                .version("v1");
    }
}
