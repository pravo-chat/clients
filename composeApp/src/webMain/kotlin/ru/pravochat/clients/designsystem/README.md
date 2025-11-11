# Pravochat Design System

Переиспользуемые Compose компоненты для быстрого создания страниц с единообразным дизайном.

## Быстрый старт

### 1. Импорты

```kotlin
// Тема
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatTypography
import ru.pravochat.clients.designsystem.theme.PravochatSpacing

// Компоненты
import ru.pravochat.clients.designsystem.components.*
```

### 2. Создание новой страницы

```kotlin
@Composable
fun NewPage() {
    // Корневой контейнер
    Div({
        style {
            width(100.percent)
            height(100.vh)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
        }
    }) {
        // Центрирование контента
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
            }
        }) {
            PravochatSpacer()
            
            PravochatContainer(maxWidth = 740.px) {
                // Ваш контент здесь
                PravochatHeading(text = "Заголовок")
                PravochatBodyText(text = "Текст страницы")
            }
            
            PravochatSpacer()
        }
    }
}
```

## Готовые компоненты

### Текст

```kotlin
// Заголовок
PravochatHeading(text = "Заголовок H2")

// Основной текст
PravochatBodyText(text = "Текст абзаца")

// С кастомным цветом
PravochatBodyText(
    text = "Серый текст",
    color = PravochatColors.TextSecondary
)
```

### Кнопки и иконки

```kotlin
// Иконка
PravochatIcon(
    src = "/images/icon.svg",
    size = 32
)

// Кнопка с иконкой
val buttonState = remember { mutableStateOf(ButtonStateModel.On) }

PravochatIconButton(
    state = buttonState.value,
    onClick = { /* действие */ }
)
```

### Чат-инпут

```kotlin
var inputText by remember { mutableStateOf("") }
val buttonState = remember { mutableStateOf(ButtonStateModel.On) }

PravochatChatInput(
    value = inputText,
    onValueChange = { inputText = it },
    onSend = {
        println("Отправка: $inputText")
        inputText = ""
    },
    buttonState = buttonState.value
)
```

### Layout

```kotlin
// Spacer (flex: 1)
PravochatSpacer()

// Контейнер с максимальной шириной
PravochatContainer(maxWidth = 740.px, gap = 24.px) {
    // контент
}
```

## Использование темы

### Цвета

```kotlin
style {
    backgroundColor(PravochatColors.BackgroundMain)
    color(PravochatColors.TextPrimary)
    borderColor(PravochatColors.PrimaryBlue)
}
```

### Типографика

```kotlin
style {
    fontFamily(PravochatTypography.FontFamily)
    fontSize(PravochatTypography.Body.fontSize)
    fontWeight(PravochatTypography.Body.fontWeight)
    property("line-height", PravochatTypography.Body.lineHeight)
}
```

### Отступы

```kotlin
style {
    padding(PravochatSpacing.md)
    gap(PravochatSpacing.lg)
    margin(PravochatSpacing.xl)
}
```

## Примеры страниц

### Простая информационная страница

```kotlin
@Composable
fun InfoPage() {
    Div({
        style {
            width(100.percent)
            minHeight(100.vh)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
            padding(PravochatSpacing.xl)
        }
    }) {
        PravochatContainer {
            PravochatHeading(text = "О проекте")
            
            PravochatBodyText(text = """
                Pravochat - это инновационный сервис для 
                консультаций по юридическим вопросам.
            """.trimIndent())
            
            PravochatBodyText(
                text = "Подробнее...",
                color = PravochatColors.PrimaryBlue
            )
        }
    }
}
```

### Страница с формой

```kotlin
@Composable
fun ContactPage() {
    var inputText by remember { mutableStateOf("") }
    val buttonState = remember { mutableStateOf(ButtonStateModel.On) }
    
    Div({
        style {
            width(100.percent)
            height(100.vh)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
        }
    }) {
        PravochatContainer(maxWidth = 600.px) {
            PravochatHeading(text = "Свяжитесь с нами")
            
            PravochatChatInput(
                value = inputText,
                onValueChange = { inputText = it },
                onSend = {
                    // отправка формы
                    inputText = ""
                },
                buttonState = buttonState.value,
                placeholderText = "Ваше сообщение..."
            )
        }
    }
}
```

## Шаблон для новой страницы

```kotlin
package ru.pravochat.clients.pages

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import ru.pravochat.clients.designsystem.components.*
import ru.pravochat.clients.designsystem.theme.*

@Composable
fun YourNewPage() {
    Div({
        style {
            width(100.percent)
            height(100.vh)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            property("overflow", "hidden")
        }
    }) {
        Div({
            style {
                flex(1)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
            }
        }) {
            PravochatSpacer()
            
            PravochatContainer {
                // TODO: Добавьте ваш контент здесь
                PravochatHeading(text = "Новая страница")
            }
            
            PravochatSpacer()
        }
    }
}
```

## Best Practices

1. ✅ **Используйте компоненты из designsystem**
2. ✅ **Используйте токены из theme (цвета, spacing, типографику)**
3. ✅ **Следуйте единой структуре страниц**
4. ❌ Не создавайте inline компоненты с дублирующимися стилями
5. ❌ Не используйте hardcoded значения цветов и размеров

## Добавление новых компонентов

Если вам нужен новый компонент:

1. Создайте файл в `designsystem/components/`
2. Используйте токены из `designsystem/theme/`
3. Документируйте в `DESIGN_SYSTEM.md`
4. Добавьте примеры использования

## Миграция существующего кода

Старый объект `Colors` deprecated, используйте `PravochatColors`:

```kotlin
// Было
import ru.pravochat.clients.Colors
backgroundColor(Colors.BackgroundMain)

// Стало
import ru.pravochat.clients.designsystem.theme.PravochatColors
backgroundColor(PravochatColors.BackgroundMain)
```

