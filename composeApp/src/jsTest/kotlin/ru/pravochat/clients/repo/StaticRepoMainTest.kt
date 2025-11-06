package ru.pravochat.clients.repo

import kotlin.test.Test
import kotlin.test.assertEquals

class StaticRepoMainTest {

    @Test
    fun `returns expected introduction text`() {
        val repo = StaticRepoMain()

        val actual = repo.getIntroduction()

        assertEquals(StaticRepoMain.INTRO_TEXT, actual)
    }
}

