package ru.pravochat.clients

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.css.*

@Composable
fun App() {
        Div({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
                padding(40.px)
            }
        }) {
            H1({
                style {
                    color(Color("#333"))
                    marginBottom(20.px)
                    fontSize(32.px)
                }
            }) {
                Text("Pravochat")
            }
            
            P({
                style {
                    color(Color("#666"))
                    fontSize(18.px)
                    margin(10.px, 0.px)
                    textAlign("center")
                    maxWidth(600.px)
                }
            }) {
                Text("ИИ-чат для получения юридических консультаций на базе российского законодательства")
            }
            
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    flexWrap(FlexWrap.Wrap)
                    justifyContent(JustifyContent.Center)
                    gap(20.px)
                    marginTop(30.px)
                }
            }) {
                FeatureCard("⚖️", "Правовые консультации", "Получайте ответы на юридические вопросы")
                FeatureCard("📚", "База знаний", "Актуальная информация о законах РФ")
                FeatureCard("🤖", "ИИ-ассистент", "Умный помощник на основе правовых документов")
                FeatureCard("💼", "Практические советы", "Конкретные рекомендации по вашей ситуации")
            }
            
            P({
                style {
                    color(Color("#4CAF50"))
                    fontSize(16.px)
                    marginTop(40.px)
                    fontWeight("bold")
                }
            }) {
                Text("Приложение в разработке.")
            }
        }
}

@Composable
fun FeatureCard(icon: String, title: String, description: String) {
    Div({
        style {
            backgroundColor(Color("#f8f9fa"))
            padding(20.px)
            borderRadius(12.px)
            border(1.px, LineStyle.Solid, Color("#e9ecef"))
            minWidth(200.px)
            textAlign("center")
        }
    }) {
        P({
            style {
                fontSize(24.px)
                margin(0.px, 0.px, 10.px, 0.px)
            }
        }) {
            Text(icon)
        }
        
        P({
            style {
                fontSize(16.px)
                fontWeight("bold")
                color(Color("#333"))
                margin(0.px, 0.px, 8.px, 0.px)
            }
        }) {
            Text(title)
        }
        
        P({
            style {
                fontSize(14.px)
                color(Color("#666"))
                margin(0.px)
                lineHeight("1.4")
            }
        }) {
            Text(description)
        }
    }
}