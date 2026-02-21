# Исправление загрузки картинок с MinIO

## Проблема

Картинки с MinIO по адресу `http://155.212.170.94:9000/privatebct/...` не отображаются, когда сайт открыт по HTTPS, из‑за:

1. **Mixed content** — браузер блокирует запросы с HTTPS-страницы к HTTP-ресурсам.
2. **CORS** — запросы к другому origin могут блокироваться.

## Решение

В nginx добавлено проксирование пути `/minio/` на MinIO. Запросы к `https://chat.pravochat.ru/minio/...` уходят на сервер как `http://155.212.170.94:9000/...`, картинки грузятся с того же домена (нет mixed content и проблем с CORS).

## Что нужно сделать на бэкенде / в приложении

Подменять URL картинок при отдаче клиенту:

- **Было:** `http://155.212.170.94:9000/privatebct/documents/...?X-Amz-...`
- **Стало:** `https://chat.pravochat.ru/minio/privatebct/documents/...?X-Amz-...`

То есть заменить начало URL:

- с `http://155.212.170.94:9000/`
- на `https://chat.pravochat.ru/minio/`

(или на текущий origin, если чат открыт с другого домена).

Пример на JS при получении URL из API:

```js
function toProxiedImageUrl(url) {
  if (!url) return url;
  return url.replace(
    /^https?:\/\/155\.212\.170\.94:9000\//,
    'https://chat.pravochat.ru/minio/'
  );
}
```

## После изменения nginx на сервере

1. Скопировать обновлённый `nginx-chat.conf` (или `nginx-chat-http.conf`) на сервер.
2. Проверить конфиг: `nginx -t`
3. Перезагрузить nginx: `systemctl reload nginx`

После этого при подмене URL в коде картинки начнут открываться.
