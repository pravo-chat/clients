package ru.pravochat.clients.di

import org.koin.dsl.module
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.analytics.YandexMetrikaTracker
import ru.pravochat.clients.repo.RepoMain
import ru.pravochat.clients.repo.StaticRepoMain

val appModule = module {
    single<AnalyticsTracker> { YandexMetrikaTracker() }
    single<RepoMain> { StaticRepoMain() }
}

