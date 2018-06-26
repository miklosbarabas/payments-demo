package org.miklosbarabas.demo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger API configuration file
 * Importing {@link SpringDataRestConfiguration} to be able to use with Spring DATA Rest
 *
 * @author Miklos Barabas
 */
@Configuration
@EnableSwagger2
@Import({SpringDataRestConfiguration.class})
@PropertySource(value = "classpath:swagger.properties")
public class SwaggerConfig {

    @Value("${api.title}")
    private String apiTitle;

    @Value("${api.description}")
    private String apiDescription;

    @Value("${api.tosUrl}")
    private String apiTosUrl;

    @Value("${api.contact.name}")
    private String apiContactName;

    @Value("${api.contact.email}")
    private String apiContactEmail;

    @Value("${api.contact.url}")
    private String apiContactUrl;

    @Value("${api.license}")
    private String apiLicense;

    @Value("${api.licenseUrl}")
    private String apiLicenseUrl;


    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(apiPaths()::test)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apiTitle)
                .description(apiDescription)
                .termsOfServiceUrl(apiTosUrl)
                .contact(new Contact(apiContactName, apiContactUrl, apiContactEmail))
                .license(apiLicense)
                .licenseUrl(apiLicenseUrl).build();
    }

    private Predicate<String> apiPaths() {
        return regex("/api/payment.*")::apply;
    }

}
