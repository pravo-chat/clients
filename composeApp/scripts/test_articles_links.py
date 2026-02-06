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
    """Извлекает все ссылки на HTML файлы из содержимого HTML."""
    links = set()
    # Ищем все href="..." с .html
    pattern = r'href="([^"]+\.html)"'
    matches = re.findall(pattern, html_content)
    
    for link in matches:
        # Игнорируем полные URL и ссылки на главную страницу
        if link.startswith("/") and not link.startswith("https://") and link != "/":
            links.add(link)
    
    return links

def check_article_files(resources_dir: Path) -> Tuple[List[str], List[str]]:
    """Проверяет наличие всех файлов статей."""
    legal_questions_file = resources_dir / "legal-questions-answers.html"
    
    if not legal_questions_file.exists():
        return [], [f"Файл {legal_questions_file} не найден"]
    
    with open(legal_questions_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    article_links = extract_article_links(content)
    missing_files = []
    
    for link in sorted(article_links):
        file_path = link.lstrip("/")
        full_path = resources_dir / file_path
        
        if not full_path.exists():
            missing_files.append(file_path)
    
    return sorted(article_links), missing_files

def check_category_index_files(resources_dir: Path) -> Tuple[List[str], List[str]]:
    """Проверяет наличие всех index.html файлов категорий."""
    categories = [
        "administrative-offenses",
        "land-disputes",
        "child-support",
        "compensations-benefits",
        "criminal-lawyer",
        "family-lawyer-divorce-alimony",
        "free-legal-consultation",
        "housing-disputes",
        "inheritance-processing",
        "inheritance-without-will",
        "labor-disputes",
        "notary-powers-of-attorney",
        "online-lawyer",
        "personal-bankruptcy",
        "property-division-mortgage",
        "sale-purchase-agreement",
        "taxes-consultation",
        "property-division-divorce",
        "apartment-check-sale"
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
