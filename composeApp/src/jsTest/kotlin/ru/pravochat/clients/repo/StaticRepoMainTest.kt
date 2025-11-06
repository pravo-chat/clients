package ru.pravochat.clients.repo

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals

class StaticRepoMainTest {

    @Test
    fun `returns expected introduction text`() = runTest {
        val repo = StaticRepoMain()

        val actual = repo.introduction().first()

        assertEquals(StaticRepoMain.INTRO_TEXT, actual)
    }
}

