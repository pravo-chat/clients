package ru.pravochat.clients

import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        App()
    }
}