#!/usr/bin/env python3
"""
Генерирует 40 новых категорий (index.html) из categories_data.py с проверкой на дубли.
Загружает реестр существующих вопросов, проверяет новые по normalize + similarity > 0.85.

Запуск: python3 composeApp/scripts/generate_60_categories.py
"""
import json
import re
from pathlib import Path
from difflib import SequenceMatcher

RESOURCES = Path(__file__).parent.parent / "src" / "webMain" / "resources"


def normalize(text: str) -> str:
    s = text.lower().strip()
    s = re.sub(r"[^\w\s]", "", s)
    s = re.sub(r"\s+", " ", s)
    return s.strip()


def similarity(a: str, b: str) -> float:
    return SequenceMatcher(None, a, b).ratio()


def is_duplicate_question(question: str, registry_normalized: set, threshold: float = 0.85) -> bool:
    norm = normalize(question)
    if norm in registry_normalized:
        return True
    for existing in registry_normalized:
        if similarity(norm, existing) > threshold:
            return True
    return False


def load_registry() -> set:
    registry_path = Path(__file__).parent / "duplicate_registry.json"
    if not registry_path.exists():
        return set()
    with open(registry_path, encoding="utf-8") as f:
        data = json.load(f)
    return {r["question_normalized"] for r in data}


def build_faq_ldjson(articles: list) -> str:
    items = []
    for a in articles:
        text = a["question"] + " " + re.sub(r"<[^>]+>", " ", a["body"])
        text = text.replace("\\", "\\\\").replace('"', '\\"').replace("\n", " ")[:300]
        name = a["question"].replace("\\", "\\\\").replace('"', '\\"')
        items.append(
            f'''        {{
            "@type": "Question",
            "name": "{name}",
            "acceptedAnswer": {{
                "@type": "Answer",
                "text": "{text}"
            }}
        }}'''
        )
    return ",\n".join(items)


def generate_index_html(slug: str, name: str, intro: str, articles: list) -> str:
    cat_path = f"/{slug}/"
    faq_json = build_faq_ldjson(articles) if articles else ""
    toc_items = "\n".join(
        f'                <li><a href="#{a["slug"]}">{a["question"][:80]}{"…" if len(a["question"]) > 80 else ""}</a></li>'
        for a in articles
    )
    articles_html = []
    for a in articles:
        articles_html.append(
            f'''        <div id="{a["slug"]}" class="qa-anchor"></div>
        <article class="qa-block">
            <h2>{a["question"]}</h2>
            <p class="article-meta">{a["meta"]}</p>
{a["body"]}
        </article>'''
        )
    articles_section = "\n\n".join(articles_html)
    return f'''<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{name} — вопросы и ответы юристов | PravoChat</title>
    <meta name="description" content="{intro[:155]}">
    <meta name="keywords" content="{name}, юридические вопросы, ответы юристов, правовая консультация">
    <meta name="robots" content="index, follow">
    <meta property="og:type" content="website">
    <meta property="og:url" content="https://pravochat.ru{cat_path}">
    <meta property="og:title" content="{name} — вопросы и ответы | PravoChat">
    <meta property="og:description" content="{intro[:155]}">
    <meta property="og:image" content="https://pravochat.ru/images/practice-gavel.jpg">
    <meta property="og:site_name" content="PravoChat">
    <meta property="og:locale" content="ru_RU">
    <link rel="canonical" href="https://pravochat.ru{cat_path}">
    <link rel="icon" type="image/svg+xml" href="/favicon.svg">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/simple-header.css">
    <script type="text/javascript">
        (function(m,e,t,r,i,k,a){{
            m[i]=m[i]||function(){{(m[i].a=m[i].a||[]).push(arguments)}};
            m[i].l=1*new Date();
            for (var j = 0; j < document.scripts.length; j++) {{if (document.scripts[j].src === r) {{ return; }}}}
            k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)
        }})(window, document,'script','https://mc.yandex.ru/metrika/tag.js?id=104954778', 'ym');
        ym(104954778, 'init', {{webvisor:true, clickmap:true, ecommerce:"dataLayer", accurateTrackBounce:true, trackLinks:true}});
    </script>
    <noscript><div><img src="https://mc.yandex.ru/watch/104954778" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
    <script type="application/ld+json">
    {{
        "@context": "https://schema.org",
        "@type": "FAQPage",
        "mainEntity": [
{faq_json}
        ]
    }}
    </script>
    <style>
        :root {{ color-scheme: light; }}
        * {{ box-sizing: border-box; }}
        body {{ margin: 0; padding-top: 70px; font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #F5F7FB; color: #111322; }}
        .container {{ max-width: 860px; margin: 0 auto; padding: 40px 24px 80px; }}
        h1 {{ font-size: 32px; margin-bottom: 16px; }}
        h2 {{ font-size: 24px; margin-top: 40px; margin-bottom: 12px; color: #111322; }}
        h2:first-of-type {{ margin-top: 0; }}
        .intro {{ font-size: 18px; line-height: 1.7; color: #2F3347; margin-bottom: 32px; }}
        .qa-anchor {{ display: block; scroll-margin-top: 80px; }}
        .qa-block {{ margin-bottom: 48px; padding-bottom: 32px; border-bottom: 1px solid #E6E9F0; }}
        .qa-block:last-of-type {{ border-bottom: none; }}
        .article-meta {{ font-size: 14px; color: #6B7080; margin-bottom: 16px; }}
        .qa-block p {{ font-size: 18px; line-height: 1.8; color: #2F3347; margin-bottom: 16px; }}
        .qa-block ul, .qa-block ol {{ font-size: 18px; line-height: 1.8; color: #2F3347; margin-bottom: 16px; padding-left: 24px; }}
        .qa-block li {{ margin-bottom: 8px; }}
        .toc {{ background: #fff; border: 1px solid #E6E9F0; border-radius: 12px; padding: 24px; margin-bottom: 32px; }}
        .toc h3 {{ margin: 0 0 16px 0; font-size: 18px; }}
        .toc ul {{ list-style: none; padding: 0; margin: 0; }}
        .toc a {{ color: #0B5ED7; text-decoration: none; font-weight: 500; }}
        .toc a:hover {{ text-decoration: underline; }}
    </style>
</head>
<body>
    <header class="simple-header">
        <a class="brand" href="/">
            <img src="/images/pravo-logo.svg" width="36" height="36" alt="Ваш Юрист логотип">
            Ваш Юрист
        </a>
        <nav class="nav-links">
            <a href="/">Главная</a>
            <a href="{cat_path}">{name}</a>
        </nav>
    </header>
    <main class="container">
        <h1>{name}</h1>
        <p class="intro">{intro}</p>
        <nav class="toc" aria-label="Содержание">
            <h3>Вопросы и ответы</h3>
            <ul>
{toc_items}
            </ul>
        </nav>

{articles_section}
    </main>
    <a href="/" class="consult-lawyer-btn">Обсудить с юристом</a>
</body>
</html>
'''


