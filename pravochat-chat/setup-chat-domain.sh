#!/bin/bash
# Скрипт для настройки домена chat.pravochat.ru на сервере

echo "Настройка домена chat.pravochat.ru..."

# 1. Установка certbot (если еще не установлен)
if ! command -v certbot &> /dev/null; then
    echo "Установка certbot..."
    apt-get update
    apt-get install -y certbot python3-certbot-nginx
fi

# 2. Копирование nginx конфигурации
echo "Копирование nginx конфигурации..."
cp nginx-chat.conf /etc/nginx/sites-available/chat.pravochat.ru

# 3. Создание симлинка
echo "Создание симлинка..."
ln -sf /etc/nginx/sites-available/chat.pravochat.ru /etc/nginx/sites-enabled/

# 4. Проверка конфигурации nginx
echo "Проверка конфигурации nginx..."
nginx -t

if [ $? -eq 0 ]; then
    echo "Конфигурация nginx корректна!"
    
    # 5. Перезагрузка nginx
    systemctl reload nginx
    echo "Nginx перезагружен!"
    
    # 6. Получение SSL сертификата
    echo ""
    echo "Получение SSL сертификата..."
    echo "Убедитесь, что DNS запись для chat.pravochat.ru уже создана и распространилась!"
    echo "Запустите вручную:"
    echo "certbot --nginx -d chat.pravochat.ru"
    
else
    echo "ОШИБКА: Конфигурация nginx некорректна!"
    exit 1
fi

echo ""
echo "Готово! После получения SSL сертификата обновите iframe в index.html на https://chat.pravochat.ru"


