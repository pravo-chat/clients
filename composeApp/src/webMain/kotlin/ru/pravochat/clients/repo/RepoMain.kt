package ru.pravochat.clients.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RepoMain {
    fun introduction(): Flow<String>
}

class StaticRepoMain : RepoMain {
    override fun introduction(): Flow<String> = flowOf(INTRO_TEXT)

    companion object {
        const val INTRO_TEXT = "— это интеллектуальная система, основанная на искусственном интеллекте, которая помогает решать правовые вопросы, анализировать документы и давать точные рекомендации. Она работает круглосуточно, мгновенно обрабатывает запросы и упрощает работу юристов и предпринимателей.\n\nПодходит для частных лиц и бизнеса: анализирует договоры, готовит документы, оценивает риски и консультирует по трудовому, гражданскому, налоговому и корпоративному праву.\n\nЭто ранний доступ. Часть функционала может не работать."
    }
}
