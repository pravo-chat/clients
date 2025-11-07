package ru.pravochat.clients.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.dsl.module
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.analytics.YandexMetrikaTracker
import ru.pravochat.clients.repo.RepoMain
import ru.pravochat.clients.repo.StaticRepoMain
import ru.pravochat.clients.repo.StaticTitleRepo
import ru.pravochat.clients.repo.TitleRepo
import ru.pravochat.clients.states.ButtonState
import ru.pravochat.clients.states.ButtonStateImpl

val appModule = module {
    single<CoroutineScope> { MainScope() }
    single<AnalyticsTracker> { YandexMetrikaTracker() }
    single<RepoMain> { StaticRepoMain(get()) }
    single<TitleRepo> { StaticTitleRepo() }
    single<ButtonState> { ButtonStateImpl(get()) }
}

