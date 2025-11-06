package ru.pravochat.clients.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.context.GlobalContext

@Composable
inline fun <reified T> koinInject(): T {
    return remember { GlobalContext.get().get() }
}

