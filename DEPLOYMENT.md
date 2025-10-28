# Деплой на GitHub Pages

## Настройка GitHub Pages

1. **Перейдите в настройки репозитория:**
   - Откройте ваш репозиторий на GitHub
   - Перейдите в Settings → Pages

2. **Настройте источник:**
   - В разделе "Source" выберите "GitHub Actions"
   - Это позволит использовать созданный workflow для деплоя

3. **Настройте права доступа:**
   - Перейдите в Settings → Actions → General
   - В разделе "Workflow permissions" выберите "Read and write permissions"
   - Поставьте галочку "Allow GitHub Actions to create and approve pull requests"

## Автоматический деплой

После настройки GitHub Pages, ваше приложение будет автоматически деплоиться при каждом push в ветку `main`.

## Локальная сборка

Для локальной сборки выполните:

```bash
cd clients
./gradlew :composeApp:jsBrowserDistribution
```

Собранные файлы будут находиться в `clients/composeApp/build/dist/js/productionExecutable/`

## Структура проекта

- `clients/composeApp/` - основное веб-приложение
- `.github/workflows/deploy.yml` - GitHub Actions workflow для деплоя
- Приложение собирается в Kotlin/JS и использует Compose Multiplatform

## Доступ к приложению

После успешного деплоя ваше приложение будет доступно по адресу:
`https://[ваш-username].github.io/[название-репозитория]/`
