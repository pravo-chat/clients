package ru.pravochat.clients.di

import org.koin.dsl.module
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.analytics.YandexMetrikaTracker
import ru.pravochat.clients.repo.RepoMain
import ru.pravochat.clients.repo.StaticRepoMain
import ru.pravochat.clients.repo.StaticTitleRepo
import ru.pravochat.clients.repo.TitleRepo

val appModule = module {
    single<AnalyticsTracker> { YandexMetrikaTracker() }
    single<RepoMain> { StaticRepoMain() }
    single<TitleRepo> { StaticTitleRepo() }
}

