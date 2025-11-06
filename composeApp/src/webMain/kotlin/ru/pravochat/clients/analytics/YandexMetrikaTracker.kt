package ru.pravochat.clients.analytics

import kotlinx.browser.window
import kotlin.js.json

object YandexMetrikaTracker {
    private const val COUNTER_ID = 104954778

    fun send(eventName: String, text: String) {
        val ym = window.asDynamic().ym

        if (ym != null) {
            val params = json(
                "chat_input_text" to text,
                "chat_event" to eventName
            )
            ym(COUNTER_ID, "params", params)
            console.log("Yandex Metrika params sent:", eventName, "text length:", text.length)
        } else {
            console.log("Yandex Metrika not loaded - ym function is null")
        }
    }
}

