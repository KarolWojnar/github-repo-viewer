package com.task.atipera.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${github.token:empty}")
    private String token;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
        if (token != null && !token.isEmpty() && !token.equals("empty")) {
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().setBearerAuth(token);
                return execution.execute(request, body);
            });
        }
        return restTemplate;
    }

}
