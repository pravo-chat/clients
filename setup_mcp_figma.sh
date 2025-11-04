#!/bin/bash

# Скрипт для настройки MCP Figma

echo "=== MCP Figma Setup ==="
echo ""
echo "Этот скрипт поможет настроить MCP Figma для Cursor."
echo ""

# Проверка наличия Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js не найден. Установите Node.js: https://nodejs.org/"
    exit 1
fi

echo "✅ Node.js найден: $(node --version)"
echo "✅ npm найден: $(npm --version)"
echo ""

# Запрос токена
read -p "Введите ваш Figma Personal Access Token: " FIGMA_TOKEN

if [ -z "$FIGMA_TOKEN" ]; then
    echo "❌ Токен не может быть пустым"
    exit 1
fi

echo ""
echo "📦 Установка MCP Figma сервера..."
npm install -g cursor-talk-to-figma-mcp 2>/dev/null || echo "Пакет уже установлен или возникла ошибка"

echo ""
echo "✅ Настройка завершена!"
echo ""
echo "📝 Теперь настройте Cursor:"
echo ""
echo "1. Откройте настройки Cursor"
echo "2. Найдите раздел MCP или Model Context Protocol"
echo "3. Добавьте следующую конфигурацию:"
echo ""
cat << EOF
{
  "mcpServers": {
    "figma": {
      "command": "cursor-talk-to-figma-mcp",
      "env": {
        "FIGMA_ACCESS_TOKEN": "${FIGMA_TOKEN}"
      }
    }
  }
}
EOF

echo ""
echo "📋 Figma File ID для вашего проекта: cuPAW7JfIhcpdoI3C0yJdt"
echo ""
echo "✨ После настройки перезапустите Cursor"

