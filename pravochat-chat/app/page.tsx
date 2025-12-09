"use client";

import { FormEvent, useEffect, useRef, useState } from "react";

// Force rebuild

type ChatMessage = {
  id: string;
  role: "user" | "assistant";
  content: string;
};

const SESSION_STORAGE_KEY = "pravochat_chat_session_id";

export default function Home() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [sessionId, setSessionId] = useState<string | null>(null);

  const chatContainerRef = useRef<HTMLDivElement | null>(null);

  // Инициализируем и сохраняем sessionId один раз на клиента
  useEffect(() => {
    try {
      const existing =
        typeof window !== "undefined"
          ? window.localStorage.getItem(SESSION_STORAGE_KEY)
          : null;
      if (existing) {
        setSessionId(existing);
        return;
      }

      const fresh = `session_${Date.now()}_${Math.random()
        .toString(36)
        .slice(2, 8)}`;
      if (typeof window !== "undefined") {
        window.localStorage.setItem(SESSION_STORAGE_KEY, fresh);
      }
      setSessionId(fresh);
    } catch (e) {
      // В случае ошибок с localStorage просто создаём in‑memory ID
      const fallback = `session_${Date.now()}`;
      setSessionId(fallback);
    }
  }, []);

  // Авто‑скролл при появлении новых сообщений
  useEffect(() => {
    const el = chatContainerRef.current;
    if (!el) return;

    // Мягкий скролл к самому низу
    el.scrollTop = el.scrollHeight;
  }, [messages.length]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!input.trim() || isLoading) return;
    if (!sessionId) {
      // Теоретически не должно случаться, но на всякий случай
      console.warn("[Chat UI] sessionId is not ready yet");
      return;
    }

    const userMessage: ChatMessage = {
      id: `user_${Date.now()}`,
      role: "user",
      content: input.trim(),
    };

    const nextMessages = [...messages, userMessage];
    setMessages(nextMessages);
    setInput("");
    setIsLoading(true);
    setError(null);

    try {
      const res = await fetch("/api/chat", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          messages: nextMessages,
          sessionId,
        }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`HTTP ${res.status}: ${text}`);
      }

      const data = await res.json();
      const assistant = data?.messages?.[0] as ChatMessage | undefined;

      if (assistant?.content) {
        setMessages([...nextMessages, assistant]);
      } else {
        throw new Error("Пустой ответ от сервера");
      }
    } catch (err: any) {
      console.error("[Chat UI] Error:", err);
      setError(err.message || "Ошибка запроса");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        fontFamily:
          "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif",
        background: "#F9F9FA",
      }}
    >
      <main
        ref={chatContainerRef}
        style={{
          flex: 1,
          padding: "16px",
          overflowY: "auto",
          background: "#F9F9FA",
          display: "flex",
          flexDirection: "column",
          gap: "12px",
        }}
      >
        {messages.length === 0 && (
          <div
            style={{
              textAlign: "center",
              color: "#000000",
              fontSize: "28px",
              marginTop: "40px",
              fontWeight: "500",
            }}
          >
            <p>Задайте любой юридический вопрос по российскому праву.</p>
          </div>
        )}

        {messages.map((m) => (
          <div
            key={m.id}
            style={{
              display: "flex",
              justifyContent: m.role === "user" ? "flex-end" : "flex-start",
            }}
          >
            <div
              style={{
                maxWidth: "75%",
                padding: "10px 14px",
                borderRadius: "12px",
                background: m.role === "user" ? "#308CEF" : "#FFFFFF",
                color: m.role === "user" ? "#FFFFFF" : "#111322",
                fontSize: "14px",
                lineHeight: "1.5",
                boxShadow:
                  m.role === "user"
                    ? "0 2px 6px rgba(48,140,239,0.25)"
                    : "0 1px 3px rgba(15,23,42,0.12)",
                whiteSpace: "pre-wrap",
                wordBreak: "break-word",
              }}
            >
              {m.content}
            </div>
          </div>
        ))}

        {isLoading && (
          <div
            style={{
              display: "flex",
              justifyContent: "flex-start",
            }}
          >
            <div
              style={{
                padding: "10px 14px",
                borderRadius: "12px",
                background: "#FFFFFF",
                color: "#666",
                fontSize: "14px",
              }}
            >
              Печатает...
            </div>
          </div>
        )}
        
        {error && (
          <div
            style={{
              display: "flex",
              justifyContent: "flex-start",
            }}
          >
            <div
              style={{
                padding: "10px 14px",
                borderRadius: "12px",
                background: "#FFEBEE",
                color: "#C62828",
                fontSize: "14px",
              }}
            >
              Ошибка: {error}
            </div>
          </div>
        )}
      </main>

      <form
        onSubmit={handleSubmit}
        style={{
          padding: "12px 16px",
          display: "flex",
          gap: "8px",
          background: "#F9F9FA",
        }}
      >
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Опишите вашу ситуацию..."
          disabled={isLoading}
          style={{
            flex: 1,
            padding: "10px 14px",
            borderRadius: "999px",
            border: "1px solid #D9D9DC",
            fontSize: "14px",
            outline: "none",
            fontFamily: "inherit",
            background: "#FFFFFF",
          }}
          onFocus={(e) => {
            e.target.style.borderColor = "#308CEF";
          }}
          onBlur={(e) => {
            e.target.style.borderColor = "#D9D9DC";
          }}
        />
        <button
          type="submit"
          disabled={isLoading || !input.trim()}
          style={{
            padding: "10px 20px",
            borderRadius: "999px",
            border: "none",
            background:
              isLoading || !input.trim() ? "#A3C7F6" : "#308CEF",
            color: "#FFFFFF",
            fontSize: "14px",
            fontWeight: 500,
            cursor: isLoading || !input.trim() ? "default" : "pointer",
            transition: "opacity 150ms",
          }}
          onMouseEnter={(e) => {
            if (!isLoading && input.trim()) {
              e.currentTarget.style.opacity = "0.9";
            }
          }}
          onMouseLeave={(e) => {
            e.currentTarget.style.opacity = "1";
          }}
        >
          Отправить
        </button>
      </form>
    </div>
  );
}

