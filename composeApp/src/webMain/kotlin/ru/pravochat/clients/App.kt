package ru.pravochat.clients

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import ru.pravochat.clients.analytics.AnalyticsTracker
import ru.pravochat.clients.designsystem.components.*
import ru.pravochat.clients.designsystem.theme.PravochatColors
import ru.pravochat.clients.designsystem.theme.PravochatTypography
import ru.pravochat.clients.designsystem.theme.PravochatSpacing
import ru.pravochat.clients.di.koinInjectRemember
import ru.pravochat.clients.repo.TitleRepo
import ru.pravochat.clients.states.ButtonState
import org.w3c.dom.events.Event

data class Message(val id: Int, val text: String, val isUser: Boolean, val timestamp: String)

val chatMessages = listOf(
    Message(1, "Здравствуйте! У меня вопрос по трудовому законодательству.", true, "10:30"),
    Message(2, "Добрый день! Я готов помочь вам с вопросами по трудовому праву. Расскажите, пожалуйста, подробнее о вашей ситуации.", false, "10:31"),
    Message(3, "Мой работодатель не оплачивает сверхурочные часы. Что мне делать?", true, "10:32"),
    Message(4, "Согласно статье 152 Трудового кодекса РФ, сверхурочная работа оплачивается за первые два часа работы не менее чем в полуторном размере, за последующие часы - не менее чем в двойном размере. Рекомендую составить письменную претензию работодателю и обратиться в Государственную инспекцию труда.", false, "10:33")
)

private data class NavigationItem(
    val label: String,
    val targetId: String
)

private val navigationItems = listOf(
    NavigationItem("Реальные кейсы", "cases"),
    NavigationItem("О нас", "about"),
    NavigationItem("Контакты", "contacts"),
    NavigationItem("Премиум модель", "premium"),
    NavigationItem("Консультация юриста", "consultation")
)

private data class CaseStudy(
    val title: String,
    val summary: String
)

private val caseStudies = listOf(
    CaseStudy(
        title = "Восстановление сотрудника после незаконного увольнения",
        summary = "Подготовили претензию, представили клиента в суде и добились восстановления на работе с выплатой компенсации."
    ),
    CaseStudy(
        title = "Сопровождение сделки по продаже бизнеса",
        summary = "Проанализировали договор, провели правовую экспертизу активов и снизили риски сторон с помощью дополнительных соглашений."
    ),
    CaseStudy(
        title = "Оптимизация налоговой нагрузки для ИП",
        summary = "Провели аудит договоров с контрагентами, подобрали льготные режимы и снизили расходы на 18% без нарушения законодательства."
    )
)

@Composable
fun MarkdownContent(markdown: String) {
    if (markdown.isBlank()) return
    markdown.split("\n\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .forEach { block ->
            when {
                block.startsWith("## ") -> PravochatHeading(text = block.removePrefix("## ").trim())
                else -> PravochatBodyText(text = block)
            }
        }
}

@Composable
fun App() {
    val titleRepo = koinInjectRemember<TitleRepo>()
    val content by titleRepo.content().collectAsState(initial = "")
    var isMenuOpen by remember { mutableStateOf(false) }
    var windowWidth by remember { mutableStateOf(window.innerWidth.toInt()) }
    var isImproveDialogOpen by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            windowWidth = window.innerWidth.toInt()
        }
        window.addEventListener("resize", listener)
        onDispose {
            window.removeEventListener("resize", listener)
        }
    }
    val isMobile = windowWidth < 769

    Div({
        style {
            width(100.percent)
            minHeight(100.vh)
            margin(0.px)
            padding(0.px)
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            backgroundColor(PravochatColors.BackgroundMain)
            fontFamily(PravochatTypography.FontFamily)
        }
    }) {
        HeaderBar(
            isMobile = isMobile,
            isMenuOpen = isMenuOpen,
            onToggle = { isMenuOpen = !isMenuOpen },
            onNavigate = { isMenuOpen = false },
            onRequestImproveAccess = {
                isMenuOpen = false
                isImproveDialogOpen = true
            }
        )
        Main({
            style {
                flex(1)
                width(100.percent)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
            }
        }) {
            HeroSection(content = content)
            CasesSection()
            AboutSection()
            ConsultationSection()
            ContactsSection()
        }
        if (isMobile && isMenuOpen) {
            MobileNavigationOverlay(
                onDismiss = { isMenuOpen = false },
                onNavigate = { isMenuOpen = false },
                onRequestImproveAccess = {
                    isMenuOpen = false
                    isImproveDialogOpen = true
                }
            )
        }
        if (isImproveDialogOpen) {
            EarlyAccessDialog(
                onClose = { isImproveDialogOpen = false }
            )
        }
    }
}

