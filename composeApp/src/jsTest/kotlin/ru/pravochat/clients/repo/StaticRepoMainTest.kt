package ru.pravochat.clients.repo

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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

class StaticTitleRepoTest {

    @Test
    fun `returns expected title text`() = runTest {
        val repo = StaticTitleRepo()

        val actual = repo.title().first()

        assertEquals(StaticTitleRepo.TITLE_TEXT, actual)
    }
}

