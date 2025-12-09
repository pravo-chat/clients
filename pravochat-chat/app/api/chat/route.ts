import OpenAI from "openai";

export const runtime = "nodejs";

// CORS headers для iframe
const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
  "Access-Control-Allow-Headers": "Content-Type",
};

export async function OPTIONS() {
  return new Response(null, {
    status: 200,
    headers: corsHeaders,
  });
}

export async function POST(req: Request) {
  console.log("[POST] Received request");
  
  let requestBody;
  try {
    requestBody = await req.json();
    console.log("[POST] Request body:", JSON.stringify(requestBody).substring(0, 200));
  } catch (e) {
    console.error("[POST] Failed to parse request body:", e);
    return new Response(
      JSON.stringify({ error: "Invalid request body" }),
      { status: 400, headers: { "Content-Type": "application/json" } }
    );
  }

  const { messages, sessionId: clientSessionId } = requestBody;
  console.log("[POST] Messages count:", messages?.length);

  // Используем sessionId от клиента или генерируем новый, если его нет
  const sessionId =
    typeof clientSessionId === "string" && clientSessionId.trim().length > 0
      ? clientSessionId
      : `session_${Date.now()}`;
  console.log("[POST] Session ID:", sessionId);
  console.log("[POST] Raw messages payload snippet:", JSON.stringify(messages).substring(0, 300));

  try {
    const apiKey = process.env.OPENAI_API_KEY;
    if (!apiKey) {
      console.error("[POST] Missing OPENAI_API_KEY");
      return new Response(
        JSON.stringify({
          error:
            "Сервер не настроен: отсутствует OPENAI_API_KEY. Обратитесь к администратору.",
        }),
        {
          status: 500,
          headers: {
            "Content-Type": "application/json",
            ...corsHeaders,
          },
        }
      );
    }

    const openai = new OpenAI({
      apiKey,
      timeout: 40_000,
    });

    if (!Array.isArray(messages) || messages.length === 0) {
      return new Response(
        JSON.stringify({ error: "Messages array is empty" }),
        { status: 400, headers: { "Content-Type": "application/json" } }
      );
    }

    // Формируем историю для OpenAI: системный промпт + вся переписка
    const systemPrompt =
      "Ты ИИ-юрист PravoChat. Отвечай на вопросы по российскому праву простым, понятным языком. " +
      "Если вопрос не про право или данных не хватает, честно об этом говори и уточняй детали. " +
      "Отвечай по-русски, структурировано и по сути, избегая лишней болтовни. " +
      "У тебя всегда есть доступ ко ВСЕЙ истории диалога в этом чате. " +
      "Если пользователь просит напомнить, о чём он писал раньше, обязательно используй предыдущие сообщения и кратко перескажи их содержание. " +
      "Никогда не отвечай, что ты не можешь вспомнить или увидеть прошлые сообщения — они уже перед тобой в истории диалога.";

    const openAiMessages: any[] = [
      { role: "system", content: systemPrompt },
      ...messages
        .filter((m: any) => m && typeof m.content === "string")
        .map((m: any) => ({
          role: (m.role === "assistant" ? "assistant" : "user") as
            | "assistant"
            | "user",
          content: m.content as string,
        })),
    ];

    const model = process.env.OPENAI_MODEL || "gpt-4.1-mini";

    const completion = await openai.chat.completions.create({
      model,
      messages: openAiMessages,
      temperature: 0.2,
      max_tokens: 800,
    });

    const content =
      completion.choices[0]?.message?.content ??
      "Извините, не удалось получить ответ от модели.";

    const assistantMessage = {
      id: `assistant_${Date.now()}`,
      role: "assistant",
      content,
    };

    console.log("[POST] Returning JSON response with assistant message from OpenAI");

    return new Response(JSON.stringify({ messages: [assistantMessage] }), {
      status: 200,
      headers: {
        "Content-Type": "application/json",
        ...corsHeaders,
      },
    });
  } catch (error: any) {
    console.error("[POST] Error:", error);
    console.error("[POST] Error stack:", error.stack);

    const message =
      (error && typeof error.message === "string" && error.message) ||
      "Internal server error";

    return new Response(JSON.stringify({ error: message }), {
      status:
        (error as any)?.statusCode && Number.isInteger((error as any).statusCode)
          ? (error as any).statusCode
          : 500,
      headers: {
        "Content-Type": "application/json",
        ...corsHeaders,
      },
    });
  }
}

