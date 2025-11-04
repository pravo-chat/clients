package ru.pravochat.clients

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color

object Colors {
    val BackgroundMain = Color("#F9F9FA")
    val BackgroundWhite = Color("#FFFFFF")
    
    val PrimaryBlue = Color("#308CEF")
    val PrimaryBlue50 = Color("#308CEF")
    val AccentBlue = Color("#147EFF")
    
    val TextPrimary = Color("#000000")
    val TextSecondary = Color("#000000")
    val TextTertiary = Color("#000000")
    val TextWhite = Color("#FFFFFF")
    
    fun black50Alpha(): String = "rgba(0, 0, 0, 0.5)"
    fun black10Alpha(): String = "rgba(0, 0, 0, 0.1)"
    fun blue50Alpha(): String = "rgba(48, 140, 239, 0.5)"
    
    const val FontFamily = "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
}

