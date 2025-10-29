/**
 * Pravochat Analytics - Яндекс.Метрика интеграция
 * Отслеживание конверсий и пользовательских действий
 */

// ID счетчика Яндекс.Метрики
const METRIKA_ID = 104954778;

/**
 * Инициализация аналитики
 */
function initAnalytics() {
    // Проверяем, что Яндекс.Метрика загружена
    if (typeof ym === 'undefined') {
        console.warn('Yandex.Metrika not loaded');
        return;
    }
    
    console.log('Analytics initialized with ID:', METRIKA_ID);
}

/**
 * Отслеживание захода в приложение
 */
function trackAppEnter() {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'app_enter');
        console.log('Tracked: app_enter');
    }
}

/**
 * Отслеживание начала консультации
 * @param {string} consultationType - Тип консультации (free, paid, urgent)
 * @param {string} category - Категория правового вопроса
 */
function trackConsultationStart(consultationType = 'free', category = 'general') {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'consultation_start', {
            consultation_type: consultationType,
            category: category,
            timestamp: Date.now()
        });
        console.log('Tracked: consultation_start', { consultationType, category });
    }
}

/**
 * Отслеживание отправки сообщения
 * @param {string} messageType - Тип сообщения (question, answer, follow_up)
 * @param {number} messageLength - Длина сообщения
 */
function trackMessageSent(messageType = 'question', messageLength = 0) {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'message_sent', {
            message_type: messageType,
            message_length: messageLength,
            timestamp: Date.now()
        });
        console.log('Tracked: message_sent', { messageType, messageLength });
    }
}

/**
 * Отслеживание завершения консультации
 * @param {string} result - Результат консультации (completed, abandoned, timeout)
 * @param {number} duration - Длительность консультации в секундах
 * @param {number} messageCount - Количество сообщений
 */
function trackConsultationComplete(result = 'completed', duration = 0, messageCount = 0) {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'consultation_complete', {
            result: result,
            duration: duration,
            message_count: messageCount,
            timestamp: Date.now()
        });
        console.log('Tracked: consultation_complete', { result, duration, messageCount });
    }
}

/**
 * Отслеживание клика по кнопке
 * @param {string} buttonName - Название кнопки
 * @param {string} location - Местоположение кнопки
 */
function trackButtonClick(buttonName, location = 'unknown') {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'button_click', {
            button_name: buttonName,
            location: location,
            timestamp: Date.now()
        });
        console.log('Tracked: button_click', { buttonName, location });
    }
}

/**
 * Отслеживание просмотра страницы
 * @param {string} pageName - Название страницы
 * @param {string} section - Раздел страницы
 */
function trackPageView(pageName, section = 'main') {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'hit', pageName, {
            section: section,
            timestamp: Date.now()
        });
        console.log('Tracked: page_view', { pageName, section });
    }
}

/**
 * Отслеживание ошибки
 * @param {string} errorType - Тип ошибки
 * @param {string} errorMessage - Сообщение об ошибке
 * @param {string} location - Местоположение ошибки
 */
function trackError(errorType, errorMessage, location = 'unknown') {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'error_occurred', {
            error_type: errorType,
            error_message: errorMessage,
            location: location,
            timestamp: Date.now()
        });
        console.log('Tracked: error_occurred', { errorType, errorMessage, location });
    }
}

/**
 * Отслеживание времени на сайте
 * @param {number} timeSpent - Время в секундах
 */
function trackTimeSpent(timeSpent) {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'time_spent', {
            time_seconds: timeSpent,
            timestamp: Date.now()
        });
        console.log('Tracked: time_spent', { timeSpent });
    }
}

/**
 * Отслеживание источника трафика
 * @param {string} source - Источник (search, social, direct, referral)
 * @param {string} medium - Канал (organic, cpc, social, email)
 * @param {string} campaign - Кампания
 */
function trackTrafficSource(source, medium = 'unknown', campaign = '') {
    if (typeof ym !== 'undefined') {
        ym(METRIKA_ID, 'reachGoal', 'traffic_source', {
            source: source,
            medium: medium,
            campaign: campaign,
            timestamp: Date.now()
        });
        console.log('Tracked: traffic_source', { source, medium, campaign });
    }
}

// Экспорт функций для использования в приложении
window.PravochatAnalytics = {
    init: initAnalytics,
    trackAppEnter,
    trackConsultationStart,
    trackMessageSent,
    trackConsultationComplete,
    trackButtonClick,
    trackPageView,
    trackError,
    trackTimeSpent,
    trackTrafficSource
};

// Автоматическая инициализация при загрузке
document.addEventListener('DOMContentLoaded', function() {
    initAnalytics();
    trackAppEnter();
    
    // Отслеживание времени на сайте
    let startTime = Date.now();
    setInterval(function() {
        let timeSpent = Math.floor((Date.now() - startTime) / 1000);
        if (timeSpent > 0 && timeSpent % 30 === 0) { // каждые 30 секунд
            trackTimeSpent(timeSpent);
        }
    }, 1000);
});

console.log('Pravochat Analytics loaded');
