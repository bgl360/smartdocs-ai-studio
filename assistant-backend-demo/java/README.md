# 🧠 AI Chatbot Gateway Proxy

This Spring Boot controller (`AiChatBotController`) serves as a **secure proxy gateway** that enables third-party systems 
to communicate with the internal **SmartDocs AI Assistant service**. It forwards requests, appends necessary headers, 
and supports real-time streaming via server-sent events.

## 📍 Base URL
SmartDocs AI Assistant Base URL should be set in the `application.yaml` file under the `smartdocs.ai.assistant.base-url` property.
This URL is used to construct the endpoint for the AI Assistant service.

---

## 🔄 Endpoints

### 1. 📦 Generic Proxy Endpoint

**Route:**
/ai-assistant/{segment}


**Purpose:**
Proxies REST API requests to the AI Assistant backend.

**Example:**
GET /ai-assistant/assistants/

**Notes:**
- `{segment}` will be appended to the configured backend URL (`smartdocs.ai-assistant.base-url`).
---

### 2. 🔁 Streaming Events from a Thread

**Route:**
GET /ai-assistant/{context}/threads/{threadId}/events


**Purpose:**
Streams real-time events (e.g., assistant messages) from a conversation thread.

**Returns:**
`text/event-stream` (`ServerSentEvent<String>`)
---

### 3. 🚀 Streaming Runs from a Thread

**Route:**
GET /ai-assistant/{context}/threads/stream/{threadId}/runs

**Purpose:**
Streams long-running assistant tasks (e.g., document generation or data analysis).

**Returns:**
`text/event-stream` (`ServerSentEvent<String>`)
---
## 🔐 Required Headers

Each request **must** include the following headers:

| Header Name              | Description                                                                 |
|--------------------------|-----------------------------------------------------------------------------|
| `X-API-KEY`              | API key provided by us to authenticate your requests                        |
| `X-ThirdParty-User-Id`   | A unique identifier for the user initiating the request. **Defines thread ownership**, so it must be globally unique per user to avoid conflicts. |

---

## 🧩 Optional Headers (Custom Context)

You may provide additional context by setting headers prefixed with `app-ai-assistant-function-call-`. 
These headers will be passed directly to the AI Assistant's function call logic.

| Header Name                                      | Description                                |
|--------------------------------------------------|--------------------------------------------|
| `app-ai-assistant-function-call-firm-id`         | Firm identifier or similar                 |
| `app-ai-assistant-function-call-source`          | Source system or integration name          |
| `app-ai-assistant-function-call-entity-id`       | Associated entity ID                       |
| *(Other `app-ai-assistant-function-call-*` headers)* | Define your own custom context headers   |

> ℹ️These optional headers are useful for injecting business-specific context.



## ⚙️ Configuration

Update your `application.yml` or `application.properties` with the backend service URL and API key:

```yaml
smartdocs:
  ai-assistant:
    url: https://your-internal-ai-service/api
    api-key: YOUR_INTERNAL_API_KEY

> ⚠️Ensure these values are wired into IntegrationConfig.


