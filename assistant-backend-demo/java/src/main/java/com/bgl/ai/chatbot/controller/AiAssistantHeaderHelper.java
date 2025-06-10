package com.bgl.ai.chatbot.controller;

public interface AiAssistantHeaderHelper {
    //API Key and Third Party User ID headers are required for the request.
    String API_KEY = "X-API-KEY";
    String THIRD_PARTY_USER_ID = "X-ThirdParty-User-Id";

    // Headers used for AI Assistant function calls
    // Can define your own headers as per your requirement. Please start with "app-ai-assistant-function-call-"
    String USERNAME = "app-ai-assistant-function-call-user-name";
    String FIRM_ID = "app-ai-assistant-function-call-firm-id";
    String SOURCE = "app-ai-assistant-function-call-source";
    String SOURCE_NAME= "sourceName"; //replace with actual source name if needed, for example "sf360"

}
