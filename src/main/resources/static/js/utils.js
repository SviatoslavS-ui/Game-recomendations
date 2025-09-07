/**
 * Shared utility functions for Game Recommender
 */

// Application configuration
const APP_CONFIG = {
    ENDPOINTS: {
        RECOMMENDATIONS: '/recommendations/results',
        GAME_DETAILS: '/api/games/{id}/details'
    }
};

// Debug configuration
const DEBUG_CONFIG = {
    RECOMMENDATIONS: false,
    MODAL: false,
    GENERAL: true
};

/**
 * Debug logging utility with module-specific control
 * @param {string} module - The module name (recommendations, modal, etc.)
 * @param {string} message - The message to log
 * @param {any} data - Optional data to log
 */
function debugLog(module, message, data) {
    const moduleKey = module.toUpperCase();
    if (DEBUG_CONFIG[moduleKey]) {
        console.log(`[${module}] ${message}`, data || '');
    }
}

/**
 * Creates a module-specific debug logger
 * @param {string} moduleName - The name of the module
 * @returns {Function} A debug function that automatically includes the module name
 */
function createDebugger(moduleName) {
    return function(message, data) {
        debugLog(moduleName, message, data);
    };
}

// Export to global scope
window.debugLog = debugLog;
window.createDebugger = createDebugger;
window.DEBUG_CONFIG = DEBUG_CONFIG;
window.APP_CONFIG = APP_CONFIG;
