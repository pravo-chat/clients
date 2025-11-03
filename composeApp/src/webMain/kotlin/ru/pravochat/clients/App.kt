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
        // Основной контент - вертикальный контейнер как в дизайне
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                alignItems(AlignItems.Center) // Вертикальное центрирование
                justifyContent(JustifyContent.Center) // Горизонтальное центрирование
            }
        }) {
            // Левый отступ (гибкий)
            Space()
            
            // Frame 2 - контейнер с заголовком и Input (как в дизайне)
            Div({
                style {
                    width(740.px) // ✅ Размер из дизайна
                    maxWidth(100.percent)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center) // ✅ Counter axis align: CENTER
                    gap(24.px) // ✅ Item spacing: 24px между заголовком и Input
                }
            }) {
                // Заголовок
                H2({
                    style {
                        fontSize(24.px)
                        fontWeight("700")
                        property("line-height", "1.0") // 24px line height
                        color(Colors.TextPrimary)
                        margin(0.px)
                        textAlign("center")
                        width(100.percent) // Растягивается на всю ширину Frame 2
                    }
                }) {
                    Text("Как я могу помочь?")
                }
                
                // Input сразу под заголовком в том же контейнере
                ChatInputCompact()
            }
            
            // Правый отступ (гибкий)
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

// Компактная версия Input для использования внутри Frame 2
@Composable
fun ChatInputCompact() {
    // Input контейнер с кнопкой внутри
    Div({
        style {
            width(100.percent) // Растягивается на всю ширину родителя (740px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row) // ✅ HORIZONTAL layout
            alignItems(AlignItems.Center) // ✅ Counter axis align: CENTER
            justifyContent(JustifyContent.SpaceBetween) // ✅ Primary axis align: MAX (кнопка справа)
            gap(10.px) // ✅ Item spacing: 10px
            backgroundColor(Colors.BackgroundWhite)
            borderRadius(16.px)
            paddingTop(20.px)
            paddingRight(16.px)
            paddingBottom(20.px)
            paddingLeft(16.px)
            property("border", "0.5px solid rgba(0, 0, 0, 0.1)")
        }
    }) {
        // TextArea для многострочного ввода
        TextArea(attrs = {
            placeholder("Спросите что-нибудь...")
            rows(1)
            style {
                flex(1)
                border(0.px)
                property("outline", "none")
                backgroundColor(Color.transparent)
                fontSize(16.px)
                fontWeight("400")
                property("line-height", "1.0")
                fontFamily(Colors.FontFamily)
                color(Colors.TextPrimary)
                property("resize", "none") // Отключаем ручное изменение размера
                property("overflow-y", "auto") // Показываем скролл при необходимости
                property("overflow-x", "hidden") // Скрываем горизонтальный скролл
                property("min-height", "24px") // Минимальная высота
                property("max-height", "120px") // Максимальная высота (примерно 5 строк)
            }
            onInput { event ->
                // Автоматическое изменение высоты при вводе
                js("var textarea = event.target; if (textarea) { textarea.style.height = 'auto'; var newHeight = Math.min(textarea.scrollHeight, 120); textarea.style.height = newHeight + 'px'; }")
            }
        })
        
        // Кнопка отправки внутри Input контейнера
        Button(attrs = {
            onClick { 
                js("console.log('Send message clicked')")
            }
            style {
                width(32.px)
                height(32.px)
                border(0.px)
                backgroundColor(Color.transparent) // SVG содержит фон
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                property("cursor", "pointer")
                padding(0.px)
                property("transition", "opacity 200ms")
                property("flex-shrink", "0") // Не сжимается
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
                gap(10.px) // ✅ Исправлено: было 12px, должно быть 10px по дизайну
                alignItems(AlignItems.Center)
            }
        }) {
            // Input контейнер
            Div({
                style {
                    flex(1)
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                    backgroundColor(Colors.BackgroundWhite)
                    borderRadius(16.px) // ✅ Исправлено: было 24px, должно быть 16px по дизайну
                    paddingTop(20.px) // ✅ Исправлено: было padding(12px, 16px), должно быть T=20 R=16 B=20 L=16
                    paddingRight(16.px)
                    paddingBottom(20.px)
                    paddingLeft(16.px)
                    property("border", "0.5px solid rgba(0, 0, 0, 0.1)") // ✅ Исправлено: stroke weight 0.5px, opacity 0.1
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
                        property("line-height", "1.0") // ✅ Исправлено: было 1.2, должно быть 1.0 (16px) по дизайну
                        fontFamily(Colors.FontFamily)
                        color(Colors.TextPrimary)
                    }
                    placeholder("Спросите что-нибудь...") // ✅ Исправлено: было "Введите ваш вопрос...", должно быть "Спросите что-нибудь..."
                }
            }
            
            // Кнопка отправки
            Button(attrs = {
                onClick { 
                    js("console.log('Send message clicked')")
                }
                style {
                    width(32.px) // ✅ Исправлено: было 48px, должно быть 32px по дизайну
                    height(32.px) // ✅ Исправлено: было 48px, должно быть 32px по дизайну
                    border(0.px)
                    backgroundColor(Color.transparent) // SVG содержит фон
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                    property("cursor", "pointer")
                    padding(0.px)
                    property("transition", "opacity 200ms")
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
        
        // Правый отступ (гибкий)
        Space()
    }
}