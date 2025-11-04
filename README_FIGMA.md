# Настройка MCP Figma - Быстрый старт

## Что уже готово:

✅ Файл дизайна: `Chat AI.fig` (в корне проекта)  
✅ Конфигурация: `figma_mcp_config.json`  
✅ Скрипт установки: `setup_mcp_figma.sh`  
✅ Подробная инструкция: `MCP_FIGMA_SETUP.md`

## Быстрая настройка (3 шага):

### 1. Получите Figma Token
- Откройте https://www.figma.com/
- Settings → Account → Personal Access Tokens → Create new token
- Скопируйте токен

### 2. ✅ MCP Figma сервер уже установлен!
Пакет `cursor-talk-to-figma-mcp` установлен глобально.
```bash
# Проверка установки
npm list -g cursor-talk-to-figma-mcp
```

### 3. Добавьте конфигурацию в Cursor
- Откройте настройки Cursor
- Найдите раздел "MCP" или "Model Context Protocol"
- Скопируйте содержимое из `figma_mcp_config.json`
- Замените `YOUR_FIGMA_PERSONAL_ACCESS_TOKEN_HERE` на ваш токен
- Перезапустите Cursor

## После настройки:

Я смогу читать ваш дизайн напрямую из Figma и синхронизировать код с дизайном!

**Figma File ID:** `cuPAW7JfIhcpdoI3C0yJdt`

