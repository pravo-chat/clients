package ru.pravochat.clients.designsystem.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Img

@Composable
fun PravochatIcon(
    src: String,
    size: Int = 32,
    alt: String = ""
) {
    Img(src = src, alt = alt, attrs = {
        style {
            width(size.px)
            height(size.px)
            property("object-fit", "contain")
            property("display", "block")
        }
    })
}

