package ru.pravochat.clients.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

interface TitleRepo {
    fun content(): Flow<String>
}

class StaticTitleRepo() : TitleRepo {
    override fun content(): Flow<String> = flow {
        emit(CONTENT)
    }

    companion object {
        const val TITLE_TEXT = "ИИ-юридический консультант"
        const val INTRO_TEXT =
            "— это интеллектуальная система, основанная на искусственном интеллекте, которая помогает решать правовые вопросы, анализировать документы и давать точные рекомендации. Она работает круглосуточно, мгновенно обрабатывает запросы и упрощает работу юристов и предпринимателей.\n\nПодходит для частных лиц и бизнеса: анализирует договоры, готовит документы, оценивает риски и консультирует по трудовому, гражданскому, налоговому и корпоративному праву.\n\nЭто ранний доступ. Часть функционала может не работать."

        val CONTENT: String = """
## $TITLE_TEXT

$INTRO_TEXT
""".trimIndent()
    }
}

