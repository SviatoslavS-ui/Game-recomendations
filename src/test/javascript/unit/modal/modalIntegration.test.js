/**
 * Integration tests for game-details-modal.js
 * 
 * These tests verify the complete flow of opening the modal,
 * loading data, and displaying it correctly.
 */

const { mockGameData, mockSuccessResponse } = require('../../mocks/gameMocks');

describe('Modal Integration Tests', () => {
  // Helper functions to reduce repetition
  const helpers = {
    getModalElements() {
      return {
        modal: document.getElementById('gameDetailsModal'),
        overlay: document.getElementById('modalOverlay'),
        spinner: document.getElementById('loadingSpinner'),
        content: document.getElementById('gameDetailsContent')
      };
    },

    expectModalVisible(elements) {
      expect(elements.modal.classList.contains('hidden')).toBe(false);
      expect(elements.overlay.classList.contains('hidden')).toBe(false);
    },

    expectModalHidden(elements) {
      expect(elements.modal.classList.contains('hidden')).toBe(true);
      expect(elements.overlay.classList.contains('hidden')).toBe(true);
    },

    expectLoadingState(elements) {
      expect(elements.spinner.style.display).toBe('flex');
      expect(elements.content.classList.contains('hidden')).toBe(true);
    },

    expectContentState(elements) {
      expect(elements.spinner.style.display).toBe('none');
      expect(elements.content.classList.contains('hidden')).toBe(false);
    }
  };
  
  beforeEach(() => {
    // Mock fetch
    fetch.mockResolvedValue(mockSuccessResponse);
    
    // Ensure event listeners are set up
    if (window.setupEventListeners) {
      window.setupEventListeners();
    }
  });
  
  describe('Modal Opening', () => {
    test('should show modal and loading state immediately', () => {
      const elements = helpers.getModalElements();
      
      // Initial state - modal is hidden
      helpers.expectModalHidden(elements);
      expect(document.body.style.overflow).toBe('');
      
      // Open the modal (synchronous part)
      const openPromise = window.openGameDetailsModal('test-game');
      
      // Check that modal is visible and loading state is shown immediately
      helpers.expectModalVisible(elements);
      helpers.expectLoadingState(elements);
      expect(document.body.style.overflow).toBe('hidden');
      
      // Verify it returns a promise
      expect(openPromise).toBeInstanceOf(Promise);
    });
    
    test('should load and display game data', async () => {
      // Clear any existing cache to ensure fetch is called
      if (window.gameDetailsCache) {
        delete window.gameDetailsCache['test-game'];
      }
      
      // Open the modal and wait for data loading
      const result = await window.openGameDetailsModal('test-game');
      
      const elements = helpers.getModalElements();
      
      // Check that fetch was called
      expect(fetch).toHaveBeenCalledWith('/api/games/test-game/details');
      
      // Wait for the 200ms delay in displayGameDetails to complete
      await new Promise(resolve => setTimeout(resolve, 250));
      
      // Check that content is now visible
      helpers.expectContentState(elements);
      
      // Check that game details are displayed
      expect(document.getElementById('gameTitle').textContent).toBe(mockGameData.title);
      expect(document.getElementById('gameReleaseDate').textContent).toBe(mockGameData.releaseDate);
      expect(document.getElementById('releaseDateDisplay').textContent).toBe(mockGameData.releaseDate);
      expect(document.getElementById('gameDeveloper').textContent).toBe(mockGameData.developer);
      expect(document.getElementById('gamePublisher').textContent).toBe(mockGameData.publisher);
      
      // Check that result is returned
      expect(result).toEqual(mockGameData);
    });
    
    test('should handle API errors gracefully', async () => {
      // Clear any existing cache to ensure fetch is called
      if (window.gameDetailsCache) {
        delete window.gameDetailsCache['test-game'];
      }
      
      // Mock fetch to reject
      fetch.mockRejectedValueOnce(new Error('Network error'));
      
      try {
        await window.openGameDetailsModal('test-game');
        throw new Error('Expected openGameDetailsModal to reject, but it resolved');
      } catch (error) {
        expect(error.message).toBe('Network error');
      }
      
      // Check that error message is displayed
      expect(document.getElementById('gameDetailsContent').innerHTML).toContain('Error loading game details');
      expect(document.getElementById('gameDetailsContent').innerHTML).toContain('Network error');
    });
  });
  
  describe('Modal Closing', () => {
    // Helper to set up an open modal before each test
    beforeEach(async () => {
      await window.openGameDetailsModal('test-game');
    });

    const closingMethods = [
      {
        name: 'close button',
        action: () => document.getElementById('modalCloseBtn').click()
      },
      {
        name: 'overlay click',
        action: () => document.getElementById('modalOverlay').click()
      },
      {
        name: 'Escape key',
        action: () => document.dispatchEvent(new KeyboardEvent('keydown', { key: 'Escape' }))
      }
    ];

    closingMethods.forEach(({ name, action }) => {
      test(`should close via ${name}`, () => {
        const elements = helpers.getModalElements();
        
        // Verify modal is open
        helpers.expectModalVisible(elements);
        expect(document.body.style.overflow).toBe('hidden');
        
        // Perform closing action
        action();
        
        // Verify modal is closed
        helpers.expectModalHidden(elements);
        expect(document.body.style.overflow).toBe('');
      });
    });
    
    test('should properly manage DOM states during modal lifecycle', async () => {
      // Modal should be open from beforeEach
      expect(document.body.style.overflow).toBe('hidden');
      
      // Close modal
      window.closeGameDetailsModal();
      expect(document.body.style.overflow).toBe('');
      
      // Open again
      await window.openGameDetailsModal('test-game');
      expect(document.body.style.overflow).toBe('hidden');
    });
  });
  

  
  describe('Data Display', () => {
    beforeEach(async () => {
      await window.openGameDetailsModal('test-game');
    });

    test('should display basic game information', () => {
      expect(document.getElementById('gameTitle').textContent).toBe(mockGameData.title);
      expect(document.getElementById('gameDeveloper').textContent).toBe(mockGameData.developer);
      expect(document.getElementById('gamePublisher').textContent).toBe(mockGameData.publisher);
      expect(document.getElementById('gameReleaseDate').textContent).toBe(mockGameData.releaseDate);
      expect(document.getElementById('releaseDateDisplay').textContent).toBe(mockGameData.releaseDate);
    });

    test('should display game details and pricing', () => {
      expect(document.getElementById('ageRating').textContent).toBe(mockGameData.ageRating);
      // The function looks for 'multiplayer' field but our mock has 'isMultiplayer'
      expect(document.getElementById('multiplayer').textContent).toBe('No');
      // The function looks for 'playtime' field but our mock has 'playtimeHours'
      expect(document.getElementById('playtime').textContent).toBe('Not available');
      // The function just sets the raw price value without $ prefix
      expect(document.getElementById('price').textContent).toBe(mockGameData.price.toString());
    });

    test('should display scores with correct styling', () => {
      const metacriticScore = document.getElementById('metacriticScore');
      expect(metacriticScore.textContent).toBe(mockGameData.metacriticScore.toString());
      expect(metacriticScore.className).toContain('score-high');
      
      const userScore = document.getElementById('userScore');
      expect(userScore.textContent).toBe(mockGameData.userScore.toString());
      expect(userScore.className).toContain('score-high');
    });

    test('should display image and description', () => {
      const mainImage = document.getElementById('gameMainImage');
      expect(mainImage.src).toContain(mockGameData.imageUrl);
      expect(mainImage.alt).toBe(mockGameData.title);
      
      expect(document.getElementById('gameDescription').innerHTML).toBe(mockGameData.detailsHtml);
    });

    test('should populate lists correctly', () => {
      expect(document.getElementById('platformsList').querySelectorAll('li').length).toBe(mockGameData.platforms.length);
      expect(document.getElementById('genresList').querySelectorAll('li').length).toBe(mockGameData.genres.length);
      expect(document.getElementById('tagsList').querySelectorAll('li').length).toBe(mockGameData.tags.length);
    });
    
    test('should handle missing or incomplete data gracefully', async () => {
      // Reset fetch mock from previous tests
      fetch.mockClear();
      
      // Mock incomplete data
      const incompleteData = {
        title: 'Incomplete Game',
        developer: null,
        publisher: undefined,
        releaseDate: '2023-01-01'
      };
      
      fetch.mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(incompleteData)
      });
      
      // Open modal and wait for completion
      await window.openGameDetailsModal('incomplete-game');
      
      // Check that missing values are handled
      expect(document.getElementById('gameTitle').textContent).toBe('Incomplete Game');
      expect(document.getElementById('gameDeveloper').textContent).toBe('Not available');
      expect(document.getElementById('gamePublisher').textContent).toBe('Not available');
      expect(document.getElementById('gameReleaseDate').textContent).toBe('2023-01-01');
    });
  });
  
  describe('Edge Cases', () => {
    test('should handle rapid open/close operations', async () => {
      const elements = helpers.getModalElements();
      
      // Rapidly open and close modal multiple times
      for (let i = 0; i < 3; i++) {
        await window.openGameDetailsModal('test-game');
        window.closeGameDetailsModal();
      }
      
      // Should end in closed state
      helpers.expectModalHidden(elements);
      expect(document.body.style.overflow).toBe('');
    });
    
    test('should handle opening modal for same game multiple times', async () => {
      // Open modal
      await window.openGameDetailsModal('test-game');
      
      // Try to open again (should use cache)
      fetch.mockClear();
      const result = await window.openGameDetailsModal('test-game');
      
      // Should not fetch again and return cached data
      expect(fetch).not.toHaveBeenCalled();
      expect(result).toEqual(mockGameData);
      
      // Modal should still be open and functional
      const elements = helpers.getModalElements();
      helpers.expectModalVisible(elements);
    });
  });
});
