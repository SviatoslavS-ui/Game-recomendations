/**
 * Jest setup file
 * 
 * This file runs before each test file to set up the test environment.
 */

const fs = require('fs');
const path = require('path');
const { setupMockModalHtml } = require('../mocks/modalHtmlMock');

// Mock fetch API globally
global.fetch = jest.fn();

// Load the actual game-details-modal.js file
let scriptLoaded = false;
const loadGameDetailsScript = () => {
  if (scriptLoaded) {
    return true;
  }
  
  try {
    const scriptPath = path.resolve(__dirname, '../../../main/resources/static/js/game-details-modal.js');
    let scriptContent = fs.readFileSync(scriptPath, 'utf8');
    
    // Wrap the script content in an IIFE to avoid global variable conflicts
    scriptContent = `(function() {
      ${scriptContent}
      
      // Make functions available globally
      window.openGameDetailsModal = openGameDetailsModal;
      window.closeGameDetailsModal = closeGameDetailsModal;
      window.loadGameDetails = loadGameDetails;
      window.displayGameDetails = displayGameDetails;
      window.setElementText = setElementText;
      window.populateList = populateList;
      window.setupEventListeners = setupEventListeners;
      window.gameDetailsCache = gameDetailsCache;
    })();`;
    
    // Create a script element and append it to the document
    const scriptElement = document.createElement('script');
    scriptElement.textContent = scriptContent;
    document.head.appendChild(scriptElement);
    
    scriptLoaded = true;
    return true;
  } catch (error) {
    console.error('Error loading game-details-modal.js:', error);
    return false;
  }
};

// Load the script once before all tests
beforeAll(() => {
  loadGameDetailsScript();
});

// Helper to reset all mocks between tests
beforeEach(() => {
  jest.clearAllMocks();
  
  // Reset fetch mock
  fetch.mockClear();
  
  // Reset document body before each test
  document.body.innerHTML = setupMockModalHtml();
  
  // Clear cache before each test
  if (window.gameDetailsCache) {
    Object.keys(window.gameDetailsCache).forEach(key => {
      delete window.gameDetailsCache[key];
    });
  }
});

// Silence console.log during tests but keep errors visible
console.log = jest.fn();
// Keep error logging visible for debugging
// console.error = jest.fn();