@Composable
private fun HeaderBar(
    isMobile: Boolean,
    isMenuOpen: Boolean,
    onToggle: () -> Unit,
    onNavigate: () -> Unit,
    onRequestImproveAccess: () -> Unit
) {
    Header({
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.SpaceBetween)
            if (isMobile) {
                padding(PravochatSpacing.md)
                paddingLeft(PravochatSpacing.lg)
                paddingRight(PravochatSpacing.lg)
            } else {
                padding(PravochatSpacing.lg)
                paddingLeft(PravochatSpacing.xxl)
                paddingRight(PravochatSpacing.xxl)
            }
            position(Position.Sticky)
            top(0.px)
            backgroundColor(PravochatColors.BackgroundMain)
            property("z-index", "20")
            property("border-bottom", "1px solid #E6E6E8")
        }
    }) {
        A("#hero", attrs = {
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                gap(PravochatSpacing.md)
                fontSize(PravochatTypography.Heading.fontSize)
                fontWeight(PravochatTypography.Heading.fontWeight)
                color(PravochatColors.TextPrimary)
                textDecoration("none")
            }
        }) {
            PravochatIcon(src = "/images/pravo-logo.svg", size = 32, alt = "PravoChat logo")
            Text("PravoChat")
        }

        Nav({
            style {
                display(if (isMobile) DisplayStyle.None else DisplayStyle.Flex)
                gap(PravochatSpacing.xl)
                alignItems(AlignItems.Center)
            }
        }) {
            navigationItems.forEach { item ->
                A("#${item.targetId}", attrs = {
                    style {
                        fontSize(PravochatTypography.Body.fontSize)
                        fontWeight(PravochatTypography.Body.fontWeight)
                        color(PravochatColors.TextPrimary)
                        textDecoration("none")
                        property("transition", "opacity 150ms")
                    }
                    onClick { event ->
                        if (item.targetId == "premium") {
                            event.preventDefault()
                            onNavigate()
                            onRequestImproveAccess()
                        } else {
                            onNavigate()
                        }
                    }
                }) {
                    Text(item.label)
                }
            }
        }

        if (isMobile) {
            BurgerMenuButton(
                isMenuOpen = isMenuOpen,
                onToggle = onToggle
            )
        }
    }
}

@Composable
private fun BurgerMenuButton(
    isMenuOpen: Boolean,
    onToggle: () -> Unit
) {
    Button(attrs = {
        attr("aria-label", if (isMenuOpen) "Закрыть меню" else "Открыть меню")
        attr("aria-expanded", isMenuOpen.toString())
        style {
            width(40.px)
            height(40.px)
            border(0.px)
            borderRadius(8.px)
            backgroundColor(Color.transparent)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(0.px)
            property("cursor", "pointer")
        }
        onClick { onToggle() }
    }) {
        Div({
            style {
                width(24.px)
                height(18.px)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                justifyContent(JustifyContent.SpaceBetween)
                position(Position.Relative)
            }
        }) {
            listOf(0, 1, 2).forEach { index ->
                val isMiddle = index == 1
                Div({
                    style {
                        width(24.px)
                        height(2.px)
                        backgroundColor(PravochatColors.TextPrimary)
                        borderRadius(2.px)
                        property("transition", "transform 150ms, opacity 150ms")
                        property(
                            "transform",
                            when {
                                isMenuOpen && index == 0 -> "translateY(8px) rotate(45deg)"
                                isMenuOpen && index == 2 -> "translateY(-8px) rotate(-45deg)"
                                else -> "none"
                            }
                        )
                        property(
                            "opacity",
                            if (isMenuOpen && isMiddle) "0" else "1"
                        )
                    }
                })
            }
        }
    }
}

