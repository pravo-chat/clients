package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatSpacing
import ru.pravochat.clients.designsystem.theme.PravochatTypography

@Composable
fun SimpleHeaderBar() {
    Header({
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.SpaceBetween)
            padding(PravochatSpacing.md)
            paddingLeft(PravochatSpacing.lg)
            paddingRight(PravochatSpacing.lg)
            position(Position.Sticky)
            top(0.px)
            backgroundColor(PravochatColors.BackgroundMain)
            property("z-index", "20")
            property("border-bottom", "1px solid #E6E6E8")
        }
    }) {
        A("/", attrs = {
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                gap(PravochatSpacing.md)
                fontSize(PravochatTypography.Heading.fontSize)
                fontWeight(PravochatTypography.Heading.fontWeight)
                color(PravochatColors.TextPrimary)
                textDecoration("none")
            }
        }) {
            PravochatIcon(src = "/images/pravo-logo.svg", size = 32, alt = "PravoChat logo")
            Text("PravoChat")
        }

        Nav({
            style {
                display(DisplayStyle.Flex)
                gap(PravochatSpacing.lg)
                alignItems(AlignItems.Center)
            }
        }) {
            A("/", attrs = {
                style {
                    fontSize(PravochatTypography.Body.fontSize)
                    fontWeight(PravochatTypography.Body.fontWeight)
                    color(PravochatColors.TextPrimary)
                    textDecoration("none")
                    property("white-space", "nowrap")
                }
            }) {
                Text("Главная")
            }
        }
    }
}

