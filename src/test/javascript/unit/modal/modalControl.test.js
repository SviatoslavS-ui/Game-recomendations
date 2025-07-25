/**
 * Tests for modal control functions in game-details-modal.js
 */
// setupMockModalHtml is now handled in setupTests.js

describe('Modal Control Functions', () => {
  beforeEach(() => {
    // Mock fetch since loadGameDetails uses it
    fetch.mockResolvedValue({
      ok: true,
      json: () => Promise.resolve({ title: 'Test Game' })
    });
  });
  
  describe('openGameDetailsModal', () => {
    test('should remove hidden class from modal and overlay', () => {
      // Initial state - modal and overlay are hidden
      const modal = document.getElementById('gameDetailsModal');
      const overlay = document.getElementById('modalOverlay');
      
      expect(modal.classList.contains('hidden')).toBe(true);
      expect(overlay.classList.contains('hidden')).toBe(true);
      
      // Call the function
      window.openGameDetailsModal('test-game-id');
      
      // Check that hidden class is removed
      expect(modal.classList.contains('hidden')).toBe(false);
      expect(overlay.classList.contains('hidden')).toBe(false);
    });
    
    test('should show spinner and hide content', () => {
      const spinner = document.getElementById('loadingSpinner');
      const content = document.getElementById('gameDetailsContent');
      
      // Initial state
      spinner.style.display = 'none';
      content.classList.remove('hidden');
      
      // Call the function
      window.openGameDetailsModal('test-game-id');
      
      // Check that spinner is shown and content is hidden
      expect(spinner.style.display).toBe('flex');
      expect(content.classList.contains('hidden')).toBe(true);
    });
    
    test('should set body overflow to hidden', () => {
      // Initial state
      document.body.style.overflow = 'auto';
      
      // Call the function
      window.openGameDetailsModal('test-game-id');
      
      // Check that body overflow is hidden
      expect(document.body.style.overflow).toBe('hidden');
    });
  });
  
  describe('closeGameDetailsModal', () => {
    test('should add hidden class to modal and overlay', () => {
      // Initial state - modal and overlay are visible
      const modal = document.getElementById('gameDetailsModal');
      const overlay = document.getElementById('modalOverlay');
      modal.classList.remove('hidden');
      overlay.classList.remove('hidden');
      
      // Call the function
      window.closeGameDetailsModal();
      
      // Check that hidden class is added
      expect(modal.classList.contains('hidden')).toBe(true);
      expect(overlay.classList.contains('hidden')).toBe(true);
    });
    
    test('should reset body overflow', () => {
      // Initial state
      document.body.style.overflow = 'hidden';
      
      // Call the function
      window.closeGameDetailsModal();
      
      // Check that body overflow is reset
      expect(document.body.style.overflow).toBe('');
    });
  });
});
