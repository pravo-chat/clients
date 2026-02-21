#!/usr/bin/env python3
"""Adds BreadcrumbList JSON-LD to category index.html files."""
import json
import re
from pathlib import Path

RESOURCES = Path(__file__).parent.parent / "src" / "webMain" / "resources"


def get_category_name_from_title(html: str) -> str | None:
    m = re.search(r"<title>([^—]+)", html)
    return m.group(1).strip() if m else None


def add_breadcrumb_to_file(file_path: Path) -> bool:
    slug = file_path.parent.name
    html = file_path.read_text(encoding="utf-8")
    if "BreadcrumbList" in html:
        return False
    name = get_category_name_from_title(html)
    if not name:
        return False
    cat_path = f"/{slug}/"
    name_quoted = json.dumps(name)
    breadcrumb = f"""    <script type="application/ld+json">
    {{
        "@context": "https://schema.org",
        "@type": "BreadcrumbList",
        "itemListElement": [
            {{"@type": "ListItem", "position": 1, "name": "Главная", "item": "https://pravochat.ru/"}},
            {{"@type": "ListItem", "position": 2, "name": "Юридические вопросы и ответы", "item": "https://pravochat.ru/legal-questions-answers.html"}},
            {{"@type": "ListItem", "position": 3, "name": {name_quoted}, "item": "https://pravochat.ru{cat_path}"}}
        ]
    }}
    </script>
"""
    old = "    </script>\n    <style>"
    new = "    </script>\n" + breadcrumb + "    <style>"
    if old in html:
        html = html.replace(old, new, 1)
        file_path.write_text(html, encoding="utf-8")
        return True
    return False


def main() -> None:
    count = 0
    for dir_path in sorted(RESOURCES.iterdir()):
        if not dir_path.is_dir():
            continue
        index_path = dir_path / "index.html"
        if not index_path.exists():
            continue
        if add_breadcrumb_to_file(index_path):
            count += 1
            print(f"Added BreadcrumbList to {dir_path.name}/")
    print(f"Updated {count} category pages.")


if __name__ == "__main__":
    main()
