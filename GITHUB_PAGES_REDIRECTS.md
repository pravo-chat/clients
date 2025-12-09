# Решение редиректов для GitHub Pages

## Проблема
GitHub Pages не поддерживает серверные редиректы (как nginx), поэтому `/index` и `/index.html` возвращают 200 вместо редиректа на `/`.

## Решение 1: HTML редиректы (уже добавлено в workflow)
В workflow автоматически создаются HTML файлы с редиректами после билда.

## Решение 2: Cloudflare (рекомендуется)
Используйте Cloudflare перед GitHub Pages для настоящих 301 редиректов:

1. Подключите домен к Cloudflare
2. Настройте DNS записи на GitHub Pages
3. В Cloudflare Dashboard → Rules → Redirect Rules создайте:
   - `/index` → `/` (301 Permanent Redirect)
   - `/index.html` → `/` (301 Permanent Redirect)

## Решение 3: Настройка HTTPS в GitHub Pages
1. Зайдите в Settings репозитория
2. Pages → Enforce HTTPS (включите)
3. Это автоматически редиректит http → https

## Проверка
После деплоя проверьте:
- `curl -I https://pravochat.ru/index` - должен быть редирект
- `curl -I https://pravochat.ru/index.html` - должен быть редирект
- `curl -I http://pravochat.ru/` - должен редиректить на https

