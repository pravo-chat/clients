package ru.pravochat.clients

import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatTypography

@Deprecated("Use PravochatColors instead", ReplaceWith("PravochatColors", "ru.pravochat.clients.designsystem.theme.PravochatColors"))
object Colors {
    val BackgroundMain = PravochatColors.BackgroundMain
    val BackgroundWhite = PravochatColors.BackgroundWhite
    
    val PrimaryBlue = PravochatColors.PrimaryBlue
    val PrimaryBlue50 = PravochatColors.PrimaryBlue50
    val AccentBlue = PravochatColors.AccentBlue
    
    val TextPrimary = PravochatColors.TextPrimary
    val TextSecondary = PravochatColors.TextSecondary
    val TextTertiary = PravochatColors.TextTertiary
    val TextWhite = PravochatColors.TextWhite
    
    fun black50Alpha(): String = PravochatColors.black50Alpha()
    fun black10Alpha(): String = PravochatColors.black10Alpha()
    fun blue50Alpha(): String = PravochatColors.blue50Alpha()
    
    const val FontFamily = PravochatTypography.FontFamily
}

