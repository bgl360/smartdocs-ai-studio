package config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

@Component
@ConfigurationProperties(prefix = "integration")
class IntegrationConfig {
  @BeanProperty
  var smartdocsAiAssistantUrl: String = _

  @BeanProperty
  var smartdocsAiAssistantApiKey: String = _
}
