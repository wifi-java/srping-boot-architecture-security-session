package com.example.base.common.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


  @Bean
  public OpenAPI api() {
    Info info = new Info()
            .title("Swagger")
            .version("1.0.0");

    SecurityScheme auth = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.COOKIE)
            .name("JSESSIONID");

    SecurityRequirement securityRequirement = new SecurityRequirement().addList("basicAuth");

    return new OpenAPI()
            .components(new Components().addSecuritySchemes("basicAuth", auth))
            .addSecurityItem(securityRequirement)
            .info(info);
  }
}
