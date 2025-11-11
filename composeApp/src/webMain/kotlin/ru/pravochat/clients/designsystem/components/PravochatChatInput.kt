package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.TextArea
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatSpacing
import ru.pravochat.clients.designsystem.theme.PravochatTypography
import ru.pravochat.clients.states.ButtonStateModel

@Composable
fun PravochatChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    buttonState: ButtonStateModel,
    placeholderText: String = "Спросите что-нибудь...",
    minHeight: Int = 98,
    maxHeight: Int = 320
) {
    Div({
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.FlexEnd)
            justifyContent(JustifyContent.SpaceBetween)
            gap(PravochatSpacing.chatInputGap)
            backgroundColor(PravochatColors.BackgroundWhite)
            borderRadius(16.px)
            padding(PravochatSpacing.md)
            property("border", "0.5px solid ${PravochatColors.black10Alpha()}")
            property("box-sizing", "border-box")
        }
    }) {
        TextArea(attrs = {
            value(value)
            placeholder(placeholderText)
            style {
                flex(1)
                border(0.px)
                property("outline", "none")
                backgroundColor(Color.transparent)
                fontSize(PravochatTypography.Body.fontSize)
                fontWeight(PravochatTypography.Body.fontWeight)
                property("line-height", PravochatTypography.Body.lineHeight)
                fontFamily(PravochatTypography.FontFamily)
                color(PravochatColors.TextPrimary)
                property("resize", "none")
                property("overflow", "hidden")
                property("min-height", "${minHeight}px")
                property("max-height", "${maxHeight}px")
                property("box-sizing", "border-box")
                property("vertical-align", "top")
                paddingTop(4.px)
            }
            onInput { event ->
                event.target.let { element ->
                    val newText = element.asDynamic().value as String
                    onValueChange(newText)
                    
                    element.asDynamic().style.height = "1px"
                    val scrollHeight = element.asDynamic().scrollHeight as Int
                    val actualMinHeight = minHeight - 46
                    val actualMaxHeight = maxHeight - 100
                    val newHeight = maxOf(actualMinHeight, scrollHeight)
                    
                    if (newHeight <= actualMaxHeight) {
                        element.asDynamic().style.height = "${newHeight}px"
                        element.asDynamic().style.overflow = "hidden"
                    } else {
                        element.asDynamic().style.height = "${actualMaxHeight}px"
                        element.asDynamic().style.overflowY = "auto"
                    }
                }
            }
            onChange { event ->
                event.target.let { element ->
                    val newText = element.asDynamic().value as String
                    onValueChange(newText)
                    
                    element.asDynamic().style.height = "1px"
                    val scrollHeight = element.asDynamic().scrollHeight as Int
                    val actualMinHeight = minHeight - 46
                    val actualMaxHeight = maxHeight - 100
                    val newHeight = maxOf(actualMinHeight, scrollHeight)
                    
                    if (newHeight <= actualMaxHeight) {
                        element.asDynamic().style.height = "${newHeight}px"
                        element.asDynamic().style.overflow = "hidden"
                    } else {
                        element.asDynamic().style.height = "${actualMaxHeight}px"
                        element.asDynamic().style.overflowY = "auto"
                    }
                }
            }
        })
        
        PravochatIconButton(
            state = buttonState,
            onClick = onSend
        )
    }
}

