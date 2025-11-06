package ru.pravochat.clients.di

import org.koin.dsl.module
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.analytics.YandexMetrikaTracker

val appModule = module {
    single<AnalyticsTracker> { YandexMetrikaTracker() }
}

