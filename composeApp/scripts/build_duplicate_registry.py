#!/usr/bin/env python3
"""
Собирает реестр существующих вопросов из всех category/index.html для проверки дублей.
Результат: JSON с (category_slug, question_normalized, slug) — используется в generate_60_categories.py.

Запуск: python3 composeApp/scripts/build_duplicate_registry.py
"""
import json
import re
from pathlib import Path


RESOURCES = Path(__file__).parent.parent / "src" / "webMain" / "resources"


def normalize(text: str) -> str:
    """Нормализация: lowercase, удаление пунктуации, схлопывание пробелов."""
    s = text.lower().strip()
    s = re.sub(r"[^\w\s]", "", s)
    s = re.sub(r"\s+", " ", s)
    return s.strip()


def extract_qa_from_index(html: str) -> list[tuple[str, str]]:
    """
    Извлекает из index.html пары (slug, question).
    Ищет <div id="slug" class="qa-anchor"> и следующий <h2>question</h2>.
    """
    result = []
    pattern = r'<div\s+id="([^"]+)"\s+class="qa-anchor"\s*></div>\s*<article[^>]*>\s*<h2>([^<]+)</h2>'
    for m in re.finditer(pattern, html, re.DOTALL):
        slug, question = m.group(1), m.group(2).strip()
        question = re.sub(r"\s+", " ", question)
        result.append((slug, question))
    return result


def build_registry(resources: Path) -> list[dict]:
    """Собирает реестр из всех category/index.html."""
    registry = []
    for item in sorted(resources.iterdir()):
        if not item.is_dir() or item.name.startswith("."):
            continue
        index_path = item / "index.html"
        if not index_path.exists():
            continue
        html = index_path.read_text(encoding="utf-8")
        for slug, question in extract_qa_from_index(html):
            registry.append({
                "category": item.name,
                "question": question,
                "question_normalized": normalize(question),
                "slug": slug,
            })
    return registry


def main():
    registry = build_registry(RESOURCES)
    out_path = Path(__file__).parent / "duplicate_registry.json"
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(registry, f, ensure_ascii=False, indent=2)
    print(f"Written {len(registry)} entries to {out_path}")
    normalized = [r["question_normalized"] for r in registry]
    duplicates = [n for n in normalized if normalized.count(n) > 1]
    if duplicates:
        unique_dups = set(duplicates)
        print(f"Potential duplicates: {len(unique_dups)}")
        for n in sorted(unique_dups)[:10]:
            print(f"  - {n}")


if __name__ == "__main__":
    main()