def main():
    import sys
    project_root = Path(__file__).resolve().parent.parent.parent
    if str(project_root) not in sys.path:
        sys.path.insert(0, str(project_root))
    from composeApp.scripts.build_duplicate_registry import build_registry, normalize as reg_normalize
    from composeApp.scripts.categories_data import NEW_CATEGORIES

    try:
        from composeApp.scripts.categories_20_extras import EXTRA_ARTICLES
        for cat in NEW_CATEGORIES:
            cat["articles"] = list(cat["articles"]) + EXTRA_ARTICLES.get(cat["slug"], [])
    except ImportError:
        pass

    build_registry(RESOURCES)
    registry_path = Path(__file__).parent / "duplicate_registry.json"
    with open(registry_path, encoding="utf-8") as f:
        registry_data = json.load(f)
    registry_normalized = {r["question_normalized"] for r in registry_data}

    seen_in_session = set(registry_normalized)
    duplicates_found = []

    for cat in NEW_CATEGORIES:
        slug = cat["slug"]
        name = cat["name"]
        intro = cat["intro"]
        articles = list(cat["articles"])
        filtered = []
        for a in articles:
            q = a["question"]
            norm = reg_normalize(q)
            if norm in seen_in_session:
                duplicates_found.append((slug, q))
                continue
            dup = False
            for existing in seen_in_session:
                if SequenceMatcher(None, norm, existing).ratio() > 0.85:
                    duplicates_found.append((slug, q))
                    dup = True
                    break
            if not dup:
                filtered.append(a)
                seen_in_session.add(norm)

        if len(filtered) < 16:
            print(f"WARNING: {slug} has only {len(filtered)} unique questions (duplicates skipped)")

        cat_dir = RESOURCES / slug
        cat_dir.mkdir(parents=True, exist_ok=True)
        html = generate_index_html(slug, name, intro, filtered)
        (cat_dir / "index.html").write_text(html, encoding="utf-8")
        print(f"Wrote {slug}/index.html ({len(filtered)} articles)")

    if duplicates_found:
        print(f"\nSkipped {len(duplicates_found)} duplicate questions")
        for s, q in duplicates_found[:5]:
            print(f"  [{s}] {q[:60]}...")


if __name__ == "__main__":
    main()
