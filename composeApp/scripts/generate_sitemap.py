#!/usr/bin/env python3
from __future__ import annotations

import datetime
from pathlib import Path

DIST_PATH = Path("composeApp/build/dist/js/productionExecutable")
EXCLUDED_PATHS = {
    "404.html",
    "index-redirect.html",
    "legal-questions.html",
    "redirect-template.html",
    "index/index.html",
}


def collect_urls(dist: Path) -> list[tuple[str, str]]:
    html_files = sorted(dist.rglob("*.html"))
    result: list[tuple[str, str]] = []
    for file_path in html_files:
        relative = file_path.relative_to(dist)
        relative_str = relative.as_posix()
        if relative_str in EXCLUDED_PATHS:
            continue
        if relative_str.startswith("."):
            continue

        if relative_str.endswith("index.html"):
            url_suffix = relative_str[: -len("index.html")]
        else:
            url_suffix = relative_str

        if url_suffix == "":
            url_path = "/"
        else:
            url_path = "/" + url_suffix

        if not url_path.endswith("/") and url_suffix.endswith("/"):
            url_path += "/"

        lastmod = datetime.datetime.utcfromtimestamp(file_path.stat().st_mtime).date().isoformat()
        result.append((url_path, lastmod))
    return result


def write_sitemap(dist: Path, entries: list[tuple[str, str]]) -> None:
    output = dist / "sitemap.xml"
    header = """<?xml version="1.0" encoding="UTF-8"?>\n"""
    header += (
        '<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"\n'
        '        xmlns:xhtml="http://www.w3.org/1999/xhtml"\n'
        '        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n'
        '        xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9\n'
        '        http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">\n'
    )
    lines = [header]
    for url_path, lastmod in entries:
        lines.extend(
            [
                "    <url>",
                f"        <loc>https://pravochat.ru{url_path}</loc>",
                f"        <lastmod>{lastmod}</lastmod>",
                "        <changefreq>weekly</changefreq>",
                "        <priority>0.7</priority>",
                "    </url>",
            ]
        )
    lines.append("</urlset>\n")
    output.write_text("\n".join(lines), encoding="utf-8")


def main() -> None:
    if not DIST_PATH.exists():
        raise SystemExit(f"Distribution path not found: {DIST_PATH}")
    entries = collect_urls(DIST_PATH)
    write_sitemap(DIST_PATH, entries)


if __name__ == "__main__":
    main()
