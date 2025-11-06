package ru.pravochat.clients.analytics

import kotlinx.browser.window
import kotlin.js.json

interface AnalyticsTracker {
    fun send(eventName: String, text: String)
}

class YandexMetrikaTracker : AnalyticsTracker {
    override fun send(eventName: String, text: String) {
        val ym = window.asDynamic().ym
        val counterId = 104954778

        if (ym != null) {
            val params = json(
                "chat_input_text" to text,
                "chat_event" to eventName
            )
            ym(counterId, "params", params)
            console.log("Yandex Metrika params sent:", eventName, "text length:", text.length)
        } else {
            console.log("Yandex Metrika not loaded - ym function is null")
        }
    }
}

