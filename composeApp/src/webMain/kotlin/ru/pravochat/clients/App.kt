package ru.pravochat.clients

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.attributes.*

// Захардкоженные сообщения для чата
data class Message(val id: Int, val text: String, val isUser: Boolean, val timestamp: String)

val chatMessages = listOf(
    Message(1, "Здравствуйте! У меня вопрос по трудовому законодательству.", true, "10:30"),
    Message(2, "Добрый день! Я готов помочь вам с вопросами по трудовому праву. Расскажите, пожалуйста, подробнее о вашей ситуации.", false, "10:31"),
    Message(3, "Мой работодатель не оплачивает сверхурочные часы. Что мне делать?", true, "10:32"),
    Message(4, "Согласно статье 152 Трудового кодекса РФ, сверхурочная работа оплачивается за первые два часа работы не менее чем в полуторном размере, за последующие часы - не менее чем в двойном размере. Рекомендую составить письменную претензию работодателю и обратиться в Государственную инспекцию труда.", false, "10:33")
)

@Composable
fun App() {
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

        // Область сообщений с прокруткой
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
            }
        }) {
            // Левый отступ (гибкий)
            Space()
            
            // Контент с фиксированной шириной
            Div({
                style {
                    width(800.px)
                    maxWidth(100.percent)
                    paddingTop(20.px)
                    paddingBottom(20.px)
                    property("overflow-y", "auto")
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(16.px)
                }
            }) {
                chatMessages.forEach { message ->
                    MessageBubble(message)
                }
            }
            
            // Правый отступ (гибкий)
            Space()
        }
        
        // Поле ввода
        ChatInput()
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
                property("line-height", "1.2") // 120% как в дизайне (16px * 1.2 = 19.2px для полужирного, 16px для обычного)
            }
        }) {
            Text(message.text)
        }
        
        Span({
            style {
                fontSize(14.px) // ✅ Исправлено: было 12px, должно быть 14px по дизайну
                fontWeight("400")
                property("line-height", "1.2") // 120% как в дизайне (14px * 1.2 = 16.8px)
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
fun ChatInput() {
    Div({
        style {
            backgroundColor(Colors.BackgroundWhite)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            property("border-top", "1px solid ${Colors.black10Alpha()}")
            property("border-left", "none")
            property("border-right", "none")
            property("border-bottom", "none")
            paddingTop(20.px)
            paddingBottom(20.px)
        }
    }) {
        // Левый отступ (гибкий)
        Space()
        
        // Контент с фиксированной шириной
        Div({
            style {
                width(800.px)
                maxWidth(100.percent)
                display(DisplayStyle.Flex)
                gap(12.px)
                alignItems(AlignItems.Center)
            }
        }) {
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                backgroundColor(Colors.BackgroundWhite)
                borderRadius(24.px)
                padding(12.px, 16.px)
                property("border", "1px solid ${Colors.black10Alpha()}")
            }
        }) {
            Input(type = InputType.Text) {
                style {
                    flex(1)
                    border(0.px)
                    property("outline", "none")
                    backgroundColor(Color.transparent)
                    fontSize(16.px)
                    fontWeight("400")
                    property("line-height", "1.2") // 120% как в дизайне
                    fontFamily(Colors.FontFamily)
                }
                placeholder("Введите ваш вопрос...")
            }
        }
        
        Button(attrs = {
            onClick { 
                js("console.log('Send message clicked')")
            }
            style {
                width(48.px)
                height(48.px)
                borderRadius(50.percent)
                backgroundColor(Colors.PrimaryBlue)
                border(0.px)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                property("cursor", "pointer")
                color(Colors.TextWhite)
                fontSize(20.px)
                fontWeight("bold")
                property("transition", "background-color 200ms")
            }
        }) {
            Text("→")
        }
        }
        
        // Правый отступ (гибкий)
        Space()
    }
}