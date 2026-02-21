#!/usr/bin/env python3
"""Извлекает Q&A из index.html 20 оригинальных категорий. Результат — для добавления в categories_data."""
import re
from pathlib import Path

RESOURCES = Path(__file__).parent.parent / "src" / "webMain" / "resources"
ORIGINAL_SLUGS = [
    "administrative-offenses", "apartment-check-sale", "child-support", "compensations-benefits",
    "consumer-rights-protection", "criminal-lawyer", "family-lawyer-divorce-alimony",
    "free-legal-consultation", "housing-disputes", "inheritance-processing", "inheritance-without-will",
    "labor-disputes", "land-disputes", "notary-powers-of-attorney", "online-lawyer",
    "personal-bankruptcy", "property-division-divorce", "property-division-mortgage",
    "sale-purchase-agreement", "taxes-consultation"
]
CATEGORY_NAMES = {
    "administrative-offenses": "Административные правонарушения",
    "apartment-check-sale": "Проверка квартиры / продажа",
    "child-support": "Алименты на ребёнка",
    "compensations-benefits": "Компенсации и льготы",
    "consumer-rights-protection": "Защита прав потребителей",
    "criminal-lawyer": "Уголовный адвокат",
    "family-lawyer-divorce-alimony": "Семейный юрист (развод, алименты)",
    "free-legal-consultation": "Бесплатная юридическая консультация",
    "housing-disputes": "Жилищные споры",
    "inheritance-processing": "Оформление наследства",
    "inheritance-without-will": "Наследство без завещания",
    "labor-disputes": "Трудовые споры",
    "land-disputes": "Земельные споры",
    "notary-powers-of-attorney": "Нотариальные доверенности",
    "online-lawyer": "Юрист онлайн",
    "personal-bankruptcy": "Банкротство физических лиц",
    "property-division-divorce": "Раздел имущества при разводе",
    "property-division-mortgage": "Раздел квартиры / ипотека",
    "sale-purchase-agreement": "Договор купли-продажи",
    "taxes-consultation": "Налоги и консультации",
}
INTROS = {
    "Административные правонарушения": "Штрафы, ГИБДД, административные протоколы и обжалование постановлений.",
    "Проверка квартиры / продажа": "Юридическая проверка квартиры перед покупкой и сопровождение сделок с недвижимостью.",
    "Алименты на ребёнка": "Назначение, изменение и взыскание алиментов.",
    "Компенсации и льготы": "Социальные выплаты, льготы и порядок их оформления.",
    "Защита прав потребителей": "Возврат товара, некачественные услуги, претензии к магазинам и сервисам.",
    "Уголовный адвокат": "Защита по уголовным делам, допросы, задержание и избрание меры пресечения.",
    "Семейный юрист (развод, алименты)": "Развод, алименты, споры о детях, брачные договоры и порядок расторжения брака.",
    "Бесплатная юридическая консультация": "Ответы юристов на типичные правовые вопросы.",
    "Жилищные споры": "Регистрация, выселение, коммунальные споры, споры с управляющими компаниями.",
    "Оформление наследства": "Вступление в наследство, сроки, документы, споры между наследниками.",
    "Наследство без завещания": "Наследование по закону, очередность наследников.",
    "Трудовые споры": "Увольнение, невыплата зарплаты, переработки и споры с работодателем.",
    "Земельные споры": "Аренда и выкуп земли, границы участков и споры с муниципалитетом.",
    "Нотариальные доверенности": "Оформление и отзыв доверенностей, проверка полномочий.",
    "Юрист онлайн": "Консультации юриста через интернет, проверка документов онлайн.",
    "Банкротство физических лиц": "Процедура банкротства граждан, списание долгов.",
    "Раздел имущества при разводе": "Раздел квартиры, бизнеса, вкладов при разводе.",
    "Раздел квартиры / ипотека": "Раздел ипотечной квартиры, ответственность по кредиту.",
    "Договор купли-продажи": "Подготовка и проверка договоров купли-продажи.",
    "Налоги и консультации": "Налоговые споры, оптимизация нагрузки.",
}


def extract_qa_blocks(html: str):
    """Извлекает блоки Q&A из index.html (формат: div.qa-anchor + article)."""
    blocks = []
    pattern = re.compile(
        r'<div\s+id="([^"]+)"\s+class="qa-anchor"\s*></div>\s*<article[^>]*>\s*<h2>([^<]+)</h2>\s*<p\s+class="article-meta">([^<]+)</p>\s*(.*?)(?=\s*</article>|\s*<div\s+id=)',
        re.DOTALL
    )
    for m in pattern.finditer(html):
        slug, question, meta, body = m.group(1), m.group(2).strip(), m.group(3).strip(), m.group(4)
        body = re.sub(r'\s+', ' ', body.strip())
        body_paras = [p.strip() for p in re.split(r'</p>\s*<p>|<p>|</p>', body) if p.strip() and not p.strip().startswith('<')]
        if not body_paras:
            body_paras = [body[:500]] if body else [""]
        blocks.append({"slug": slug, "question": question, "meta": meta, "body_paras": body_paras[:5]})
    return blocks


def main():
    for slug in ORIGINAL_SLUGS:
        path = RESOURCES / slug / "index.html"
        if not path.exists():
            print(f"# SKIP {slug} - no index.html")
            continue
        html = path.read_text(encoding="utf-8")
        blocks = extract_qa_blocks(html)
        name = CATEGORY_NAMES.get(slug, slug)
        intro = INTROS.get(name, f"Вопросы и ответы по теме «{name}».")
        print(f"\n# {slug} ({name}) - {len(blocks)} articles")


if __name__ == "__main__":
    main()
