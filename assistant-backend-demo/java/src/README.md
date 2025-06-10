# 🧠 AI Chatbot Gateway Proxy

This Spring Boot controller (`AiChatBotController`) serves as a **secure proxy gateway** that enables third-party systems 
to communicate with the internal **SmartDocs AI Assistant service**. It forwards requests, appends necessary headers, 
and supports real-time streaming via server-sent events.

## 📍 Base URL
SmartDocs AI Assistant Base URL should be set in the `application.yaml` file under the `smartdocs.ai.assistant.base-url` property.
This URL is used to construct the endpoint for the AI Assistant service.

/entities/{entityId}/ai-assistant

Replace `{entityId}` with the unique identifier for the entity or tenant making the request.

---

## 🔄 Endpoints

### 1. 📦 Generic Proxy Endpoint

**Route:**
/entities/{entityId}/ai-assistant/{segment}


**Purpose:**
Proxies REST API requests to the AI Assistant backend.

**Example:**
GET entities/1234/ai-assistant/assistants/

**Notes:**
- `{segment}` will be appended to the configured backend URL (`smartdocs.ai-assistant.base-url`).
---

### 2. 🔁 Streaming Events from a Thread

**Route:**
GET /entities/{entityId}/ai-assistant/{context}/threads/{threadId}/events


**Purpose:**
Streams real-time events (e.g., assistant messages) from a conversation thread.

**Returns:**
`text/event-stream` (`ServerSentEvent<String>`)
---

### 3. 🚀 Streaming Runs from a Thread

**Route:**
GET /entities/{entityId}/ai-assistant/{context}/threads/stream/{threadId}/runs

**Purpose:**
Streams long-running assistant tasks (e.g., document generation or data analysis).

**Returns:**
`text/event-stream` (`ServerSentEvent<String>`)
---

## 🔐 Headers Automatically Injected

The proxy layer will add these headers to requests forwarded to the AI Assistant backend:

| Header Name  | Description                          |
|--------------|--------------------------------------|
| `API_KEY`    | Internal API key from configuration  |
| `USERNAME`   | The username of the caller (currently hardcoded as `"demo-user"`) |
| `FIRM_ID`    | The firm ID (currently hardcoded as `"demo-firm-id"`) |
| `ENTITY_ID`  | The entity ID from the path variable |
| `SOURCE`     | Fixed value to identify request source (`SOURCE_NAME`) |

> ⚠️ Replace hardcoded `USERNAME` and `FIRM_ID` values with proper resolution logic for production.

---

## ⚙️ Configuration

Update your `application.yml` or `application.properties` with the backend service URL and API key:

```yaml
smartdocs:
  ai-assistant:
    url: https://your-internal-ai-service/api
    api-key: YOUR_INTERNAL_API_KEY

> ⚠️Ensure these values are wired into IntegrationConfig.


