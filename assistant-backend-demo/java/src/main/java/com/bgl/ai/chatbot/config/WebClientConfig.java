package com.bgl.ai.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient aiAssistantWebClient(IntegrationConfig integrationConfig) {
        return WebClient.builder()
                .baseUrl(integrationConfig.getSmartDocsAiAssistantUrl())
                .build();
    }
}

