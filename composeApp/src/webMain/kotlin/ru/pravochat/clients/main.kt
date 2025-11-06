package ru.pravochat.clients

import org.jetbrains.compose.web.renderComposable
import org.koin.core.context.startKoin
import ru.pravochat.clients.di.appModule

fun main() {
    startKoin {
        modules(appModule)
    }
    renderComposable(rootElementId = "root") {
        App()
    }
}