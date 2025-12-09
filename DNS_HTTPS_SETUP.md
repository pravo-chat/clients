# Настройка DNS и HTTPS для pravochat.ru на GitHub Pages

## Текущая ситуация
Сайт размещён на GitHub Pages, нужно обеспечить единый протокол HTTPS и закрыть доступ по HTTP.

## Шаг 1: Настройка в GitHub Pages

1. Зайдите в репозиторий на GitHub
2. Settings → Pages
3. Включите **"Enforce HTTPS"** (Принудительное использование HTTPS)
   - Это автоматически редиректит все HTTP запросы на HTTPS

## Шаг 2: Настройка DNS записей

### Вариант A: Использование CNAME (рекомендуется)

Если у вас есть файл `CNAME` в репозитории (содержит `pravochat.ru`):

1. **У вашего регистратора домена** настройте DNS записи:
   ```
   Тип: CNAME
   Имя: @ (или pravochat.ru)
   Значение: [ваш-username].github.io
   TTL: 3600 (или автоматически)
   ```

2. **Или используйте A-записи** (если CNAME не поддерживается для корневого домена):
   ```
   Тип: A
   Имя: @
   Значение: 185.199.108.153
   TTL: 3600
   
   Тип: A
   Имя: @
   Значение: 185.199.109.153
   TTL: 3600
   
   Тип: A
   Имя: @
   Значение: 185.199.110.153
   TTL: 3600
   
   Тип: A
   Имя: @
   Значение: 185.199.111.153
   TTL: 3600
   ```

### Вариант B: Использование Cloudflare (лучше для SEO)

Cloudflare может принудительно редиректить HTTP → HTTPS на уровне CDN:

1. **Подключите домен к Cloudflare:**
   - Зарегистрируйтесь на cloudflare.com
   - Добавьте домен pravochat.ru
   - Измените nameservers у регистратора на те, что даст Cloudflare

2. **Настройте DNS в Cloudflare:**
   - Создайте CNAME запись:
     ```
     Имя: @
     Целевой хост: [ваш-username].github.io
     Прокси: включен (оранжевое облако)
     ```

3. **Включите принудительный HTTPS:**
   - SSL/TLS → Overview → SSL/TLS encryption mode: **"Full"**
   - SSL/TLS → Edge Certificates → **"Always Use HTTPS"** (включите)

4. **Создайте Redirect Rules (для редиректов /index):**
   - Rules → Redirect Rules → Create rule
   - Rule name: "Redirect index to root"
   - If: URI Path equals `/index` OR `/index.html`
   - Then: Redirect to `https://pravochat.ru/` (301 Permanent)

## Шаг 3: Проверка

После настройки проверьте:

```bash
# Проверка HTTP → HTTPS редиректа
curl -I http://pravochat.ru/
# Должен вернуть: HTTP/1.1 301 Moved Permanently
# Location: https://pravochat.ru/

# Проверка HTTPS
curl -I https://pravochat.ru/
# Должен вернуть: HTTP/2 200 OK

# Проверка редиректа /index
curl -I https://pravochat.ru/index
# Должен быть редирект на /
```

## Текущие IP адреса GitHub Pages (для A-записей)

Если используете A-записи, актуальные IP можно проверить:
```bash
dig [ваш-username].github.io +short
```

Обычно это:
- 185.199.108.153
- 185.199.109.153
- 185.199.110.153
- 185.199.111.153

## Примечание

Если используете Cloudflare:
- **Не нужно** настраивать "Enforce HTTPS" в GitHub Pages (Cloudflare сделает это лучше)
- Cloudflare даст настоящие 301 редиректы (лучше для SEO)
- Cloudflare также может делать редиректы `/index` → `/` на уровне CDN

