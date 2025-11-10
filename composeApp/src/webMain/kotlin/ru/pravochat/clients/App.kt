package ru.pravochat.clients

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.designsystem.components.*
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatTypography
import ru.pravochat.clients.designsystem.theme.PravochatSpacing
import ru.pravochat.clients.di.koinInjectRemember
import ru.pravochat.clients.repo.TitleRepo
import ru.pravochat.clients.states.ButtonState

data class Message(val id: Int, val text: String, val isUser: Boolean, val timestamp: String)

val chatMessages = listOf(
    Message(1, "Здравствуйте! У меня вопрос по трудовому законодательству.", true, "10:30"),
    Message(2, "Добрый день! Я готов помочь вам с вопросами по трудовому праву. Расскажите, пожалуйста, подробнее о вашей ситуации.", false, "10:31"),
    Message(3, "Мой работодатель не оплачивает сверхурочные часы. Что мне делать?", true, "10:32"),
    Message(4, "Согласно статье 152 Трудового кодекса РФ, сверхурочная работа оплачивается за первые два часа работы не менее чем в полуторном размере, за последующие часы - не менее чем в двойном размере. Рекомендую составить письменную претензию работодателю и обратиться в Государственную инспекцию труда.", false, "10:33")
)

@Composable
fun MarkdownContent(markdown: String) {
    if (markdown.isBlank()) return
    markdown.split("\n\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .forEach { block ->
            when {
                block.startsWith("## ") -> PravochatHeading(text = block.removePrefix("## ").trim())
                else -> PravochatBodyText(text = block)
            }
        }
}

@Composable
fun App() {
    val titleRepo = koinInjectRemember<TitleRepo>()
    val content by titleRepo.content().collectAsState(initial = "")
    Div({
        style {
            width(100.percent)
            height(100.vh)
            margin(0.px)
            padding(0.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
            property("overflow", "hidden")
        }
    }) {
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
            }
        }) {
            PravochatSpacer()
            
            PravochatContainer {
                Div({
                    style {
                        width(100.percent)
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Column)
                        gap(PravochatSpacing.contentGap)
                    }
                }) {
                    MarkdownContent(content)
                }
                
                ChatInputCompact()
            }
            
            PravochatSpacer()
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignSelf(if (message.isUser) AlignSelf.FlexEnd else AlignSelf.FlexStart)
            maxWidth(70.percent)
        }
    }) {
        Div({
            style {
                padding(PravochatSpacing.md, PravochatSpacing.lg)
                borderRadius(16.px)
                backgroundColor(if (message.isUser) PravochatColors.PrimaryBlue else PravochatColors.BackgroundWhite)
                color(if (message.isUser) PravochatColors.TextWhite else PravochatColors.TextPrimary)
                property("box-shadow", "0px 1px 3px ${PravochatColors.black10Alpha()}")
                fontSize(PravochatTypography.Body.fontSize)
                fontWeight(PravochatTypography.Body.fontWeight)
                property("line-height", PravochatTypography.Caption.lineHeight)
            }
        }) {
            Text(message.text)
        }
        
        Span({
            style {
                fontSize(PravochatTypography.Caption.fontSize)
                fontWeight(PravochatTypography.Caption.fontWeight)
                property("line-height", PravochatTypography.Caption.lineHeight)
                property("color", PravochatColors.black50Alpha())
                marginTop(PravochatSpacing.xs)
                marginLeft(if (message.isUser) 0.px else 0.px)
                marginRight(if (message.isUser) 0.px else 0.px)
                alignSelf(if (message.isUser) AlignSelf.FlexEnd else AlignSelf.FlexStart)
            }
        }) {
            Text(message.timestamp)
        }
    }
}

@Composable
fun ChatInputCompact() {
    var inputText by remember { mutableStateOf("") }
    val analytics: AnalyticsTracker = koinInjectRemember()
    val buttonState = koinInjectRemember<ButtonState>()
    
    PravochatChatInput(
        value = inputText,
        onValueChange = { inputText = it },
        onSend = {
            buttonState.onClick()
            analytics.send("chat_input", inputText)
            js("console.log('Send message clicked')")
        },
        buttonState = buttonState.state.value
    )
}

