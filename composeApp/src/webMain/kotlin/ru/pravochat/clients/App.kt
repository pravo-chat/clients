package ru.pravochat.clients

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.attributes.*
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.di.koinInjectRemember
import ru.pravochat.clients.repo.RepoMain
import ru.pravochat.clients.repo.TitleRepo

data class Message(val id: Int, val text: String, val isUser: Boolean, val timestamp: String)

val chatMessages = listOf(
    Message(1, "Здравствуйте! У меня вопрос по трудовому законодательству.", true, "10:30"),
    Message(2, "Добрый день! Я готов помочь вам с вопросами по трудовому праву. Расскажите, пожалуйста, подробнее о вашей ситуации.", false, "10:31"),
    Message(3, "Мой работодатель не оплачивает сверхурочные часы. Что мне делать?", true, "10:32"),
    Message(4, "Согласно статье 152 Трудового кодекса РФ, сверхурочная работа оплачивается за первые два часа работы не менее чем в полуторном размере, за последующие часы - не менее чем в двойном размере. Рекомендую составить письменную претензию работодателю и обратиться в Государственную инспекцию труда.", false, "10:33")
)

@Composable
fun Heading(textProvider: () -> String) {
    H2({
        style {
            fontSize(24.px)
            fontWeight("700")
            property("line-height", "1.4")
            color(Colors.TextPrimary)
            margin(0.px)
            textAlign("left")
        }
    }) {
        Text(textProvider())
    }
}

@Composable
fun BodyText(textProvider: () -> String) {
    Div({
        style {
            fontSize(16.px)
            fontWeight("400")
            property("line-height", "1.5")
            color(Colors.TextPrimary)
            textAlign("left")
            property("white-space", "pre-line")
        }
    }) {
        Text(textProvider())
    }
}

@Composable
fun App() {
    val repoMain = koinInjectRemember<RepoMain>()
    val titleRepo = koinInjectRemember<TitleRepo>()
    val introduction by repoMain.introduction().collectAsState(initial = "")
    val title by titleRepo.title().collectAsState(initial = "")
    Div({
        style {
            width(100.percent)
            height(100.vh)
            margin(0.px)
            padding(0.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            backgroundColor(Colors.BackgroundMain)
            fontFamily(Colors.FontFamily)
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
            Space()
            
            Div({
                style {
                    width(740.px)
                    maxWidth(100.percent)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center)
                    gap(24.px)
                }
            }) {
                Div({
                    style {
                        width(100.percent)
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Column)
                        gap(16.px)
                    }
                }) {
                    Heading { title }
                    BodyText { introduction }
                }
                
                ChatInputCompact()
            }
            
            Space()
        }
    }
}

@Composable
fun Space() {
    Div({
        style {
            flex(1)
            minWidth(0.px)
        }
    }) {}
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
                padding(12.px, 16.px)
                borderRadius(16.px)
                backgroundColor(if (message.isUser) Colors.PrimaryBlue else Colors.BackgroundWhite)
                color(if (message.isUser) Colors.TextWhite else Colors.TextPrimary)
                property("box-shadow", "0px 1px 3px rgba(0, 0, 0, 0.1)")
                fontSize(16.px)
                fontWeight("400")
                property("line-height", "1.2")
            }
        }) {
            Text(message.text)
        }
        
        Span({
            style {
                fontSize(14.px)
                fontWeight("400")
                property("line-height", "1.2")
                property("color", Colors.black50Alpha())
                marginTop(4.px)
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
    
    Div({
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.FlexEnd)
            justifyContent(JustifyContent.SpaceBetween)
            gap(10.px)
            backgroundColor(Colors.BackgroundWhite)
            borderRadius(16.px)
            paddingTop(12.px)
            paddingRight(12.px)
            paddingBottom(12.px)
            paddingLeft(12.px)
            property("border", "0.5px solid rgba(0, 0, 0, 0.1)")
            property("box-sizing", "border-box")
        }
    }) {
        val analytics: AnalyticsTracker = koinInjectRemember()

        TextArea(attrs = {
            value(inputText)
            placeholder("Спросите что-нибудь...")
            style {
                flex(1)
                border(0.px)
                property("outline", "none")
                backgroundColor(Color.transparent)
                fontSize(16.px)
                fontWeight("400")
                property("line-height", "1.5")
                fontFamily(Colors.FontFamily)
                color(Colors.TextPrimary)
                property("resize", "none")
                property("overflow", "hidden")
                property("min-height", "98px")
                property("max-height", "320px")
                property("box-sizing", "border-box")
                property("vertical-align", "top")
                paddingTop(4.px)
            }
            onInput { event ->
                event.target.let { element ->
                    val newText = element.asDynamic().value as String
                    inputText = newText
                    
                    element.asDynamic().style.height = "1px"
                    val scrollHeight = element.asDynamic().scrollHeight as Int
                    val minHeight = 52
                    val maxHeight = 120
                    val newHeight = maxOf(minHeight, scrollHeight)
                    
                    if (newHeight <= maxHeight) {
                        element.asDynamic().style.height = "${newHeight}px"
                        element.asDynamic().style.overflow = "hidden"
                    } else {
                        element.asDynamic().style.height = "${maxHeight}px"
                        element.asDynamic().style.overflowY = "auto"
                    }
                }
            }
            onChange { event ->
                event.target.let { element ->
                    val newText = element.asDynamic().value as String
                    inputText = newText
                    
                    element.asDynamic().style.height = "1px"
                    val scrollHeight = element.asDynamic().scrollHeight as Int
                    val minHeight = 52
                    val maxHeight = 120
                    val newHeight = maxOf(minHeight, scrollHeight)
                    
                    if (newHeight <= maxHeight) {
                        element.asDynamic().style.height = "${newHeight}px"
                        element.asDynamic().style.overflow = "hidden"
                    } else {
                        element.asDynamic().style.height = "${maxHeight}px"
                        element.asDynamic().style.overflowY = "auto"
                    }
                }
            }
        })
        
        Button(attrs = {
            onClick { 
                if (inputText.isNotBlank()) {
                    analytics.send("chat_input", inputText)
                }
                js("console.log('Send message clicked')")
            }
            style {
                width(32.px)
                height(32.px)
                border(0.px)
                backgroundColor(Color.transparent)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                property("cursor", "pointer")
                padding(0.px)
                property("transition", "opacity 200ms")
                property("flex-shrink", "0")
            }
        }) {
            Img(src = "/images/button-default.svg", attrs = {
                style {
                    width(32.px)
                    height(32.px)
                    property("object-fit", "contain")
                    property("display", "block")
                }
            })
        }
    }
}
