#!/usr/bin/env python3
from __future__ import annotations

import datetime
from pathlib import Path

DIST_PATH = Path("composeApp/build/dist/js/productionExecutable")
REDIRECTS_PATH = Path("composeApp/src/webMain/resources/_redirects")
EXCLUDED_PATHS = {
    "404.html",
    "index-redirect.html",
    "redirect-template.html",
    "index/index.html",
}
EXCLUDED_PREFIXES = ("google", "yandex_")

PRIORITY_HOMEPAGE = 1.0
PRIORITY_MAIN = 0.9
PRIORITY_CATEGORY = 0.7
MAIN_PAGES = {"/", "/about.html", "/practice.html", "/consult.html", "/premium.html", "/legal-questions-answers.html"}


def load_redirect_sources(redirects_path: Path) -> set[str]:
    if not redirects_path.exists():
        return set()
    sources: set[str] = set()
    for line in redirects_path.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        parts = line.split()
        if len(parts) >= 2 and parts[-1] == "301":
            src = parts[0]
            if src and src.startswith("/"):
                sources.add(src)
    sources.add("/inheritance")
    sources.add("/inheritance/")
    return sources


def collect_urls(dist: Path, redirect_sources: set[str]) -> list[tuple[str, str, float]]:
    html_files = sorted(dist.rglob("*.html"))
    result: list[tuple[str, str, float]] = []
    for file_path in html_files:
        relative = file_path.relative_to(dist)
        relative_str = relative.as_posix()
        if relative_str in EXCLUDED_PATHS:
            continue
        if relative_str.startswith("."):
            continue
        base_name = Path(relative_str).stem
        if base_name.startswith(EXCLUDED_PREFIXES):
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

        path_normalized = url_path.rstrip("/") or "/"
        if url_path in redirect_sources or path_normalized in redirect_sources:
            continue

        lastmod = datetime.datetime.utcfromtimestamp(file_path.stat().st_mtime).date().isoformat()
        if url_path in MAIN_PAGES or path_normalized in {p.rstrip("/") or "/" for p in MAIN_PAGES}:
            priority = PRIORITY_MAIN if url_path != "/" else PRIORITY_HOMEPAGE
        else:
            priority = PRIORITY_CATEGORY
        result.append((url_path, lastmod, priority))
    return result


def write_sitemap(dist: Path, entries: list[tuple[str, str, float]]) -> None:
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
    for url_path, lastmod, priority in entries:
        lines.extend(
            [
                "    <url>",
                f"        <loc>https://pravochat.ru{url_path}</loc>",
                f"        <lastmod>{lastmod}</lastmod>",
                f"        <changefreq>{'daily' if url_path == '/' else 'weekly'}</changefreq>",
                f"        <priority>{priority:.1f}</priority>",
                "    </url>",
            ]
        )
    lines.append("</urlset>\n")
    output.write_text("\n".join(lines), encoding="utf-8")


def main() -> None:
    if not DIST_PATH.exists():
        raise SystemExit(f"Distribution path not found: {DIST_PATH}")
    redirect_sources = load_redirect_sources(Path(__file__).resolve().parent.parent / "src" / "webMain" / "resources" / "_redirects")
    entries = collect_urls(DIST_PATH, redirect_sources)
    write_sitemap(DIST_PATH, entries)


if __name__ == "__main__":
    main()
