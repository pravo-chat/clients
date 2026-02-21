# SEO-ревизия pravochat.ru

**Дата:** 20 февраля 2026

## Исправлено в ходе ревизии

| Проблема | Исправление |
|----------|-------------|
| **Дублирование og:description** | Удалён дубликат на 145+ страницах (причина: дублированный meta снижал качество сниппетов в поиске и соцсетях) |
| **Отсутствие robots** | Добавлен `meta name="robots" content="index, follow"` на consult.html и premium.html |
| **Crawl-delay в robots.txt** | Удалён (устарел для Яндекса с 2018 года, игнорируется основными поисковиками) |

---

## Рекомендации (требуют ручных действий)

### 1. Google Analytics — placeholder
В `index.html` используется заглушка `G-XXXXXXXXXX`. Либо:
- Замените на реальный ID Google Analytics 4, либо
- Удалите блок GA, если он не нужен (Яндекс.Метрика уже есть)

### 2. Брендинг
Смешение «Юридическая помощь» / «PravoChat» / «Ваш Юрист»:
- Главная: «Юридическая помощь»
- Подстраницы (about, статьи): «PravoChat»
- Header/навигация: «Ваш Юрист»

Рекомендация: выбрать один основной бренд и привести к нему заголовки, мета-теги и JSON-LD.

### 3. JSON-LD — контакты
В JSON-LD на главной:
- `sameAs` содержит `https://wa.me/79000000000` — замените на реальный номер
- Проверьте корректность ссылок на Telegram, VK, OK и т.п.

### 4. legal-questions-answers.html — canonical
`canonical` сейчас после `</style>`, но до `</head>`. Семантически корректно, можно оставить или переместить в начало head.

---

## Что сделано хорошо

- **Мета-теги**: уникальные title, description, keywords на страницах
- **Canonical**: заданы на основных и большинстве статей
- **Open Graph**: og:url, og:title, og:description, og:image, og:site_name, og:locale
- **404**: `robots="noindex, follow"`, страница не в sitemap
- **robots.txt**: Sitemap, Host, Clean-param, Disallow для /admin/, /api/
- **Sitemap**: формируется при деплое, исключает 404
- **Верификация**: Google Search Console и Yandex Webmaster
- **JSON-LD**: WebSite, Organization, FAQPage на главной
- **Структура URL**: логичная иерархия категорий и статей

---

## Дополнительные рекомендации

1. **Скорость загрузки**: проверить через PageSpeed Insights (LCP, CLS и т.д.).
2. **Структурированные данные**: добавить Article или QAPage для статей.
3. **Хлебные крошки**: добавить breadcrumb markup (BreadcrumbList) для глубоких страниц.
4. **hreflang**: сейчас только ru, при появлении версий на других языках добавить hreflang.
