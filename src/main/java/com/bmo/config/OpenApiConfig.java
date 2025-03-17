package com.bmo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeNexusOpenAPI() {
        Server devServer = new Server()
            .url("http://localhost:8080")
            .description("Development server");

        Contact contact = new Contact()
            .name("Aarif Diwan")
            .email("aarif.diwan@gmail.com")
            .url("https://github.com/aarifdiwan-hub");

        License license = new License()
            .name("Apache License 2.0")
            .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
            .title("Employee Nexus API")
            .version("1.0.0")
            .description("REST API for employee management with optimistic locking for concurrent modifications")
            .contact(contact)
            .license(license);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer));
    }
}