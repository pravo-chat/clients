package ru.pravochat.clients.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface TitleRepo {
    fun title(): Flow<String>
}

class StaticTitleRepo : TitleRepo {
    override fun title(): Flow<String> = flowOf(TITLE_TEXT)

    companion object {
        const val TITLE_TEXT = "ИИ-юридический консультант"
    }
}

