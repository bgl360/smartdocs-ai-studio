package com.bgl.ai.chatbot.controller;

import com.bgl.ai.chatbot.config.IntegrationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.webflux.ProxyExchange;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/entities/{entityId}/ai-assistant")
public class AiChatBotController implements AiAssistantHeaderHelper {

    @Autowired
    private IntegrationConfig integrationConfig;

    @Autowired
    private WebClient aiAssistantWebClient;

    @RequestMapping("/{*segment}")
    public Mono<ResponseEntity<byte[]>> proxyAiAssistantRequest(
            @PathVariable String entityId,
            @PathVariable String segment,
            @RequestParam Map<String, String> params,
            ProxyExchange<byte[]> proxy
    ) {
        String path = integrationConfig.getSmartDocsAiAssistantUrl() + segment;
        return proxyRequest(path, params, proxy, entityId);
    }

    @GetMapping("/{context}/threads/{threadId}/events")
    public Flux<ServerSentEvent<String>> proxyStreamingEventsRequest(
            @PathVariable String entityId,
            @PathVariable String context,
            @PathVariable String threadId,
            @RequestParam Map<String, String> params
    ) {
        String path = "/" + context + "/threads/" + threadId + "/events";
        return proxyStreamingRequest(path, params, entityId);
    }

    @GetMapping("/{context}/threads/stream/{threadId}/runs")
    public Flux<ServerSentEvent<String>> proxyStreamingRunRequest(
            @PathVariable String entityId,
            @PathVariable String context,
            @PathVariable String threadId,
            @RequestParam Map<String, String> params
    ) {
        String path = "/" + context + "/threads/stream/" + threadId + "/runs";
        return proxyStreamingRequest(path, params, entityId);
    }

    private Mono<ResponseEntity<byte[]>> proxyRequest(String path, Map<String, String> params, ProxyExchange<byte[]> proxy, String entityId) {
        String uri = queryParamsURI(entityId, path, params);
        String firmId = "demo-firm-id"; // Replace with actual firm ID retrieval logic
        String userName = "demo-user"; // Replace with actual user name retrieval logic

        return proxy.uri(uri)
                .header(API_KEY, integrationConfig.getSmartDocsAiAssistantApiKey())
                .header(USERNAME, userName)
                .header(FIRM_ID, firmId)
                .header(SOURCE, SOURCE_NAME)
                .header(ENTITY_ID, entityId)
                .forward();
    }

    private Flux<ServerSentEvent<String>> proxyStreamingRequest(String path, Map<String, String> params, String entityId) {
        String url = queryParamsURI(entityId, path, params);
        String firmId = "demo-firm-id"; // Replace with actual firm ID retrieval logic
        String userName = "demo-user"; // Replace with actual user name retrieval logic

        return aiAssistantWebClient.get()
                .uri(url)
                .header(API_KEY, integrationConfig.getSmartDocsAiAssistantApiKey())
                .header(USERNAME, userName)
                .header(FIRM_ID, firmId)
                .header(SOURCE, SOURCE_NAME)
                .header(ENTITY_ID, entityId)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .onErrorResume(ex -> Flux.empty());
    }

    private String queryParamsURI(String entityId, String segment, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(segment)
                .queryParam("entityId", entityId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        return builder.build().toUriString();
    }
}
