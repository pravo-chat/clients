#!/bin/bash
# Скрипт для настройки SSH доступа к бэкенд серверу через сертификат

SERVER_IP="213.171.27.185"
SERVER_USER="root"
SSH_KEY="$HOME/.ssh/id_rsa.pub"

echo "🔐 Настройка SSH доступа к бэкенд серверу $SERVER_USER@$SERVER_IP"
echo ""

# Проверка наличия SSH ключа
if [ ! -f "$SSH_KEY" ]; then
    echo "❌ SSH ключ не найден: $SSH_KEY"
    echo "Создайте ключ: ssh-keygen -t rsa -b 4096"
    exit 1
fi

echo "📋 Публичный ключ:"
cat "$SSH_KEY"
echo ""
echo ""

# Копирование ключа на сервер
echo "📤 Копирование ключа на сервер..."
echo "Введите пароль для $SERVER_USER@$SERVER_IP:"
ssh-copy-id -o StrictHostKeyChecking=no "$SERVER_USER@$SERVER_IP"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Ключ успешно скопирован!"
    echo ""
    echo "🧪 Проверка подключения без пароля..."
    ssh -o StrictHostKeyChecking=no "$SERVER_USER@$SERVER_IP" "echo '✅ Подключение успешно! SSH ключ работает.'"
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "🎉 Настройка завершена! Теперь можно подключаться без пароля:"
        echo "   ssh $SERVER_USER@$SERVER_IP"
    else
        echo "⚠️  Подключение не работает. Проверьте настройки на сервере."
    fi
else
    echo ""
    echo "❌ Ошибка при копировании ключа."
    echo ""
    echo "Альтернативный способ - скопируйте ключ вручную:"
    echo "1. Подключитесь к серверу: ssh $SERVER_USER@$SERVER_IP"
    echo "2. Выполните на сервере:"
    echo "   mkdir -p ~/.ssh"
    echo "   chmod 700 ~/.ssh"
    echo "   echo '$(cat $SSH_KEY)' >> ~/.ssh/authorized_keys"
    echo "   chmod 600 ~/.ssh/authorized_keys"
fi


