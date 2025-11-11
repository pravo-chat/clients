package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import ru.pravochat.clients.states.ButtonStateModel

@Composable
fun PravochatIconButton(
    state: ButtonStateModel,
    onClick: () -> Unit,
    enabledIcon: String = "/images/button-default.svg",
    disabledIcon: String = "/images/button-disabled.svg",
    size: Int = 32
) {
    val iconSrc = when (state) {
        ButtonStateModel.On -> enabledIcon
        ButtonStateModel.Off -> disabledIcon
    }

    Button(attrs = {
        this.onClick {
            if (state == ButtonStateModel.On) {
                onClick()
            }
        }
        if (state == ButtonStateModel.Off) {
            disabled()
        }
        style {
            width(size.px)
            height(size.px)
            border(0.px)
            backgroundColor(Color.transparent)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(0.px)
            property("transition", "opacity 200ms")
            property("flex-shrink", "0")
        }
    }) {
        PravochatIcon(src = iconSrc, size = size)
    }
}

