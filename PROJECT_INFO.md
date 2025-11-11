# Pravochat Project Information

## Repositories

### Backend
- **Repository**: `git@github.com:pravo-chat/server.git`
- **URL**: https://github.com/pravo-chat/server
- **Server IP**: `143.198.69.242`

### Frontend (Clients)
- **Current location**: `/Users/antonbutov/StudioProjects/pravochat2/clients`
- **Technology**: Kotlin Multiplatform, Compose for Web

## Project Structure

### Frontend Clients
```
composeApp/src/webMain/kotlin/ru/pravochat/clients/
├── analytics/          # Аналитика (Yandex Metrika)
├── di/                 # Dependency Injection (Koin)
├── repo/               # Репозитории данных
├── states/             # Состояния UI (ButtonState и др.)
├── App.kt              # Главный UI компонент
├── Colors.kt           # Дизайн-система: цвета и шрифты
└── main.kt             # Entry point
```

## Design System

### Figma
- **File ID**: `cuPAW7JfIhcpdoI3C0yJdt`
- **URL**: https://www.figma.com/design/cuPAW7JfIhcpdoI3C0yJdt/Chat-AI?node-id=26-76&t=wObD9IK4BVcN74hU-0

### Current Components
- `Colors` - дизайн-токены (цвета, шрифты)
- `IconButton` - кнопка с иконкой и состояниями
- `ChatInputCompact` - текстовое поле для ввода с авторазмером
- `MessageBubble` - пузырь сообщения в чате
- `Heading` - заголовки
- `BodyText` - текстовые блоки
- `IconImage` - компонент для иконок

## Design System ✅

### Структура
```
designsystem/
├── theme/
│   ├── PravochatColors.kt      # Цвета
│   ├── PravochatTypography.kt  # Типографика
│   └── PravochatSpacing.kt     # Отступы
└── components/
    ├── PravochatText.kt        # Текстовые компоненты
    ├── PravochatIcon.kt        # Иконки
    ├── PravochatButton.kt      # Кнопки
    ├── PravochatChatInput.kt   # Чат-инпут
    └── PravochatLayout.kt      # Layout компоненты
```

### Документация
- `DESIGN_SYSTEM.md` - полная документация
- `composeApp/.../designsystem/README.md` - быстрый старт

### Использование
```kotlin
import ru.pravochat.clients.designsystem.components.*
import ru.pravochat.clients.designsystem.theme.*

@Composable
fun NewPage() {
    PravochatContainer {
        PravochatHeading(text = "Заголовок")
        PravochatBodyText(text = "Текст")
    }
}
```

## Next Steps
- [ ] Добавить компонент PravochatMessageBubble в design system
- [ ] Создать варианты кнопок (primary, secondary, text)
- [ ] Добавить dark theme
- [ ] Создать компоненты для форм (Input, Select, Checkbox)

