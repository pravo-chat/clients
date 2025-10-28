package ru.pravochat.clients

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform