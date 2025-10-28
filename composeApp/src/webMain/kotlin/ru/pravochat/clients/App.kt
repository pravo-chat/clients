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
            Text("🚗 Pravochat Web App")
        }
        
        P({
            style {
                color(Color("#666"))
                fontSize(18.px)
                margin(10.px, 0.px)
            }
        }) {
            Text("Hello World from Compose for Web!")
        }
        
        P({
            style {
                color(Color("#666"))
                fontSize(18.px)
                margin(10.px, 0.px)
            }
        }) {
            Text("This is a Kotlin Multiplatform web application.")
        }
        
        P({
            style {
                color(Color("#4CAF50"))
                fontSize(18.px)
                margin(10.px, 0.px)
                fontWeight("bold")
            }
        }) {
            Text("✅ Compose for Web is working correctly!")
        }
    }
}