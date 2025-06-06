# AI Chat Bot backend Integration

This repository provides an example **Scala + Spring Boot controller** that proxies requests to the AI ChatBot service.
This allows third-party systems to securely forward requests while adding required headers for authentication and context.

## 🚀 Purpose

The AI ChatBot backend expects specific headers to authenticate and identify users, entities, and access permissions.
To keep your API key secure and control request flow, **a backend proxy is required**.

---

## 📌 Requirements

- Scala + Spring Boot (WebFlux)
- Spring Security (to obtain authenticated user context)
- Spring Cloud Gateway or WebClient for HTTP forwarding

---

## 📡 Endpoints

### 1. Proxy API Requests

REQUEST /entities/{entityId}/ai-assistant/{*segment}

This endpoint proxies general AI assistant requests.

### 2. Stream Assistant Events

GET /entities/{entityId}/ai-assistant/{context}/threads/{threadId}/events

Streams Server-Sent Events (e.g., assistant messages).

### 3. Stream Assistant Run Output

GET /entities/{entityId}/ai-assistant/{context}/threads/stream/{threadId}/runs

Streams assistant run output in real-time.

---

## 🔐 Required Headers

The proxy controller injects the following headers before forwarding the request:

| Header Name                                              | Description                        |
|----------------------------------------------------------|------------------------------------|
| `x-api-key`                                              | API key (provided by us)           |
| `app-ai-assistant-function-call-user-name`              | User identifier                    |
| `app-ai-assistant-function-call-user-authorities`       | Comma-separated user roles         |
| `app-ai-assistant-function-call-firm-id`                | Firm ID or tenant identifier       |
| `app-ai-assistant-function-call-source`                 | Integration source identifier      |
| `app-ai-assistant-function-call-entity-id`              | Entity ID                          |

---

## ⚙️ How It Works

- `ProxyExchange` or `WebClient` is used to forward requests.
- Headers are added from the Spring Security `Authentication` object.
- Optional query parameters are appended to the target URL.

---



