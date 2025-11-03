package ru.pravochat.clients

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color

object Colors {
    // Background colors
    val BackgroundMain = Color("#F9F9FA") // Color 7
    val BackgroundWhite = Color("#FFFFFF")
    
    // Primary colors (from design palette)
    val PrimaryBlue = Color("#308CEF") // Color 1 - 100%
    val PrimaryBlue50 = Color("#308CEF") // Color 2 - 50% opacity
    val AccentBlue = Color("#157EFF") // Color 6 - 100%
    
    // Text colors (from design palette - black with opacity)
    val TextPrimary = Color("#000000") // Color 3 - 100% black
    val TextSecondary = Color("#000000") // Color 4 - 50% black (rgba(0,0,0,0.5))
    val TextTertiary = Color("#000000") // Color 5 - 10% black (rgba(0,0,0,0.1))
    val TextWhite = Color("#FFFFFF")
    
    // Helper functions for rgba colors
    fun black50Alpha(): String = "rgba(0, 0, 0, 0.5)"
    fun black10Alpha(): String = "rgba(0, 0, 0, 0.1)"
    fun blue50Alpha(): String = "rgba(48, 140, 239, 0.5)"
    
    // Font
    const val FontFamily = "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"
}

