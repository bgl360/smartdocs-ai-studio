package com.bgl.chatbot.config

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
  @Bean
  def aiAssistantWebClient: WebClient = WebClient.builder().build()

}
