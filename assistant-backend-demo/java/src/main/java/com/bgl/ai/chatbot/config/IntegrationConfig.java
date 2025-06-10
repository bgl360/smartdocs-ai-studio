package com.bgl.ai.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "integration")
public class IntegrationConfig {

    private String smartDocsAiAssistantUrl;
    private String smartDocsAiAssistantApiKey;

    public String getSmartDocsAiAssistantUrl() {
        return smartDocsAiAssistantUrl;
    }

    public void setSmartDocsAiAssistantUrl(String smartDocsAiAssistantUrl) {
        this.smartDocsAiAssistantUrl = smartDocsAiAssistantUrl;
    }

    public String getSmartDocsAiAssistantApiKey() {
        return smartDocsAiAssistantApiKey;
    }

    public void setSmartDocsAiAssistantApiKey(String smartDocsAiAssistantApiKey) {
        this.smartDocsAiAssistantApiKey = smartDocsAiAssistantApiKey;
    }
}
