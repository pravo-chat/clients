#!/usr/bin/env python3
"""Add og:description and og:image to article HTML files that have description and og:title but lack og:image."""
import os
import re

RESOURCES = os.path.dirname(os.path.abspath(__file__))
OG_IMAGE = "https://pravochat.ru/images/practice-gavel.jpg"
SKIP_FILES = {"404.html", "index-redirect.html"}

def add_og_to_file(filepath):
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
    if "og:image" in content:
        return False
    if "og:title" not in content:
        return False
    m = re.search(r'<meta name="description" content="([^"]*)"', content)
    if not m:
        return False
    description = m.group(1).replace("&", "&amp;").replace('"', "&quot;").replace("<", "&lt;")
    m_title = re.search(r'(<meta property="og:title" content="[^"]*"\s*>)', content)
    if not m_title:
        return False
    insert_after = m_title.group(1)
    new_meta = (
        '\n<meta property="og:description" content="' + description + '">'
        '\n<meta property="og:image" content="' + OG_IMAGE + '">'
    )
    new_content = content.replace(insert_after, insert_after + new_meta, 1)
    if new_content == content:
        return False
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(new_content)
    return True

def main():
    count = 0
    for dirpath, _, filenames in os.walk(RESOURCES):
        for f in filenames:
            if not f.endswith(".html") or f in SKIP_FILES or "google" in f or "yandex" in f:
                continue
            path = os.path.join(dirpath, f)
            if add_og_to_file(path):
                count += 1
                print("Updated:", os.path.relpath(path, RESOURCES))
    print(f"Done. Updated {count} files.")

if __name__ == "__main__":
    main()