@Composable
private fun MobileNavigationOverlay(
    onDismiss: () -> Unit,
    onNavigate: () -> Unit,
    onRequestImproveAccess: () -> Unit
) {
    Div({
        style {
            position(Position.Fixed)
            top(0.px)
            left(0.px)
            width(100.percent)
            height(100.vh)
            backgroundColor(rgba(30, 30, 30, 0.6))
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            property("z-index", "10")
        }
        onClick { onDismiss() }
    }) {
        Nav({
            style {
                width(80.percent)
                maxWidth(320.px)
                backgroundColor(PravochatColors.BackgroundWhite)
                borderRadius(16.px)
                padding(PravochatSpacing.xxl)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(PravochatSpacing.xl)
            }
            onClick { event ->
                event.stopPropagation()
            }
        }) {
            navigationItems.forEach { item ->
                A("#${item.targetId}", attrs = {
                    style {
                        fontSize(PravochatTypography.Body.fontSize)
                        fontWeight(PravochatTypography.Body.fontWeight)
                        color(PravochatColors.TextPrimary)
                        textDecoration("none")
                    }
                    onClick {
                        if (item.targetId == "premium") {
                            onNavigate()
                            onRequestImproveAccess()
                        } else {
                            onNavigate()
                        }
                    }
                }) {
                    Text(item.label)
                }
            }
        }
    }
}

@Composable
private fun HeroSection(content: String) {
    Section(attrs = {
        id("hero")
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            padding(PravochatSpacing.xxl)
            property("scroll-margin-top", "96px")
        }
    }) {
        PravochatContainer {
            Div({
                style {
                    width(100.percent)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(PravochatSpacing.contentGap)
                }
            }) {
                MarkdownContent(content)
            }
        }
    }
}

@Composable
private fun CasesSection() {
    SectionLayout(id = "cases", title = "Реальные кейсы") {
        Div({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(PravochatSpacing.sectionGap)
            }
        }) {
            caseStudies.forEach { caseStudy ->
                Div({
                    style {
                        borderRadius(16.px)
                        backgroundColor(PravochatColors.BackgroundWhite)
                        padding(PravochatSpacing.xl)
                        property("box-shadow", "0px 12px 32px rgba(20, 30, 80, 0.12)")
                        display(DisplayStyle.Flex)
                        flexDirection(FlexDirection.Column)
                        gap(PravochatSpacing.sm)
                    }
                }) {
                    PravochatHeading(caseStudy.title)
                    PravochatBodyText(caseStudy.summary)
                }
            }
        }
    }
}

@Composable
private fun AboutSection() {
    SectionLayout(id = "about", title = "О нас") {
        PravochatBodyText(
            text = "Мы объединяем опыт практикующих юристов и возможности искусственного интеллекта. Команда специализируется на юридическом сопровождении бизнеса и частных клиентов, помогая быстро ориентироваться в сложных правовых ситуациях."
        )
        PravochatBodyText(
            text = "PravoChat анализирует документы, предлагает готовые решения и подключает специалистов для детальной консультации. Мы фокусируемся на прозрачности, скорости и понятной коммуникации."
        )
    }
}

