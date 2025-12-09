# Добавление Streaming Endpoint на Backend

Для правильной работы с Vercel AI SDK нужно добавить на backend (Kotlin/Ktor) endpoint с поддержкой Server-Sent Events (SSE).

## Пример реализации для Ktor

Добавь в `src/main/kotlin/.../Main.kt` или в файл с роутингом:

```kotlin
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.coroutines.flow.*

// Новый endpoint для streaming
get("/openai/chat/stream") {
    val sessionId = call.request.queryParameters["id"] ?: return@get call.respond(
        HttpStatusCode.BadRequest,
        "Missing 'id' parameter"
    )
    val text = call.request.queryParameters["text"] ?: return@get call.respond(
        HttpStatusCode.BadRequest,
        "Missing 'text' parameter"
    )

    call.respondTextWriter(contentType = ContentType.Text.EventStream) {
        try {
            // Вызываешь OpenAI API с stream: true
            val openaiResponse = // ... твой код для вызова OpenAI с streaming
            
            // Парсишь SSE stream от OpenAI и проксируешь клиенту
            openaiResponse.body?.let { body ->
                body.byteStream().bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        if (line.startsWith("data: ")) {
                            val data = line.removePrefix("data: ").trim()
                            if (data != "[DONE]") {
                                // Парсишь JSON и извлекаешь delta.content
                                // Отправляешь клиенту в формате SSE
                                write("data: $data\n\n")
                                flush()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            write("data: {\"error\": \"${e.message}\"}\n\n")
            flush()
        }
    }
}
```

## Альтернативный вариант - простой streaming

Если не хочешь парсить OpenAI SSE, можно просто стримить готовый ответ:

```kotlin
get("/openai/chat/stream") {
    val sessionId = call.request.queryParameters["id"] ?: return@get call.respond(
        HttpStatusCode.BadRequest,
        "Missing 'id' parameter"
    )
    val text = call.request.queryParameters["text"] ?: return@get call.respond(
        HttpStatusCode.BadRequest,
        "Missing 'text' parameter"
    )

    // Получаешь полный ответ от OpenAI (как сейчас)
    val fullResponse = // ... твой код для получения ответа
    
    call.respondTextWriter(contentType = ContentType.Text.EventStream) {
        // Стримишь ответ по словам
        fullResponse.split(" ").forEachIndexed { index, word ->
            val chunk = if (index > 0) " $word" else word
            write("data: ${chunk}\n\n")
            flush()
            kotlinx.coroutines.delay(30) // небольшая задержка
        }
        write("data: [DONE]\n\n")
        flush()
    }
}
```

## После добавления endpoint

Измени в Next.js `app/api/chat/route.ts`:

```typescript
const url = `${BACKEND_URL}/openai/chat/stream?id=${encodeURIComponent(sessionId)}&text=${encodeURIComponent(lastUserMessage)}`;

const response = await fetch(url, {
  method: "GET",
  headers: {
    "Accept": "text/event-stream",
  },
});

// Проксируешь SSE stream напрямую
return new StreamingTextResponse(response.body);
```

Это решит проблему с парсингом, так как backend будет возвращать правильный SSE формат.





