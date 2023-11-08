package com.example.tutorialscrud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${tutorialscrud.dev-url}")
    private String devUrl;

    @Value("${tutorialscrud.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myswagger(){
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in development environment");
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in production environment");

        Contact mycontact = new Contact();
        mycontact.setEmail("lucsamds@gmail.com");
        mycontact.setName("Lucas Silva");
        mycontact.setUrl("https://www.linkedin.com/in/lucas-silva-322a42211/");

        License licenseMIT = new License();
        licenseMIT.name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info apiInfo = new Info();
        apiInfo.title("Tutorials CRUD with Spring Boot 3");
        apiInfo.version("1.0");
        apiInfo.contact(mycontact);
        apiInfo.description("An API that exposes endpoints to manage tutorials.");
        apiInfo.license(licenseMIT);

        return new OpenAPI().info(apiInfo).servers(List.of(devServer, prodServer));
    }
}
