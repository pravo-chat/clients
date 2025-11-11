package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatTypography

@Composable
fun PravochatHeading(
    text: String,
    color: CSSColorValue = PravochatColors.TextPrimary
) {
    H2({
        style {
            fontSize(PravochatTypography.Heading.fontSize)
            fontWeight(PravochatTypography.Heading.fontWeight)
            property("line-height", PravochatTypography.Heading.lineHeight)
            color(color)
            margin(0.px)
            textAlign("left")
        }
    }) {
        Text(text)
    }
}

@Composable
fun PravochatBodyText(
    text: String,
    color: CSSColorValue = PravochatColors.TextPrimary,
    preWrap: Boolean = true
) {
    Div({
        style {
            fontSize(PravochatTypography.Body.fontSize)
            fontWeight(PravochatTypography.Body.fontWeight)
            property("line-height", PravochatTypography.Body.lineHeight)
            color(color)
            textAlign("left")
            if (preWrap) {
                property("white-space", "pre-line")
            }
        }
    }) {
        Text(text)
    }
}

