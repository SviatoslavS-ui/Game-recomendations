/**
 * Tests for data handling functions in game-details-modal.js
 */

const { mockGameData, mockIncompleteGameData, mockSuccessResponse, mockErrorResponse } = require('../../mocks/gameMocks');

describe('Data Handling Functions', () => {
  // Setup before each test is handled in setupTests.js
  
  describe('setElementText', () => {
    test('should set text content of element with given ID', () => {
      // Call the function
      window.setElementText('testElement', 'Test Value');
      
      // Check that the element's text content was set
      expect(document.getElementById('testElement').textContent).toBe('Test Value');
    });
    
    test('should set "Not available" if value is falsy', () => {
      // Call the function with undefined value
      window.setElementText('testElement', undefined);
      
      // Check that the element's text content was set to "Not available"
      expect(document.getElementById('testElement').textContent).toBe('Not available');
    });
    
    test('should do nothing if element does not exist', () => {
      // This should not throw an error
      expect(() => {
        window.setElementText('nonExistentElement', 'Test Value');
      }).not.toThrow();
    });
  });
  
  describe('populateList', () => {
    test('should populate list with given items', () => {
      const items = ['Item 1', 'Item 2', 'Item 3'];
      
      // Call the function
      window.populateList('testList', items);
      
      // Check that the list was populated
      const listItems = document.getElementById('testList').querySelectorAll('li');
      expect(listItems.length).toBe(3);
      for (let i = 0; i < listItems.length; i++) {
        expect(listItems[i].textContent).toBe(items[i]);
      }
    });
    
    test('should clear existing content before populating', () => {
      // Add some initial content
      const listElement = document.getElementById('testList');
      listElement.innerHTML = '<li>Initial item</li>';
      
      const items = ['New Item 1', 'New Item 2'];
      
      // Call the function
      window.populateList('testList', items);
      
      // Check that the initial content was cleared
      const listItems = listElement.querySelectorAll('li');
      expect(listItems.length).toBe(2);
      for (let i = 0; i < listItems.length; i++) {
        expect(listItems[i].textContent).toBe(items[i]);
      }
    });
    
    test('should do nothing if container does not exist', () => {
      // This should not throw an error
      expect(() => {
        window.populateList('nonExistentList', ['Item 1']);
      }).not.toThrow();
    });
    
    test('should do nothing if items is empty or null', () => {
      const listElement = document.getElementById('testList');
      listElement.innerHTML = '<li>Initial item</li>';
      
      // Call the function with empty array
      window.populateList('testList', []);
      
      // Check that the list was not modified
      expect(listElement.innerHTML).toBe('<li>Initial item</li>');
      
      // Call the function with null
      window.populateList('testList', null);
      
      // Check that the list was not modified
      expect(listElement.innerHTML).toBe('<li>Initial item</li>');
    });
  });
  
  describe('displayGameDetails', () => {
    test('should set text content for all game details', () => {
      // Call the function
      window.displayGameDetails(mockGameData);
      
      // Check that all text content was set correctly
      expect(document.getElementById('gameTitle').textContent).toBe(mockGameData.title);
      expect(document.getElementById('gameDeveloper').textContent).toBe(mockGameData.developer);
      expect(document.getElementById('gamePublisher').textContent).toBe(mockGameData.publisher);
      expect(document.getElementById('gameReleaseDate').textContent).toBe(mockGameData.releaseDate);
      expect(document.getElementById('releaseDateDisplay').textContent).toBe(mockGameData.releaseDate);
      expect(document.getElementById('metacriticScore').textContent).toBe(mockGameData.metacriticScore.toString());
      expect(document.getElementById('userScore').textContent).toBe(mockGameData.userScore.toString());
      expect(document.getElementById('ageRating').textContent).toBe(mockGameData.ageRating);
      expect(document.getElementById('multiplayer').textContent).toBe('No');
      expect(document.getElementById('playtime').textContent).toBe('Not available');
      expect(document.getElementById('price').textContent).toBe(mockGameData.price.toString());
    });
    
    test('should handle incomplete game data', () => {
      // Call the function with incomplete data
      window.displayGameDetails(mockIncompleteGameData);
      
      // Check that incomplete values are displayed as-is (not converted to 'Not available')
      expect(document.getElementById('gameTitle').textContent).toBe(mockIncompleteGameData.title);
      expect(document.getElementById('gameDeveloper').textContent).toBe(mockIncompleteGameData.developer);
      expect(document.getElementById('gamePublisher').textContent).toBe('Not available');
    });
    
    test('should apply correct score classes based on metacritic score', () => {
      // Test high score
      const highScoreData = { ...mockGameData, metacriticScore: '90' };
      window.displayGameDetails(highScoreData);
      expect(document.getElementById('metacriticScore').className).toContain('score-high');
      
      // Test medium score
      const mediumScoreData = { ...mockGameData, metacriticScore: '65' };
      window.displayGameDetails(mediumScoreData);
      expect(document.getElementById('metacriticScore').className).toContain('score-medium');
      
      // Test low score
      const lowScoreData = { ...mockGameData, metacriticScore: '40' };
      window.displayGameDetails(lowScoreData);
      expect(document.getElementById('metacriticScore').className).toContain('score-low');
    });
    
    test('should apply correct score classes based on user score', () => {
      // Test high score
      const highScoreData = { ...mockGameData, userScore: '9.0' };
      window.displayGameDetails(highScoreData);
      expect(document.getElementById('userScore').className).toContain('score-high');
      
      // Test medium score
      const mediumScoreData = { ...mockGameData, userScore: '6.5' };
      window.displayGameDetails(mediumScoreData);
      expect(document.getElementById('userScore').className).toContain('score-medium');
      
      // Test low score
      const lowScoreData = { ...mockGameData, userScore: '4.0' };
      window.displayGameDetails(lowScoreData);
      expect(document.getElementById('userScore').className).toContain('score-low');
    });
    
    test('should set main image if available', () => {
      // Call the function
      window.displayGameDetails(mockGameData);
      
      // Check that the image was set correctly
      const mainImage = document.getElementById('gameMainImage');
      expect(mainImage.src).toContain(mockGameData.imageUrl);
      expect(mainImage.alt).toBe(mockGameData.title);
    });
    
    test('should set description from HTML content if available', () => {
      // Call the function
      window.displayGameDetails(mockGameData);
      
      // Check that the description was set correctly
      expect(document.getElementById('gameDescription').innerHTML).toBe(mockGameData.detailsHtml);
    });
    
    test('should set description from description field if detailsHtml is not available', () => {
      const noHtmlData = { ...mockGameData, detailsHtml: null };
      
      // Call the function
      window.displayGameDetails(noHtmlData);
      
      // Check that the description was set correctly
      expect(document.getElementById('gameDescription').innerHTML).toBe(noHtmlData.description);
    });
    
    test('should show "No description available" if no description is available', () => {
      const noDescriptionData = { ...mockGameData, detailsHtml: null, description: null };
      
      // Call the function
      window.displayGameDetails(noDescriptionData);
      
      // Check that the default message was set
      expect(document.getElementById('gameDescription').innerHTML).toBe('<p>No description available</p>');
    });
    
    test('should populate lists for platforms, genres, and tags', () => {
      // Call the function
      window.displayGameDetails(mockGameData);
      
      // Check that the lists were populated
      expect(document.getElementById('platformsList').querySelectorAll('li').length).toBe(mockGameData.platforms.length);
      expect(document.getElementById('genresList').querySelectorAll('li').length).toBe(mockGameData.genres.length);
      expect(document.getElementById('tagsList').querySelectorAll('li').length).toBe(mockGameData.tags.length);
    });
    
    test('should hide spinner and show content', async () => {
      // Initial state
      const spinner = document.getElementById('loadingSpinner');
      const content = document.getElementById('gameDetailsContent');
      spinner.style.display = 'flex';
      content.classList.add('hidden');
      
      // Call the function
      window.displayGameDetails(mockGameData);
      
      // Wait for the 200ms delay in displayGameDetails to complete
      await new Promise(resolve => setTimeout(resolve, 250));
      
      // Check that spinner is hidden and content is shown
      expect(spinner.style.display).toBe('none');
      expect(content.classList.contains('hidden')).toBe(false);
    });
  });
  
  describe('loadGameDetails', () => {
    beforeEach(() => {
      // Mock fetch
      fetch.mockResolvedValue(mockSuccessResponse);
    });
    
    test('should fetch game details from API', async () => {
      // Call the function
      await window.loadGameDetails('test-game');
      
      // Check that fetch was called with the correct URL
      expect(fetch).toHaveBeenCalledWith('/api/games/test-game/details');
    });
    

    test('should populate DOM elements with fetched data', async () => {
      // Clear cache to ensure fresh fetch
      if (window.gameDetailsCache) {
        delete window.gameDetailsCache['test-game'];
      }
      
      // Call the function and wait for completion
      await window.loadGameDetails('test-game');      
      await new Promise(resolve => setTimeout(resolve, 250));
      
      // Check that DOM elements are populated with the fetched data
      expect(document.getElementById('gameTitle').textContent).toBe(mockGameData.title);
      expect(document.getElementById('gameDeveloper').textContent).toBe(mockGameData.developer);
      expect(document.getElementById('gamePublisher').textContent).toBe(mockGameData.publisher);
    });
    
    test('should handle fetch errors', async () => {
      // Mock fetch to reject
      fetch.mockRejectedValue(new Error('Network error'));
      
      // Call the function and expect it to throw
      await expect(window.loadGameDetails('test-game')).rejects.toThrow('Network error');
      
      // Check that the error message was displayed
      expect(document.getElementById('gameDetailsContent').innerHTML).toContain('Error loading game details');
    });
    
    test('should handle non-ok responses', async () => {
      // Mock fetch to return non-ok response
      fetch.mockResolvedValue(mockErrorResponse);
      
      // Call the function and expect it to throw
      await expect(window.loadGameDetails('test-game')).rejects.toThrow('Error: 404');
    });
  });
});