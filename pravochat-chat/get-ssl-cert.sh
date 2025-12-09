#!/bin/bash
# Скрипт для получения SSL сертификата для chat.pravochat.ru
# Запустите после того, как DNS распространится

echo "Получение SSL сертификата для chat.pravochat.ru..."
echo "Убедитесь, что DNS запись уже распространилась!"
echo "Проверьте: dig chat.pravochat.ru или nslookup chat.pravochat.ru"
echo ""

# Установка certbot если нужно
if ! command -v certbot &> /dev/null; then
    echo "Установка certbot..."
    apt-get update
    apt-get install -y certbot python3-certbot-nginx
fi

# Получение SSL сертификата
echo "Получение SSL сертификата..."
certbot --nginx -d chat.pravochat.ru --non-interactive --agree-tos --email admin@pravochat.ru

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SSL сертификат успешно получен!"
    echo "Проверьте работу: curl -I https://chat.pravochat.ru"
else
    echo ""
    echo "❌ Ошибка при получении SSL сертификата"
    echo "Убедитесь, что:"
    echo "1. DNS запись распространилась (проверьте: dig chat.pravochat.ru)"
    echo "2. Порт 80 открыт и доступен из интернета"
    echo "3. Nginx работает и слушает на порту 80"
fi