@Composable
private fun EarlyAccessDialog(
    onClose: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    val isFormValid = fullName.isNotBlank() && email.isNotBlank()

    Div({
        style {
            position(Position.Fixed)
            top(0.px)
            left(0.px)
            width(100.percent)
            height(100.vh)
            backgroundColor(rgba(30, 30, 30, 0.7))
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
            property("z-index", "30")
            padding(PravochatSpacing.lg)
        }
        onClick { onClose() }
    }) {
        Div({
            style {
                width(100.percent)
                maxWidth(420.px)
                backgroundColor(PravochatColors.BackgroundWhite)
                borderRadius(20.px)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                gap(PravochatSpacing.md)
                padding(PravochatSpacing.xxl)
                property("box-shadow", "0px 24px 48px rgba(20, 30, 80, 0.18)")
            }
            onClick { event -> event.stopPropagation() }
        }) {
            PravochatHeading("Ранний доступ")
            PravochatBodyText(
                text = "Функция улучшения модели доступна в раннем тестировании. Оставьте контакты, и мы подключим вас, как только доступ будет активирован."
            )

            Label(forId = "improve-name", attrs = {
                style {
                    fontWeight(PravochatTypography.Body.fontWeight)
                    color(PravochatColors.TextPrimary)
                }
            }) {
                Text("Имя")
            }
            Input(InputType.Text, attrs = {
                id("improve-name")
                placeholder("Ваше имя")
                value(fullName)
                onInput { fullName = it.value }
                style {
                    width(100.percent)
                    padding(PravochatSpacing.md)
                    borderRadius(12.px)
                    border {
                        width(1.px)
                        style(LineStyle.Solid)
                        color(Color("#D9D9DC"))
                    }
                    fontSize(PravochatTypography.Body.fontSize)
                }
            })

            Label(forId = "improve-email", attrs = {
                style {
                    fontWeight(PravochatTypography.Body.fontWeight)
                    color(PravochatColors.TextPrimary)
                }
            }) {
                Text("Email")
            }
            Input(InputType.Email, attrs = {
                id("improve-email")
                placeholder("name@example.com")
                value(email)
                onInput { email = it.value }
                style {
                    width(100.percent)
                    padding(PravochatSpacing.md)
                    borderRadius(12.px)
                    border {
                        width(1.px)
                        style(LineStyle.Solid)
                        color(Color("#D9D9DC"))
                    }
                    fontSize(PravochatTypography.Body.fontSize)
                }
            })

            if (isSubmitted) {
                PravochatBodyText(
                    text = "Спасибо! Мы свяжемся с вами в ближайшее время.",
                    color = PravochatColors.PrimaryBlue
                )
            }

            Div({
                style {
                    display(DisplayStyle.Flex)
                    gap(PravochatSpacing.sm)
                    marginTop(PravochatSpacing.lg)
                    justifyContent(JustifyContent.FlexEnd)
                }
            }) {
                Button(attrs = {
                    style {
                        padding(PravochatSpacing.sm)
                        paddingLeft(PravochatSpacing.lg)
                        paddingRight(PravochatSpacing.lg)
                        borderRadius(10.px)
                        border {
                            width(1.px)
                            style(LineStyle.Solid)
                            color(Color("#D9D9DC"))
                        }
                        backgroundColor(Color.transparent)
                        color(PravochatColors.TextPrimary)
                        fontSize(PravochatTypography.Body.fontSize)
                        property("cursor", "pointer")
                    }
                    onClick { onClose() }
                }) {
                    Text("Отмена")
                }

                Button(attrs = {
                    style {
                        padding(PravochatSpacing.sm)
                        paddingLeft(PravochatSpacing.lg)
                        paddingRight(PravochatSpacing.lg)
                        borderRadius(10.px)
                        border(0.px)
                        backgroundColor(
                            if (isFormValid) PravochatColors.PrimaryBlue else Color("#A3C7F6")
                        )
                        color(PravochatColors.TextWhite)
                        fontSize(PravochatTypography.Body.fontSize)
                        property("cursor", if (isFormValid) "pointer" else "not-allowed")
                        property("transition", "opacity 150ms")
                    }
                    if (!isFormValid) {
                        disabled()
                    }
                    onClick {
                        if (isFormValid) {
                            isSubmitted = true
                            println("Early access requested: name=$fullName email=$email")
                        }
                    }
                }) {
                    Text(if (isSubmitted) "Отправлено" else "Отправить заявку")
                }
            }
        }
    }
}

@Composable
private fun ConsultationSection() {
    SectionLayout(id = "consultation", title = "Консультация юриста") {
        PravochatBodyText(
            text = "Получите первичную консультацию за несколько минут. Опишите ситуацию в чате — система подскажет возможные варианты, а при необходимости подключит живого специалиста."
        )
        ChatInputCompact()
    }
}

@Composable
private fun ContactsSection() {
    SectionLayout(id = "contacts", title = "Контакты") {
        PravochatBodyText(
            text = "ООО «ПравоЧат»\nИНН 7701234567, ОГРН 1227700000000"
        )
        PravochatBodyText(
            text = "Москва, ул. Правовая, д. 10\nРаботаем по всей России онлайн."
        )
        A(href = "tel:+74950000000", attrs = {
            style {
                fontSize(PravochatTypography.Body.fontSize)
                fontWeight(PravochatTypography.Body.fontWeight)
                color(PravochatColors.PrimaryBlue)
                textDecoration("none")
            }
        }) {
            Text("+7 (495) 000-00-00")
        }
        A(href = "mailto:hello@pravochat.ru", attrs = {
            style {
                fontSize(PravochatTypography.Body.fontSize)
                fontWeight(PravochatTypography.Body.fontWeight)
                color(PravochatColors.PrimaryBlue)
                textDecoration("none")
            }
        }) {
            Text("hello@pravochat.ru")
        }
    }
}

