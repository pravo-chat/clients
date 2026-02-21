#!/usr/bin/env python3
"""
Тест для проверки наличия всех файлов статей и корректности ссылок.
Можно запускать как часть CI/CD или локально.
"""
import os
import re
import sys
from pathlib import Path
from typing import List, Set, Tuple

def extract_article_links(html_content: str) -> Set[str]:
    """Извлекает ссылки на статьи: /category/#slug (после объединения) или /category/slug.html."""
    links = set()
    pattern = r'href="(/(?:[^/"]+/)#([^"]+))"'
    for m in re.finditer(pattern, html_content):
        links.add(m.group(1))
    pattern_legacy = r'href="([^"]+\.html)"'
    for m in re.finditer(pattern_legacy, html_content):
        link = m.group(1)
        if link.startswith("/") and "legal-questions" not in link and "about" not in link:
            links.add(link)
    return links


def check_article_files(resources_dir: Path) -> Tuple[List[str], List[str]]:
    """Проверяет наличие страниц категорий для ссылок вида /category/#slug."""
    legal_questions_file = resources_dir / "legal-questions-answers.html"
    if not legal_questions_file.exists():
        return [], [f"Файл {legal_questions_file} не найден"]

    with open(legal_questions_file, "r", encoding="utf-8") as f:
        content = f.read()

    article_links = extract_article_links(content)
    missing_files = []

    for link in sorted(article_links):
        if "#" in link:
            category = link.split("#")[0].rstrip("/")
            index_path = resources_dir / category.lstrip("/") / "index.html"
            if not index_path.exists():
                missing_files.append(f"{category}/index.html")
        else:
            file_path = link.lstrip("/")
            if not (resources_dir / file_path).exists():
                missing_files.append(file_path)

    return sorted(article_links), missing_files

def check_category_index_files(resources_dir: Path) -> Tuple[List[str], List[str]]:
    """Проверяет наличие всех index.html файлов категорий."""
    categories = [
        "administrative-offenses",
        "apartment-check-sale",
        "child-support",
        "compensations-benefits",
        "consumer-rights-protection",
        "criminal-lawyer",
        "family-lawyer-divorce-alimony",
        "free-legal-consultation",
        "housing-disputes",
        "inheritance",
        "inheritance-processing",
        "inheritance-without-will",
        "labor-disputes",
        "land-disputes",
        "notary-powers-of-attorney",
        "online-lawyer",
        "personal-bankruptcy",
        "property-division-divorce",
        "property-division-mortgage",
        "sale-purchase-agreement",
        "taxes-consultation",
    ]
    
    missing_indexes = []
    
    for category in categories:
        index_path = resources_dir / category / "index.html"
        if not index_path.exists():
            missing_indexes.append(f"{category}/index.html")
    
    return categories, missing_indexes

def main():
    """Основная функция теста."""
    # Определяем путь к resources
    script_dir = Path(__file__).parent
    resources_dir = script_dir.parent / "src" / "webMain" / "resources"
    
    if not resources_dir.exists():
        print(f"❌ Директория {resources_dir} не найдена")
        sys.exit(1)
    
    print("🔍 Проверка файлов статей и ссылок...")
    print("=" * 60)
    
    # Проверка файлов статей
    print("\n1. Проверка файлов статей...")
    article_links, missing_files = check_article_files(resources_dir)
    print(f"   Найдено ссылок: {len(article_links)}")
    
    if missing_files:
        print(f"   ❌ Отсутствующие файлы ({len(missing_files)}):")
        for file in missing_files[:10]:  # Показываем первые 10
            print(f"      - {file}")
        if len(missing_files) > 10:
            print(f"      ... и ещё {len(missing_files) - 10} файлов")
    else:
        print("   ✅ Все файлы статей существуют")
    
    # Проверка index.html файлов категорий
    print("\n2. Проверка index.html файлов категорий...")
    categories, missing_indexes = check_category_index_files(resources_dir)
    print(f"   Проверено категорий: {len(categories)}")
    
    if missing_indexes:
        print(f"   ❌ Отсутствующие index.html файлы ({len(missing_indexes)}):")
        for index_file in missing_indexes:
            print(f"      - {index_file}")
    else:
        print("   ✅ Все index.html файлы категорий существуют")
    
    # Итоговый результат
    print("\n" + "=" * 60)
    total_errors = len(missing_files) + len(missing_indexes)
    
    if total_errors == 0:
        print("✅ Все проверки пройдены успешно!")
        sys.exit(0)
    else:
        print(f"❌ Найдено ошибок: {total_errors}")
        print(f"   - Отсутствующих файлов статей: {len(missing_files)}")
        print(f"   - Отсутствующих index.html: {len(missing_indexes)}")
        sys.exit(1)

if __name__ == "__main__":
    main()
