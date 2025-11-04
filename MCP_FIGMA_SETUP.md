# Настройка MCP Figma для Cursor

## Шаг 1: Получение Figma Personal Access Token

1. Откройте Figma: https://www.figma.com/
2. Перейдите в **Settings** → **Account**
3. Прокрутите до раздела **Personal Access Tokens**
4. Нажмите **Create new token**
5. Дайте токену имя (например: "Cursor MCP")
6. Скопируйте токен (он показывается только один раз!)

## Шаг 2: Установка MCP Figma сервера ✅ ВЫПОЛНЕНО

MCP Figma сервер уже установлен! Используется пакет `cursor-talk-to-figma-mcp` специально для Cursor IDE.

Проверка установки:
```bash
npm list -g cursor-talk-to-figma-mcp
```

Если нужно переустановить:
```bash
npm install -g cursor-talk-to-figma-mcp
```

## Шаг 3: Настройка конфигурации в Cursor

Конфигурация MCP в Cursor обычно находится в:
- macOS: `~/Library/Application Support/Cursor/User/globalStorage/...`
- Или через настройки Cursor: Settings → Features → MCP

### Формат конфигурации (JSON):

```json
{
  "mcpServers": {
    "figma": {
      "command": "cursor-talk-to-figma-mcp",
      "env": {
        "FIGMA_ACCESS_TOKEN": "YOUR_FIGMA_TOKEN_HERE"
      }
    }
  }
}
```

## Шаг 4: Данные вашего проекта

- **Figma File ID**: `cuPAW7JfIhcpdoI3C0yJdt`
- **Figma URL**: https://www.figma.com/design/cuPAW7JfIhcpdoI3C0yJdt/Chat-AI?node-id=26-76&t=wObD9IK4BVcN74hU-0

## После настройки

После настройки MCP Figma вы сможете:
- Читать компоненты дизайна напрямую из Figma
- Получать цвета, шрифты, spacing
- Синхронизировать код с дизайном автоматически

## Альтернативный вариант (если MCP не работает)

Можно использовать Figma REST API напрямую через скрипты для извлечения дизайн-токенов.

