package com.namgrengaw.template.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Template")
                        .version("v1")
                        .description("Spring Template")
                        .termsOfService("https://namgrengaw.com/")
                        .license(new License()
                                .name("No License")
                                .url("https://namgrengaw.com/")
                        )
                );
    }

}
