package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

@Composable
fun PravochatSpacer() {
    Div({
        style {
            flex(1)
            minWidth(0.px)
        }
    }) {}
}

@Composable
fun PravochatContainer(
    maxWidth: CSSSizeValue<CSSUnit.px> = 740.px,
    gap: CSSSizeValue<CSSUnit.px> = 24.px,
    content: @Composable () -> Unit
) {
    Div({
        style {
            width(maxWidth)
            maxWidth(100.percent)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            gap(gap)
        }
    }) {
        content()
    }
}

