# Pravochat Design System

Дизайн-система для переиспользуемых Compose компонентов проекта Pravochat.

## Структура

```
designsystem/
├── theme/              # Дизайн-токены
│   ├── PravochatColors.kt      # Цветовая палитра
│   ├── PravochatTypography.kt  # Типографика (шрифты, размеры)
│   └── PravochatSpacing.kt     # Отступы и gaps
│
└── components/         # Переиспользуемые компоненты
    ├── PravochatText.kt        # Текстовые компоненты
    ├── PravochatIcon.kt        # Иконки
    ├── PravochatButton.kt      # Кнопки
    ├── PravochatChatInput.kt   # Компонент ввода текста для чата
    └── PravochatLayout.kt      # Layout компоненты (Spacer, Container)
```

## Theme (Тема)

### PravochatColors
Цветовая палитра приложения:

```kotlin
import ru.pravochat.clients.designsystem.theme.PravochatColors

// Фоновые цвета
PravochatColors.BackgroundMain    // #F9F9FA
PravochatColors.BackgroundWhite   // #FFFFFF

// Основные цвета
PravochatColors.PrimaryBlue       // #308CEF
PravochatColors.AccentBlue        // #147EFF

// Текстовые цвета
PravochatColors.TextPrimary       // #000000
PravochatColors.TextWhite         // #FFFFFF

// Alpha цвета
PravochatColors.black50Alpha()    // rgba(0, 0, 0, 0.5)
PravochatColors.black10Alpha()    // rgba(0, 0, 0, 0.1)
```

### PravochatTypography
Типографика и шрифты:

```kotlin
import ru.pravochat.clients.designsystem.theme.PravochatTypography

// Font Family
PravochatTypography.FontFamily  // 'Inter', -apple-system...

// Заголовки
PravochatTypography.Heading.fontSize      // 24px
PravochatTypography.Heading.fontWeight    // 700
PravochatTypography.Heading.lineHeight    // 1.4

// Основной текст
PravochatTypography.Body.fontSize         // 16px
PravochatTypography.Body.fontWeight       // 400
PravochatTypography.Body.lineHeight       // 1.5

// Вспомогательный текст
PravochatTypography.Caption.fontSize      // 14px
PravochatTypography.Caption.fontWeight    // 400
PravochatTypography.Caption.lineHeight    // 1.2
```

### PravochatSpacing
Отступы и gaps:

```kotlin
import ru.pravochat.clients.designsystem.theme.PravochatSpacing

PravochatSpacing.xs           // 4px
PravochatSpacing.sm           // 8px
PravochatSpacing.md           // 12px
PravochatSpacing.lg           // 16px
PravochatSpacing.xl           // 24px
PravochatSpacing.xxl          // 32px

// Специфичные отступы
PravochatSpacing.chatInputGap // 10px
PravochatSpacing.contentGap   // 16px
PravochatSpacing.sectionGap   // 24px
```

## Components (Компоненты)

### PravochatHeading
Заголовок:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatHeading

PravochatHeading(
    text = "Заголовок",
    color = PravochatColors.TextPrimary  // опционально
)
```

### PravochatBodyText
Основной текст:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatBodyText

PravochatBodyText(
    text = "Текст",
    color = PravochatColors.TextPrimary,  // опционально
    preWrap = true                         // опционально, white-space: pre-line
)
```

### PravochatIcon
Иконка:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatIcon

PravochatIcon(
    src = "/images/icon.svg",
    size = 32,        // опционально, по умолчанию 32
    alt = "Icon"      // опционально
)
```

### PravochatIconButton
Кнопка с иконкой:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatIconButton
import ru.pravochat.clients.states.ButtonStateModel

PravochatIconButton(
    state = ButtonStateModel.On,  // или ButtonStateModel.Off
    onClick = { /* действие */ },
    enabledIcon = "/images/button-default.svg",   // опционально
    disabledIcon = "/images/button-disabled.svg", // опционально
    size = 32                                      // опционально
)
```

### PravochatChatInput
Текстовое поле для чата с авторазмером:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatChatInput

var inputText by remember { mutableStateOf("") }

PravochatChatInput(
    value = inputText,
    onValueChange = { inputText = it },
    onSend = { /* отправить сообщение */ },
    buttonState = ButtonStateModel.On,
    placeholderText = "Спросите что-нибудь...",  // опционально
    minHeight = 98,                               // опционально
    maxHeight = 320                               // опционально
)
```

### PravochatSpacer
Гибкий spacer для layout:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatSpacer

PravochatSpacer()  // flex: 1
```

### PravochatContainer
Контейнер с центрированием:

```kotlin
import ru.pravochat.clients.designsystem.components.PravochatContainer

PravochatContainer(
    maxWidth = 740.px,  // опционально
    gap = 24.px         // опционально
) {
    // содержимое
}
```

## Использование

### Пример: Простая страница с заголовком и текстом

```kotlin
import ru.pravochat.clients.designsystem.components.*
import ru.pravochat.clients.designsystem.theme.*

@Composable
fun MyPage() {
    Div({
        style {
            backgroundColor(PravochatColors.BackgroundMain)
            padding(PravochatSpacing.xl)
        }
    }) {
        PravochatHeading(text = "Заголовок страницы")
        
        PravochatBodyText(
            text = "Это основной текст на странице."
        )
    }
}
```

### Пример: Чат-интерфейс

```kotlin
@Composable
fun ChatPage() {
    var inputText by remember { mutableStateOf("") }
    val buttonState = remember { mutableStateOf(ButtonStateModel.On) }
    
    Div({
        style {
            backgroundColor(PravochatColors.BackgroundMain)
            height(100.vh)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
        }
    }) {
        // Область сообщений
        Div({ style { flex(1) } }) {
            // сообщения
        }
        
        // Поле ввода
        PravochatChatInput(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                println("Отправка: $inputText")
                inputText = ""
            },
            buttonState = buttonState.value
        )
    }
}
```

## Миграция со старого кода

Старый объект `Colors` помечен как `@Deprecated` и теперь является alias для `PravochatColors`.

### До:
```kotlin
import ru.pravochat.clients.Colors

backgroundColor(Colors.BackgroundMain)
```

### После:
```kotlin
import ru.pravochat.clients.designsystem.theme.PravochatColors

backgroundColor(PravochatColors.BackgroundMain)
```

## Best Practices

1. **Всегда используйте компоненты из designsystem**, а не создавайте новые inline-компоненты
2. **Используйте токены из theme** (цвета, отступы, типографику) вместо hardcoded значений
3. **Не изменяйте стили существующих компонентов** - создавайте новые варианты через параметры
4. **Документируйте новые компоненты** в этом файле

## Roadmap

- [ ] Добавить компонент для MessageBubble
- [ ] Создать variants для кнопок (primary, secondary, text)
- [ ] Добавить dark theme поддержку
- [ ] Создать Storybook/документацию с визуальными примерами
- [ ] Добавить компоненты для форм (Input, Select, Checkbox)

