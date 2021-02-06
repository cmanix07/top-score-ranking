package com.cmani.oyo.topscoreranking.e2e.controller.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Local swagger configuration.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
              .select()
              .apis(RequestHandlerSelectors.basePackage("com.cmani.oyo.topscoreranking"))
              .paths(PathSelectors.regex("/.*"))
              .build().apiInfo(metaData()).pathMapping("");
    }


  private ApiInfo metaData() {
    return new ApiInfoBuilder().title("Top-Score-Ranking REST API")
      .description("TOP SCORE RANKING GAME REST API DETAILS")
      .contact(new Contact("chintamani", "", "cmani.x07@gmail.com"))
      .version("1.0.0")
      .build();
  }


}
