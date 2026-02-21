#!/usr/bin/env python3
"""
Объединяет статьи по каждой категории в одну страницу index.html.
Создаёт редиректы для старых URL в _redirects.

Запуск: python3 composeApp/scripts/merge_articles_to_category.py
"""
import os
import re
from pathlib import Path


RESOURCES = Path(__file__).parent.parent / "src" / "webMain" / "resources"
CATEGORY_NAMES = {
    "administrative-offenses": "Административные правонарушения",
    "apartment-check-sale": "Проверка квартиры / продажа",
    "appeal-cassation": "Апелляция и кассация",
    "auto-loan": "Автокредит",
    "business-trips": "Командировки",
    "cadastral-registration": "Кадастровый учёт",
    "child-support": "Алименты на ребёнка",
    "compensations-benefits": "Компенсации и льготы",
    "consumer-rights-protection": "Защита прав потребителей",
    "contract-work": "Договор подряда",
    "copyright-law": "Авторское право",
    "credit-agreement": "Кредитный договор",
    "credit-holidays": "Кредитные каникулы",
    "criminal-lawyer": "Уголовный адвокат",
    "debt-collection": "Коллекторы и взыскание долгов",
    "developer-disputes": "Споры с застройщиками",
    "disability-rights": "Права инвалидов",
    "dolevoe-stroitelstvo": "Долевое строительство",
    "employment-contract": "Трудовой договор",
    "enforcement-proceedings": "Исполнительное производство",
    "family-lawyer-divorce-alimony": "Семейный юрист (развод, алименты)",
    "gift-agreement": "Договор дарения",
    "goods-return": "Возврат товара",
    "free-legal-consultation": "Бесплатная юридическая консультация",
    "housing-disputes": "Жилищные споры",
    "housing-privatization": "Приватизация жилья",
    "inheritance-processing": "Оформление наследства",
    "inheritance-without-will": "Наследство без завещания",
    "insurance-disputes": "Страховые споры (КАСКО, ОСАГО)",
    "ip-registration": "Регистрация ИП",
    "labor-disputes": "Трудовые споры",
    "land-disputes": "Земельные споры",
    "lease-agreement": "Договор аренды недвижимости",
    "maternity-payments": "Декретные выплаты",
    "medical-disputes": "Медицинские споры",
    "mediation": "Медиация",
    "microfinance": "Микрозаймы и МФО",
    "migration-law": "Миграционное право / РВП и ВНЖ",
    "military-mortgage": "Военная ипотека",
    "moral-damage-compensation": "Возмещение морального вреда",
    "notary-powers-of-attorney": "Нотариальные доверенности",
    "online-lawyer": "Юрист онлайн",
    "pension-disputes": "Пенсионные споры",
    "personal-bankruptcy": "Банкротство физических лиц",
    "personal-data-protection": "Защита персональных данных",
    "pledge-guarantee": "Залог и поручительство",
    "property-division-divorce": "Раздел имущества при разводе",
    "property-division-mortgage": "Раздел квартиры / ипотека",
    "redevelopment": "Перепланировка квартиры",
    "registration-residence": "Регистрация по месту жительства",
    "remote-work": "Удалённая работа",
    "rent-agreement": "Договор ренты",
    "sale-purchase-agreement": "Договор купли-продажи",
    "self-employment": "Самозанятость",
    "service-agreement": "Договор оказания услуг",
    "shareholder-rights": "Защита прав дольщиков",
    "taxes-consultation": "Налоги и консультации",
    "traffic-accident-osago": "ДТП и ОСАГО",
    "transaction-contest": "Оспаривание сделок",
    "veteran-benefits": "Льготы ветеранам",
}


