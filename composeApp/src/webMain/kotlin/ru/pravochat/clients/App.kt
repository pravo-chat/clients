package ru.pravochat.clients

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*

@Composable
fun App() {
    var showContent by remember { mutableStateOf(false) }
    
    Div {
        Button(
            attrs = {
                onClick { showContent = !showContent }
            }
        ) {
            Text("Click me!")
        }
        
        if (showContent) {
            val greeting = remember { Greeting().greet() }
            Div {
                Text("Compose: $greeting")
            }
        }
    }
}