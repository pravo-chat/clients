#!/usr/bin/env python3
"""Generate sitemap.xml with all public HTML pages."""
import os
from datetime import date

BASE_URL = "https://pravochat.ru"
RESOURCES = os.path.dirname(os.path.abspath(__file__))
SKIP = {"404.html", "index-redirect.html"}
SKIP_CONTAINS = ("google", "yandex")

def path_to_url(relpath):
    if relpath == "index.html":
        return BASE_URL + "/"
    if relpath.endswith("/index.html"):
        path_stem = relpath[:-11]
        return BASE_URL + "/" + path_stem + "/"
    return BASE_URL + "/" + relpath

def priority_changefreq(relpath):
    if relpath == "index.html":
        return "1.0", "daily"
    if relpath.endswith("/index.html"):
        return "0.8", "weekly"
    return "0.6", "monthly"

def main():
    today = date.today().isoformat()
    entries = []
    for dirpath, _, filenames in os.walk(RESOURCES):
        for f in filenames:
            if not f.endswith(".html"):
                continue
            rel = os.path.join(dirpath, f).replace(RESOURCES, "").lstrip("/").replace("\\", "/")
            if rel in SKIP:
                continue
            if any(s in rel for s in SKIP_CONTAINS):
                continue
            url = path_to_url(rel)
            pri, chf = priority_changefreq(rel)
            entries.append((url, today, chf, pri))
    entries.sort(key=lambda x: x[0])

    out = ['<?xml version="1.0" encoding="UTF-8"?>']
    out.append('<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"')
    out.append('        xmlns:xhtml="http://www.w3.org/1999/xhtml"')
    out.append('        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"')
    out.append('        xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9')
    out.append('        http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">')
    for url, lastmod, changefreq, priority in entries:
        out.append("    <url>")
        out.append(f"        <loc>{url}</loc>")
        out.append(f"        <lastmod>{lastmod}</lastmod>")
        out.append(f"        <changefreq>{changefreq}</changefreq>")
        out.append(f"        <priority>{priority}</priority>")
        if url == BASE_URL + "/":
            out.append('        <xhtml:link rel="alternate" hreflang="ru" href="' + url + '"/>')
        out.append("    </url>")
    out.append("</urlset>")

    sitemap_path = os.path.join(RESOURCES, "sitemap.xml")
    with open(sitemap_path, "w", encoding="utf-8") as f:
        f.write("\n".join(out) + "\n")
    print(f"Wrote {sitemap_path} with {len(entries)} URLs")

if __name__ == "__main__":
    main()