def parse_article(html: str, slug: str) -> dict | None:
    """Извлекает из HTML статьи: вопрос, мета, тело ответа."""
    main_match = re.search(r"<main[^>]*class=[\"']container[\"'][^>]*>(.*?)</main>", html, re.DOTALL | re.IGNORECASE)
    if not main_match:
        return None
    main_content = main_match.group(1)

    h1_match = re.search(r"<h1>(.*?)</h1>", main_content, re.DOTALL)
    meta_match = re.search(r'<p[^>]*class=[^>]*article-meta[^>]*>(.*?)</p>', main_content, re.DOTALL)
    cat_match = re.search(r'<p[^>]*class=[^>]*article-category', main_content)

    if not h1_match:
        return None
    question = h1_match.group(1).strip()
    question = re.sub(r"\s+", " ", question)

    meta = meta_match.group(1).strip() if meta_match else ""
    meta = re.sub(r"\s+", " ", meta)

    body_start = meta_match.end() if meta_match else h1_match.end()
    body_end = cat_match.start() if cat_match else len(main_content)
    body = main_content[body_start:body_end].strip()
    body = re.sub(r"\n\s*\n\s*\n", "\n\n", body)

    return {"slug": slug, "question": question, "meta": meta, "body": body}


def discover_categories(resources: Path) -> list[str]:
    """Возвращает список папок категорий (содержат index.html или статьи)."""
    categories = []
    for item in resources.iterdir():
        if item.is_dir() and not item.name.startswith("."):
            has_index = (item / "index.html").exists()
            articles = [f for f in item.iterdir() if f.suffix == ".html" and f.name != "index.html"]
            if has_index or articles:
                categories.append(item.name)
    return sorted(categories)


def collect_articles(category_path: Path) -> list[dict]:
    """Собирает все статьи в категории."""
    articles = []
    for f in sorted(category_path.iterdir()):
        if f.suffix != ".html" or f.name == "index.html":
            continue
        slug = f.stem
        html = f.read_text(encoding="utf-8")
        data = parse_article(html, slug)
        if data:
            articles.append(data)
    return articles


def _escape_json(s: str) -> str:
    return s.replace("\\", "\\\\").replace('"', '\\"').replace("\n", " ").replace("\r", " ")


def build_faq_ldjson(articles: list[dict]) -> str:
    """Формирует JSON-LD FAQPage."""
    items = []
    for a in articles:
        text = a["question"] + " " + re.sub(r"<[^>]+>", " ", a["body"])
        text = _escape_json(text[:300])
        name = _escape_json(a["question"])
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


def generate_index_html(
    category_slug: str,
    category_name: str,
    articles: list[dict],
    intro: str,
) -> str:
    """Генерирует HTML объединённой страницы."""
    cat_path = f"/{category_slug}/"
    faq_json = build_faq_ldjson(articles) if articles else ""

    nav_link = f'<a href="/">Главная</a><a href="{cat_path}">{category_name}</a>'

    articles_html = []
    for a in articles:
        articles_html.append(
            f'''        <hr id="{a["slug"]}" class="qa-divider" aria-hidden="true">
        <article class="qa-block">
            <h2>{a["question"]}</h2>
            <p class="article-meta">{a["meta"]}</p>
{a["body"]}
        </article>'''
        )

    articles_section = "\n\n".join(articles_html) if articles else "<p>В этой категории пока нет статей.</p>"

    return f'''<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{category_name} — вопросы и ответы юристов | PravoChat</title>
    <meta name="description" content="{intro[:155]}">
    <meta name="keywords" content="{category_name}, юридические вопросы, ответы юристов, правовая консультация">
    <meta name="robots" content="index, follow">
    <meta property="og:type" content="website">
    <meta property="og:url" content="https://pravochat.ru{cat_path}">
    <meta property="og:title" content="{category_name} — вопросы и ответы | PravoChat">
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
        .qa-divider {{ border: none; border-top: 1px solid #E6E9F0; margin: 40px 0 24px 0; padding: 0; height: 0; }}
        .qa-divider:first-of-type {{ margin-top: 0; }}
        .qa-block {{ margin-bottom: 0; padding-bottom: 0; border-bottom: none; }}
        .qa-block:last-child {{ border-bottom: none; }}
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
            <a href="{cat_path}">{category_name}</a>
        </nav>
    </header>
    <main class="container">
        <h1>{category_name}</h1>
        <p class="intro">{intro}</p>
''' + (
        f'''        <nav class="toc" aria-label="Содержание">
            <h3>Вопросы и ответы</h3>
            <ul>
''' + "\n".join(f'                <li><a href="#{a["slug"]}">{a["question"][:80]}{"…" if len(a["question"]) > 80 else ""}</a></li>' for a in articles) + """
            </ul>
        </nav>

""" if articles else "\n"
    ) + f'''
        {articles_section}
    </main>
    <a href="/" class="consult-lawyer-btn">Обсудить с юристом</a>
</body>
</html>
'''