@Composable
private fun SectionLayout(
    id: String,
    title: String,
    content: @Composable () -> Unit
) {
    Section(attrs = {
        id(id)
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            padding(PravochatSpacing.xxl)
            property("scroll-margin-top", "96px")
        }
    }) {
        PravochatContainer {
            Div({
                style {
                    width(100.percent)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(PravochatSpacing.sectionGap)
                }
            }) {
                PravochatHeading(title)
                content()
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignSelf(if (message.isUser) AlignSelf.FlexEnd else AlignSelf.FlexStart)
            maxWidth(70.percent)
        }
    }) {
        Div({
            style {
                padding(PravochatSpacing.md, PravochatSpacing.lg)
                borderRadius(16.px)
                backgroundColor(if (message.isUser) PravochatColors.PrimaryBlue else PravochatColors.BackgroundWhite)
                color(if (message.isUser) PravochatColors.TextWhite else PravochatColors.TextPrimary)
                property("box-shadow", "0px 1px 3px ${PravochatColors.black10Alpha()}")
                fontSize(PravochatTypography.Body.fontSize)
                fontWeight(PravochatTypography.Body.fontWeight)
                property("line-height", PravochatTypography.Caption.lineHeight)
            }
        }) {
            Text(message.text)
        }
        
        Span({
            style {
                fontSize(PravochatTypography.Caption.fontSize)
                fontWeight(PravochatTypography.Caption.fontWeight)
                property("line-height", PravochatTypography.Caption.lineHeight)
                property("color", PravochatColors.black50Alpha())
                marginTop(PravochatSpacing.xs)
                marginLeft(if (message.isUser) 0.px else 0.px)
                marginRight(if (message.isUser) 0.px else 0.px)
                alignSelf(if (message.isUser) AlignSelf.FlexEnd else AlignSelf.FlexStart)
            }
        }) {
            Text(message.timestamp)
        }
    }
}

@Composable
fun ChatInputCompact() {
    val ctaHref = "https://antoqk1avl.customgpt-agents.com"

    Div({
        style {
            width(100.percent)
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
        }
    }) {
        A(href = ctaHref, attrs = {
            target(ATarget.Blank)
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                gap(PravochatSpacing.xl)
                padding(20.px)
                paddingLeft(44.px)
                paddingRight(44.px)
                property("background", "linear-gradient(135deg, #308CEF 0%, #147EFF 100%)")
                color(PravochatColors.TextWhite)
                borderRadius(20.px)
                property("border", "1px solid rgba(255, 255, 255, 0.18)")
                textDecoration("none")
                fontSize(PravochatTypography.Heading.fontSize)
                fontWeight(PravochatTypography.Heading.fontWeight)
                property("box-shadow", "0 18px 40px rgba(20, 126, 255, 0.28)")
                property("backdrop-filter", "blur(18px)")
                property("transform", "translateY(0px)")
                property("transition", "transform 150ms, box-shadow 150ms")
            }
            onClick {
                js("console.log('Premium model CTA clicked')")
            }
            onMouseOver {
                it.currentTarget?.let { el ->
                    el.asDynamic().style.transform = "translateY(-3px)"
                    el.asDynamic().style.boxShadow = "0 24px 48px rgba(20, 126, 255, 0.35)"
                }
            }
            onMouseOut {
                it.currentTarget?.let { el ->
                    el.asDynamic().style.transform = "translateY(0px)"
                    el.asDynamic().style.boxShadow = "0 18px 40px rgba(20, 126, 255, 0.28)"
                }
            }
        }) {
            Div({
                style {
                    width(44.px)
                    height(44.px)
                    borderRadius(50.percent)
                    backgroundColor(Color("rgba(255, 255, 255, 0.18)"))
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                }
            }) {
                PravochatIcon(src = "/images/premium-button-icon.svg", size = 28, alt = "Premium model icon")
            }
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    gap(4.px)
                    textAlign("left")
                }
            }) {
                Span({
                    style {
                        fontSize(PravochatTypography.Heading.fontSize)
                        fontWeight(PravochatTypography.Heading.fontWeight)
                        color(PravochatColors.TextWhite)
                        property("line-height", "120%")
                    }
                }) {
                    Text("Перейти в чат")
                }
            }
        }
    }

    if (false) {
        var legacyInput by remember { mutableStateOf("") }
        val analytics: AnalyticsTracker = koinInjectRemember()
        val buttonState = koinInjectRemember<ButtonState>()
        PravochatChatInput(
            value = legacyInput,
            onValueChange = { legacyInput = it },
            onSend = {
                buttonState.onClick()
                analytics.send("chat_input", legacyInput)
            },
            buttonState = buttonState.state.value
        )
    }
}

