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
@RequestMapping("/ai-assistant")
public class AiChatBotController implements AiAssistantHeaderHelper {

    @Autowired
    private IntegrationConfig integrationConfig;

    @Autowired
    private WebClient aiAssistantWebClient;

    @RequestMapping("/{*segment}")
    public Mono<ResponseEntity<byte[]>> proxyAiAssistantRequest(
            @PathVariable String segment,
            @RequestParam Map<String, String> params,
            ProxyExchange<byte[]> proxy
    ) {
        String path = integrationConfig.getSmartDocsAiAssistantUrl() + "/ai-assistant" + segment;
        return proxyRequest(path, params, proxy);
    }

    @GetMapping("/{context}/threads/{threadId}/events")
    public Flux<ServerSentEvent<String>> proxyStreamingEventsRequest(
            @PathVariable String context,
            @PathVariable String threadId,
            @RequestParam Map<String, String> params
    ) {
        String path = "/ai-assistant/" + context + "/threads/" + threadId + "/events";
        return proxyStreamingRequest(path, params);
    }

    @GetMapping("/{context}/threads/stream/{threadId}/runs")
    public Flux<ServerSentEvent<String>> proxyStreamingRunRequest(
            @PathVariable String context,
            @PathVariable String threadId,
            @RequestParam Map<String, String> params
    ) {
        String path = "/ai-assistant/" + context + "/threads/stream/" + threadId + "/runs";
        return proxyStreamingRequest(path, params);
    }

    private Mono<ResponseEntity<byte[]>> proxyRequest(String path, Map<String, String> params, ProxyExchange<byte[]> proxy) {
        String uri = queryParamsURI(path, params);

        //API Key and third party user id are required headers for the request.
        String third_party_user_id = "demo-third-party-user-id"; // Replace with actual third party user ID retrieval logic
        //You can give other headers as per your requirement.
        //For example, firmId and userName can be retrieved from the session or security context.

        return proxy.uri(uri)
                .header(API_KEY, integrationConfig.getSmartDocsAiAssistantApiKey())
                .header(THIRD_PARTY_USER_ID,third_party_user_id)
                .forward();
    }

    private Flux<ServerSentEvent<String>> proxyStreamingRequest(String path, Map<String, String> params) {
        String url = queryParamsURI(path, params);

        //API Key and third party user id are required headers for the request.
        String third_party_user_id = "demo-third-party-user-id"; // Replace with actual third party user ID retrieval logic
        //You can give other headers as per your requirement.
        //For example, firmId and userName can be retrieved from the session or security context.

        return aiAssistantWebClient.get()
                .uri(url)
                .header(API_KEY, integrationConfig.getSmartDocsAiAssistantApiKey())
                .header(THIRD_PARTY_USER_ID,third_party_user_id)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {})
                .onErrorResume(ex -> Flux.empty());
    }

    private String queryParamsURI(String segment, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(segment);

        params.forEach((key, value) ->
                builder.queryParam(key, value != null ? URLEncoder.encode(value, StandardCharsets.UTF_8) : null)
        );

        return builder.build().toUriString();
    }
}
