#!/bin/bash

# Скрипт для деплоя чата на сервер
# Использование: ./deploy.sh

SERVER_IP="155.212.170.94"
SERVER_USER="root"
SERVER_PATH="/opt/pravochat-chat"

echo "🚀 Деплой PravoChat Chat на сервер..."

# Копируем файлы на сервер
echo "📦 Копирование файлов..."
scp -r . ${SERVER_USER}@${SERVER_IP}:${SERVER_PATH}

# Подключаемся и устанавливаем зависимости
echo "📥 Установка зависимостей..."
ssh ${SERVER_USER}@${SERVER_IP} << 'ENDSSH'
cd /opt/pravochat-chat
if [ ! -d "node_modules" ]; then
    npm install
fi
ENDSSH

echo "✅ Деплой завершен!"
echo ""
echo "Для запуска на сервере выполните:"
echo "  ssh root@155.212.170.94"
echo "  cd /opt/pravochat-chat"
echo "  npm run dev  # для разработки"
echo "  npm run build && npm run start  # для продакшена"
echo ""
echo "Не забудьте создать .env файл с OPENAI_API_KEY!"





