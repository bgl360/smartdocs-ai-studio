package com.bgl.chatbot.controller


import com.bgl.chatbot.config.IntegrationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.webflux.ProxyExchange
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation._
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.{Flux, Mono}

import java.net.URLEncoder
import scala.jdk.CollectionConverters.MapHasAsScala

@RestController
@RequestMapping(value = Array("/entities/{entityId}/ai-assistant"))
class AiAssistantController(
  @Autowired integrationConfig: IntegrationConfig,
  @Autowired aiAssistantWebClient: WebClient,
) extends AiAssistantHeaderHelper {

  //proxy to ai-assistant service
  // web_base_url: integrationConfig.smartdocsAiAssistantUrl
  // api_key: integrationConfig.smartdocsAiAssistantApiKey

  @RequestMapping(value = Array("/{*segment}"))
  def proxyAiAssistantRequest(
    @PathVariable entityId: String,
    @PathVariable segment: String,
    @RequestParam params: java.util.Map[String, String],
    proxy: ProxyExchange[Array[Byte]],
  ): Mono[ResponseEntity[Array[Byte]]] = {
    // Implement the logic to proxy AI assistant request
    proxyRequest(
      path = s"${integrationConfig.smartdocsAiAssistantUrl}$segment",
      params = params,
      proxy = proxy,
      entityId = entityId
    )
  }

  @GetMapping(value = Array("/{context}/threads/{threadId}/events"))
  def proxyStreamingEventsRequest(
    @PathVariable entityId: String,
    @PathVariable context: String,
    @PathVariable threadId: String,
    @RequestParam params: java.util.Map[String, String],
  ): Flux[ServerSentEvent[String]] = {
    // Implement the logic to proxy streaming events request
    proxyStreamingRequest(
      path = s"/$context/threads/$threadId/events",
      params = params,
      entityId = entityId,
    )
  }

  @GetMapping(value = Array("/{context}/threads/stream/{threadId}/runs"))
  def proxyStreamingRunRequest(
    @PathVariable entityId: String,
    @PathVariable context: String,
    @PathVariable threadId: String,
    @RequestParam params: java.util.Map[String, String],
  ): Flux[ServerSentEvent[String]] = {
    // Implement the logic to proxy streaming run request
    proxyStreamingRequest(
      s"/$context/threads/stream/$threadId/runs",
      params,
      entityId,
    )
  }


  private def proxyRequest(
    path: String,
    params: java.util.Map[String, String],
    proxy: ProxyExchange[Array[Byte]],
    entityId: String
  ): Mono[ResponseEntity[Array[Byte]]] = {
    val firmId = "demo-firm-id" // Replace with actual firm ID retrieval logic
    val userName = "demo-user" // Replace with actual user name retrieval logic, from auth principal or similar

    val uri: String = queryParamsURI(entityId, path, params)
      proxy.uri(uri)
        .header(API_KEY, integrationConfig.smartdocsAiAssistantApiKey)
        .header(USERNAME, userName)
        .header(FIRM_ID, s"$firmId")
        .header(SOURCE, SOURCE_NAME)
        .header(ENTITY_ID, entityId)
        .forward()
    }

  private def proxyStreamingRequest(
    path: String,
    params: java.util.Map[String, String],
    entityId: String,
  ): Flux[ServerSentEvent[String]] = {
    val url: String = queryParamsURI(entityId, path, params)
    val firmId = "demo-firm-id" // Replace with actual firm ID retrieval logic
    val userName = "demo-user" // Replace with actual user name retrieval logic, from auth principal or similar

    aiAssistantWebClient.get()
      .uri(url)
      .header(API_KEY, integrationConfig.smartdocsAiAssistantApiKey)
      .header(USERNAME, userName)
      .header(FIRM_ID, firmId)
      .header(SOURCE, SOURCE_NAME)
      .header(ENTITY_ID, entityId)
      .retrieve()
      .bodyToFlux(new ParameterizedTypeReference[ServerSentEvent[String]]() {})
      .onErrorResume { ex: Throwable =>
        Flux.empty[ServerSentEvent[String]]
      }
  }

  private def queryParamsURI(entityId: String, segment: String, params: java.util.Map[String, String]): String = {
    val baseUri = UriComponentsBuilder.fromUriString(segment)
    params.asScala.toList.foldLeft(baseUri.queryParam("entityId", entityId))((uri, param) =>
      uri.queryParam(param._1, URLEncoder.encode(param._2, "UTF-8"))).build.toString
  }

}

trait AiAssistantHeaderHelper {
  // for all the headers, start with "app-ai-assistant-function-call-"
  final val USERNAME = "app-ai-assistant-function-call-user-name"
  final val USER_AUTHORITIES = "app-ai-assistant-function-call-user-authorities"
  final val FIRM_ID = "app-ai-assistant-function-call-firm-id"
  final val SOURCE = "app-ai-assistant-function-call-source"
  //replace with actual source name
  final val SOURCE_NAME = "sourceName"
  final val ENTITY_ID = "app-ai-assistant-function-call-entity-id"
  final val API_KEY = "x-api-key"
}

