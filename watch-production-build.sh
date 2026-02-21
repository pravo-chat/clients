#!/usr/bin/env bash
set -euo pipefail

# Автосборка production-дистрибутива при изменениях в resources.
# Использование:
#   cd clients
#   ./watch-production-build.sh
#
# По умолчанию следим за composeApp/src/webMain/resources
# и запускаем :composeApp:jsBrowserDistribution с debounce.

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WATCH_DIR="${WATCH_DIR:-$ROOT_DIR/composeApp/src/webMain/resources}"
DEBOUNCE_SEC="${DEBOUNCE_SEC:-2}"
GRADLE_TASK="${GRADLE_TASK:-:composeApp:jsBrowserDistribution}"

echo "[watch] dir: $WATCH_DIR"
echo "[watch] task: ./gradlew $GRADLE_TASK"
echo "[watch] debounce: ${DEBOUNCE_SEC}s"

run_build() {
  echo "[build] $(date +'%H:%M:%S') starting..."
  (cd "$ROOT_DIR" && ./gradlew "$GRADLE_TASK" --no-daemon)
  echo "[build] $(date +'%H:%M:%S') done."
}

if command -v inotifywait >/dev/null 2>&1; then
  echo "[watch] using inotifywait"
  run_build

  while true; do
    # Ждём любого изменения и даём “затухнуть” серии сохранений
    inotifywait -r -e modify,create,delete,move "$WATCH_DIR" >/dev/null 2>&1 || true
    sleep "$DEBOUNCE_SEC"
    # добираем изменения, если редактор делает несколько операций подряд
    while inotifywait -t 0.2 -r -e modify,create,delete,move "$WATCH_DIR" >/dev/null 2>&1; do
      sleep "$DEBOUNCE_SEC"
    done
    run_build
  done
else
  echo "[watch] inotifywait not found, using polling fallback (slower)"
  run_build

  last_hash=""
  while true; do
    # Хеш по mtime+size: дешево и достаточно для ресурсов.
    hash="$(find "$WATCH_DIR" -type f -printf '%T@ %s %p\n' 2>/dev/null | sha256sum | awk '{print $1}')"
    if [[ "$hash" != "$last_hash" ]]; then
      last_hash="$hash"
      run_build
    fi
    sleep 2
  done
fi