INTROS = {
    "Бесплатная юридическая консультация": "Ответы юристов на типичные правовые вопросы: заброшенное имущество, права арендатора и арендодателя, срок исковой давности, судебные процедуры и бесплатная юридическая помощь.",
    "Юрист онлайн": "Консультации юриста через интернет, проверка документов онлайн, защита от мошенников и порядок действий при повреждении автомобиля.",
    "Семейный юрист (развод, алименты)": "Развод, алименты, споры о детях, брачные договоры и порядок расторжения брака.",
    "Раздел имущества при разводе": "Раздел квартиры, бизнеса, вкладов и другого совместно нажитого имущества при разводе.",
    "Оформление наследства": "Вступление в наследство, сроки, документы, споры между наследниками и оформление прав у нотариуса.",
    "Жилищные споры": "Регистрация, выселение, коммунальные споры, споры с управляющими компаниями и соседями.",
    "Договор купли-продажи": "Подготовка и проверка договоров купли-продажи, риски и способы их минимизации.",
    "Проверка квартиры / продажа": "Юридическая проверка квартиры перед покупкой и сопровождение сделок с недвижимостью.",
    "Защита прав потребителей": "Возврат товара, некачественные услуги, претензии к магазинам и сервисам.",
    "Трудовые споры": "Увольнение, невыплата зарплаты, переработки и споры с работодателем.",
    "Банкротство физических лиц": "Процедура банкротства граждан, списание долгов и защита имущества.",
    "Административные правонарушения": "Штрафы, ГИБДД, административные протоколы и обжалование постановлений.",
    "Уголовный адвокат": "Защита по уголовным делам, допросы, задержание и избрание меры пресечения.",
    "Нотариальные доверенности": "Оформление и отзыв доверенностей, проверка полномочий и рисков.",
    "Алименты на ребёнка": "Назначение, изменение и взыскание алиментов.",
    "Раздел квартиры / ипотека": "Раздел ипотечной квартиры, ответственность по кредиту и взаимодействие с банком.",
    "Земельные споры": "Аренда и выкуп земли, границы участков и споры с муниципалитетом.",
    "Наследство без завещания": "Наследование по закону, очередность наследников и споры при отсутствии завещания.",
    "Налоги и консультации": "Налоговые споры, оптимизация нагрузки и консультации по обязанностям.",
    "Компенсации и льготы": "Социальные выплаты, льготы и порядок их оформления.",
}


def main() -> None:
    resources = RESOURCES
    if not resources.exists():
        raise SystemExit(f"Resources dir not found: {resources}")

    categories = discover_categories(resources)
    redirects = ["# Редиректы для GitHub Pages", "/index / 301", "/index.html / 301", ""]

    for cat_slug in categories:
        cat_path = resources / cat_slug
        if not cat_path.is_dir():
            continue
        articles = collect_articles(cat_path)
        cat_name = CATEGORY_NAMES.get(cat_slug, cat_slug.replace("-", " ").title())
        intro = INTROS.get(cat_name, f"Вопросы и ответы юристов по теме «{cat_name}».")

        html = generate_index_html(cat_slug, cat_name, articles, intro)
        index_path = cat_path / "index.html"
        index_path.write_text(html, encoding="utf-8")
        print(f"Wrote {index_path.relative_to(resources)} ({len(articles)} articles)")

        for a in articles:
            old_url = f"/{cat_slug}/{a['slug']}.html"
            new_url = f"/{cat_slug}/#{a['slug']}"
            redirects.append(f"{old_url} {new_url} 301")

    redirects_path = resources / "_redirects"
    redirects_path.write_text("\n".join(redirects), encoding="utf-8")
    print(f"\nWrote {redirects_path.relative_to(resources)} ({len(redirects) - 4} redirects)")


if __name__ == "__main__":
    main()
