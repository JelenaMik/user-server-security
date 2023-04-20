package com.example.security;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

    @Configuration
    public class RestTemplateBuilderConfig {

        @Bean
        RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer){


            RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
            DefaultUriBuilderFactory uriBuilderFactory = new
                    DefaultUriBuilderFactory();

            return builder.uriTemplateHandler(uriBuilderFactory);
        }

        @Bean
        public RestTemplate getRestTemplate(){
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            RestTemplate restTemplate = restTemplateBuilder.build();
            return restTemplate;
        }

    }

