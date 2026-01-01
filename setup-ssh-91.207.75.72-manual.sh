#!/bin/bash
# Инструкция для ручной настройки SSH доступа к серверу 91.207.75.72

SERVER_IP="91.207.75.72"
SERVER_USER="root"
PUBLIC_KEY=$(cat ~/.ssh/id_rsa.pub)

echo "🔐 Настройка SSH доступа к бэкенд серверу $SERVER_USER@$SERVER_IP"
echo ""
echo "📋 Твой публичный ключ:"
echo "$PUBLIC_KEY"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📝 Выполни следующие команды:"
echo ""
echo "1. Подключись к серверу (введи пароль):"
echo "   ssh $SERVER_USER@$SERVER_IP"
echo ""
echo "2. На сервере выполни:"
echo "   mkdir -p ~/.ssh"
echo "   chmod 700 ~/.ssh"
echo "   echo '$PUBLIC_KEY' >> ~/.ssh/authorized_keys"
echo "   chmod 600 ~/.ssh/authorized_keys"
echo "   exit"
echo ""
echo "3. Проверь подключение без пароля:"
echo "   ssh $SERVER_USER@$SERVER_IP 'echo ✅ Подключение работает!'"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "💡 Альтернативный способ (если ssh-copy-id не работает):"
echo ""
echo "   ssh-copy-id -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_IP"
echo ""



