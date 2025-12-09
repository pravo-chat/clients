# PravoChat Chat Application

Next.js чат-приложение с Vercel AI SDK для развертывания на сервере.

## Установка на сервере

1. Скопируйте всю папку `pravochat-chat` на сервер:
   ```bash
   scp -r pravochat-chat root@155.212.170.94:/opt/
   ```

2. Подключитесь к серверу:
   ```bash
   ssh root@155.212.170.94
   ```

3. Установите зависимости:
   ```bash
   cd /opt/pravochat-chat
   npm install
   ```

4. Создайте файл `.env`:
   ```bash
   cp .env.example .env
   nano .env
   # Добавьте ваш OPENAI_API_KEY
   ```

5. Запустите в режиме разработки:
   ```bash
   npm run dev
   ```

6. Для продакшена:
   ```bash
   npm run build
   npm run start
   ```

## Использование с PM2

Для постоянной работы в фоне:

```bash
npm install -g pm2
pm2 start npm --name "pravochat-chat" -- start
pm2 save
pm2 startup
```

## Настройка домена chat.pravochat.ru для продакшена

### Шаг 1: Создание DNS записи

У вашего регистратора домена создайте A-запись:
- **Тип**: A
- **Имя**: `chat` (или `chat.pravochat.ru`)
- **Значение**: `155.212.170.94`
- **TTL**: 3600 (или по умолчанию)

Подождите 5-30 минут, пока DNS распространится (проверьте: `dig chat.pravochat.ru` или `nslookup chat.pravochat.ru`).

### Шаг 2: Настройка Nginx и SSL

1. Скопируйте файлы конфигурации на сервер:
   ```bash
   scp pravochat-chat/nginx-chat.conf root@155.212.170.94:/opt/pravochat-chat/
   scp pravochat-chat/setup-chat-domain.sh root@155.212.170.94:/opt/pravochat-chat/
   ```

2. На сервере запустите скрипт настройки:
   ```bash
   ssh root@155.212.170.94
   cd /opt/pravochat-chat
   chmod +x setup-chat-domain.sh
   ./setup-chat-domain.sh
   ```

3. После того, как DNS распространился, получите SSL сертификат:
   ```bash
   certbot --nginx -d chat.pravochat.ru
   ```

4. Проверьте, что все работает:
   ```bash
   curl -I https://chat.pravochat.ru
   ```

### Шаг 3: Обновление iframe в index.html

После настройки домена обновите `src` iframe в `composeApp/src/webMain/resources/index.html`:

```html
<iframe 
    src="https://chat.pravochat.ru" 
    ...
</iframe>
```

### Проверка работы

1. Откройте в браузере: `https://chat.pravochat.ru` - должен открыться чат
2. Откройте основной сайт `https://pravochat.ru` - чат должен работать в iframe
3. Проверьте консоль браузера на наличие ошибок CORS или mixed content




