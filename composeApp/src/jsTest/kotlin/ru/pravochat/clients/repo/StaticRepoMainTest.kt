package ru.pravochat.clients.repo

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StaticRepoMainTest {

    @Test
    fun `returns expected introduction text`() = runTest {
        val repo = StaticRepoMain(this)

        val actual = repo.introduction().first()

        assertEquals(StaticRepoMain.INTRO_TEXT, actual)
    }
}

class StaticTitleRepoTest {

    @Test
    fun `returns expected markdown content`() = runTest {
        val repo = StaticTitleRepo()

        val actual = repo.content().first()

        assertEquals(StaticTitleRepo.CONTENT, actual)
    }
}

