package ru.pravochat.clients.repo

import kotlinx.coroutines.flow.Flow

interface RepoMain {
    val messages: Flow<Message>
}

data class Message(val text: String)
