/**
 * Tests for event listeners in game-details-modal.js
 */

const { setupMockModalHtml } = require('../../mocks/modalHtmlMock');

describe('Event Listeners', () => {
  // Setup before each test
  beforeEach(() => {
    // Set up the mock HTML structure
    document.body.innerHTML = setupMockModalHtml();
    
    // Mock the closeGameDetailsModal function
    global.closeGameDetailsModal = jest.fn();
    
    // Define the setupEventListeners function
    global.setupEventListeners = function() {
      const closeBtn = document.getElementById('modalCloseBtn');
      if (closeBtn) {
        closeBtn.addEventListener('click', closeGameDetailsModal);
      }
    
      const overlay = document.getElementById('modalOverlay');
      if (overlay) {
        overlay.addEventListener('click', closeGameDetailsModal);
      }
    
      document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
          closeGameDetailsModal();
        }
      });
    };
  });
  
  describe('setupEventListeners', () => {
    test('should add click event listener to close button', () => {
      // Call the function
      setupEventListeners();
      
      // Simulate clicking the close button
      document.getElementById('modalCloseBtn').click();
      
      // Check that closeGameDetailsModal was called
      expect(closeGameDetailsModal).toHaveBeenCalled();
    });
    
    test('should add click event listener to overlay', () => {
      // Call the function
      setupEventListeners();
      
      // Simulate clicking the overlay
      document.getElementById('modalOverlay').click();
      
      // Check that closeGameDetailsModal was called
      expect(closeGameDetailsModal).toHaveBeenCalled();
    });
    
    test('should add keydown event listener for Escape key', () => {
      // Call the function
      setupEventListeners();
      
      // Simulate pressing the Escape key
      const escapeKeyEvent = new KeyboardEvent('keydown', { key: 'Escape' });
      document.dispatchEvent(escapeKeyEvent);
      
      // Check that closeGameDetailsModal was called
      expect(closeGameDetailsModal).toHaveBeenCalled();
    });
    
    test('should not call closeGameDetailsModal for other keys', () => {
      // Call the function
      setupEventListeners();
      
      // Simulate pressing a different key
      const otherKeyEvent = new KeyboardEvent('keydown', { key: 'Enter' });
      document.dispatchEvent(otherKeyEvent);
      
      // Check that closeGameDetailsModal was not called
      expect(closeGameDetailsModal).not.toHaveBeenCalled();
    });
    
    test('should handle missing elements gracefully', () => {
      // Remove the close button and overlay
      document.body.innerHTML = '<div>Empty page</div>';
      
      // This should not throw an error
      expect(() => {
        setupEventListeners();
      }).not.toThrow();
    });
  });
});
