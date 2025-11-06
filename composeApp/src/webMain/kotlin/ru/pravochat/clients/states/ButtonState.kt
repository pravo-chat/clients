package ru.pravochat.clients.states

import androidx.compose.runtime.State
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import ru.pravochat.clients.repo.RepoMain

sealed interface ButtonStateModel {
    object On : ButtonStateModel
    object Off : ButtonStateModel
}

interface ButtonState {
    val state: State<ButtonStateModel>
    fun onClick()
}

class ButtonStateImpl(private val repoMain: RepoMain) : ButtonState {
    private val _state = mutableStateOf<ButtonStateModel>(ButtonStateModel.On)
    override val state: State<ButtonStateModel>
        get() = _state

    override fun onClick() {
        _state.value = ButtonStateModel.Off
    }

}