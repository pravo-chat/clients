package ru.pravochat.clients.designsystem.theme

import org.jetbrains.compose.web.css.px

object PravochatTypography {
    const val FontFamily = "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"

    object Heading {
        val fontSize = 24.px
        const val fontWeight = "700"
        const val lineHeight = "1.4"
    }

    object Body {
        val fontSize = 16.px
        const val fontWeight = "400"
        const val lineHeight = "1.5"
    }

    object Caption {
        val fontSize = 14.px
        const val fontWeight = "400"
        const val lineHeight = "1.2"
    }
}

