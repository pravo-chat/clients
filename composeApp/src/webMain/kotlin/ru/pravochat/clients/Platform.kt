package ru.pravochat.clients

interface Platform {
    val name: String
}

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

fun getPlatform(): Platform = JsPlatform()