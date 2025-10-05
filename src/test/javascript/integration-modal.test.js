/**
 * Integration test for Game Details Modal
 * This test loads the actual module code into JSDOM
 */

const fs = require('fs');
const path = require('path');

describe('Game Details Modal Integration', () => {
  // Store original window properties to restore later
  let originalWindowProps = {};
  
  beforeEach(() => {
    // Set up a complete DOM structure needed for all tests
    document.body.innerHTML = `
      <div id="gameDetailsModal" class="hidden">
        <div id="modalContent">
          <div id="loadingSpinner" style="display: none;"></div>
          <div id="gameDetailsContent" class="hidden">
            <h2 id="gameTitle"></h2>
            <p id="gameDeveloper"></p>
            <p id="gamePublisher"></p>
            <p id="gameReleaseDate"></p>
            <div id="metacriticScore"></div>
            <div id="userScore"></div>
            <p id="ageRating"></p>
            <p id="multiplayer"></p>
            <p id="playtime"></p>
            <p id="price"></p>
            <img id="gameMainImage" />
            <div id="gameDescription"></div>
            <ul id="platformsList"></ul>
            <ul id="genresList"></ul>
            <ul id="tagsList"></ul>
            <p id="releaseDateDisplay"></p>
            <div id="relatedGamesContainer"></div>
          </div>
        </div>
      </div>
      <div id="modalOverlay" class="hidden"></div>
      <div id="modalCloseBtn"></div>
    `;
    
    // Save original window properties
    originalWindowProps = {
      openGameDetailsModal: window.openGameDetailsModal,
      closeGameDetailsModal: window.closeGameDetailsModal
    };
    
    // Mock global dependencies
    global.createDebugger = jest.fn(() => jest.fn());
    
    // Define APP_CONFIG with the same values as in utils.js
    global.APP_CONFIG = {
      ENDPOINTS: {
        RECOMMENDATIONS: '/recommendations/results',
        GAME_DETAILS: '/api/games/{id}/details',
        RELATED_GAMES: '/recommendations/{id}/related'
      }
    };
    
    // Mock fetch API
    global.fetch = jest.fn().mockImplementation((url) => {
      // Extract the endpoint pattern from APP_CONFIG
      const gameDetailsPattern = APP_CONFIG.ENDPOINTS.GAME_DETAILS.split('{id}')[0];
      const relatedGamesPattern = APP_CONFIG.ENDPOINTS.RELATED_GAMES.split('{id}')[0];
      
      if (url.includes(gameDetailsPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({
            id: 'test-game',
            title: 'Test Game',
            developer: 'Test Developer',
            metacriticScore: '85',
            imageUrl: 'http://example.com/image.jpg'
          })
        });
      } else if (url.includes(relatedGamesPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve([
            { id: 'related-1', title: 'Related Game 1' },
            { id: 'related-2', title: 'Related Game 2' }
          ])
        });
      }
      return Promise.reject(new Error(`Unhandled fetch URL: ${url}`));
    });
    
    // Load the actual module via script tag
    const scriptContent = fs.readFileSync(
      path.join(__dirname, '../../main/resources/static/js/game-details-modal.js'), 
      'utf-8'
    );
    
    // Create a script element and append it to document
    const scriptEl = document.createElement('script');
    scriptEl.textContent = scriptContent;
    document.head.appendChild(scriptEl);
    
    // Trigger DOMContentLoaded to initialize event listeners
    document.dispatchEvent(new Event('DOMContentLoaded'));
  });
  
  afterEach(() => {
    // Restore original window properties
    Object.keys(originalWindowProps).forEach(key => {
      if (originalWindowProps[key] === undefined) {
        delete window[key];
      } else {
        window[key] = originalWindowProps[key];
      }
    });
    
    // Reset mocks
    jest.clearAllMocks();
  });
  
  // Helper function for parameterized game data tests
  async function testGameDataDisplay(testCase) {
    // Mock fetch to return the test case's game data
    global.fetch.mockImplementation((url) => {
      const gameDetailsPattern = APP_CONFIG.ENDPOINTS.GAME_DETAILS.split('{id}')[0];
      const relatedGamesPattern = APP_CONFIG.ENDPOINTS.RELATED_GAMES.split('{id}')[0];
      
      if (url.includes(gameDetailsPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve(testCase.gameData)
        });
      } else if (url.includes(relatedGamesPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve([])
        });
      }
      return Promise.reject(new Error(`Unhandled fetch URL: ${url}`));
    });
    
    // Act - Call the actual function
    await window.openGameDetailsModal('test-game');
    
    // Assert - Modal should be visible
    expect(document.getElementById('modalOverlay').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('gameDetailsModal').classList.contains('hidden')).toBe(false);
    
    // Run all assertions from the test case
    testCase.assertions();
  }
  
  // Define game data test cases
  const gameDataTestCases = [
    {
      name: 'displays complete game data correctly',
      gameData: {
        id: 'test-game',
        title: 'Complete Game',
        developer: 'Test Developer',
        publisher: 'Test Publisher',
        releaseDate: '2023-10-05',
        metacriticScore: '85',
        userScore: '8.5',
        ageRating: 'PEGI 18',
        multiplayer: true,
        playtime: '40 hours',
        price: '$59.99',
        imageUrl: 'http://example.com/image.jpg',
        description: '<p>Game description</p>',
        platforms: ['PC', 'PS5', 'Xbox Series X'],
        genres: ['RPG', 'Action'],
        tags: ['Open World', 'Fantasy']
      },
      assertions: () => {
        // Check basic text content
        expect(document.getElementById('gameTitle').textContent).toBe('Complete Game');
        expect(document.getElementById('gameDeveloper').textContent).toBe('Test Developer');
        expect(document.getElementById('gamePublisher').textContent).toBe('Test Publisher');
        expect(document.getElementById('gameReleaseDate').textContent).toBe('2023-10-05');
        expect(document.getElementById('metacriticScore').textContent).toBe('85');
        expect(document.getElementById('userScore').textContent).toBe('8.5');
        expect(document.getElementById('ageRating').textContent).toBe('PEGI 18');
        expect(document.getElementById('multiplayer').textContent).toBe('Yes');
        expect(document.getElementById('playtime').textContent).toBe('40 hours');
        expect(document.getElementById('price').textContent).toBe('$59.99');
        
        // Check image
        expect(document.getElementById('gameMainImage').src).toBe('http://example.com/image.jpg');
        
        // Check lists
        expect(document.getElementById('platformsList').children.length).toBe(3);
        expect(document.getElementById('genresList').children.length).toBe(2);
        expect(document.getElementById('tagsList').children.length).toBe(2);
      }
    },
    {
      name: 'handles missing fields gracefully',
      gameData: {
        id: 'incomplete-game',
        title: 'Incomplete Game',
        // Missing most fields
      },
      assertions: () => {
        // Check that required fields are present
        expect(document.getElementById('gameTitle').textContent).toBe('Incomplete Game');
        
        // Check that missing fields show "Not available"
        expect(document.getElementById('gameDeveloper').textContent).toBe('Not available');
        expect(document.getElementById('gamePublisher').textContent).toBe('Not available');
        expect(document.getElementById('ageRating').textContent).toBe('Not available');
        
        // Check that lists are empty
        expect(document.getElementById('platformsList').children.length).toBe(0);
        expect(document.getElementById('genresList').children.length).toBe(0);
      }
    },
    {
      name: 'handles null values gracefully',
      gameData: {
        id: 'null-values-game',
        title: 'Null Values Game',
        developer: null,
        publisher: null,
        releaseDate: null,
        metacriticScore: null,
        platforms: null,
        genres: null
      },
      assertions: () => {
        // Check that null fields show "Not available"
        expect(document.getElementById('gameTitle').textContent).toBe('Null Values Game');
        expect(document.getElementById('gameDeveloper').textContent).toBe('Not available');
        expect(document.getElementById('gamePublisher').textContent).toBe('Not available');
        expect(document.getElementById('gameReleaseDate').textContent).toBe('Not available');
        expect(document.getElementById('metacriticScore').textContent).toBe('Not available');
      }
    },
    {
      name: 'handles HTML content in description',
      gameData: {
        id: 'html-game',
        title: 'HTML Game',
        detailsHtml: '<h2>Game Details</h2><p>This is <strong>formatted</strong> content.</p>'
      },
      assertions: () => {
        // Check that HTML content is rendered correctly
        const descriptionEl = document.getElementById('gameDescription');
        expect(descriptionEl.innerHTML).toContain('<h2>Game Details</h2>');
        expect(descriptionEl.innerHTML).toContain('<strong>formatted</strong>');
      }
    }
  ];
  
  // Generate tests for game data display
  gameDataTestCases.forEach(testCase => {
    test(`Game data display: ${testCase.name}`, async () => {
      await testGameDataDisplay(testCase);
    });
  });
  
  test('openGameDetailsModal shows modal and makes API call', async () => {
    // Act - Call the actual function
    await window.openGameDetailsModal('test-game');
    
    // Assert - Modal should be visible
    expect(document.getElementById('modalOverlay').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('gameDetailsModal').classList.contains('hidden')).toBe(false);
    
    // Assert - API call should be made with correct endpoint
    const expectedGameDetailsUrl = APP_CONFIG.ENDPOINTS.GAME_DETAILS.replace('{id}', 'test-game');
    expect(global.fetch).toHaveBeenCalledWith(expectedGameDetailsUrl);
  });
  
  test('closeGameDetailsModal hides the modal', () => {
    // Arrange - Make modal visible first
    document.getElementById('modalOverlay').classList.remove('hidden');
    document.getElementById('gameDetailsModal').classList.remove('hidden');
    
    // Act - Call the actual function
    window.closeGameDetailsModal();
    
    // Assert - Modal should be hidden
    expect(document.getElementById('modalOverlay').classList.contains('hidden')).toBe(true);
    expect(document.getElementById('gameDetailsModal').classList.contains('hidden')).toBe(true);
  });
  
  test('openGameDetailsModal fetches related games', async () => {
    // Act - Call the actual function
    await window.openGameDetailsModal('test-game');
    
    // Wait for all promises to resolve (including the related games fetch)
    await new Promise(process.nextTick);
    
    // Assert - Second API call should be made for related games
    const expectedRelatedGamesUrl = APP_CONFIG.ENDPOINTS.RELATED_GAMES.replace('{id}', 'test-game');
    expect(global.fetch).toHaveBeenCalledWith(expectedRelatedGamesUrl);
    
    // Assert - Related games should be displayed
    const relatedGamesContainer = document.getElementById('relatedGamesContainer');
    expect(relatedGamesContainer.innerHTML).toContain('Related Game 1');
    expect(relatedGamesContainer.innerHTML).toContain('Related Game 2');
  });
  
  test('openGameDetailsModal uses cache for repeated calls', async () => {
    // Act - First call
    await window.openGameDetailsModal('test-game');
    
    // Reset fetch mock to track new calls
    global.fetch.mockClear();
    
    // Act - Second call with same game ID
    await window.openGameDetailsModal('test-game');
    
    // Assert - Should not call game details API again
    const expectedGameDetailsUrl = APP_CONFIG.ENDPOINTS.GAME_DETAILS.replace('{id}', 'test-game');
    expect(global.fetch).not.toHaveBeenCalledWith(expectedGameDetailsUrl);
    
    // But should still call related games API
    const expectedRelatedGamesUrl = APP_CONFIG.ENDPOINTS.RELATED_GAMES.replace('{id}', 'test-game');
    expect(global.fetch).toHaveBeenCalledWith(expectedRelatedGamesUrl);
  });
  
  test('openGameDetailsModal handles API errors gracefully', async () => {
    // Arrange - Mock fetch to reject
    global.fetch.mockImplementationOnce(() => Promise.reject(new Error('API Error')));
    
    // Act & Assert - Should show error message
    await expect(window.openGameDetailsModal('error-game')).rejects.toThrow('API Error');
    
    // Check error message is displayed
    expect(document.getElementById('gameDetailsContent').innerHTML).toContain('Error loading game details');
  });

  // Helper function to run a parameterized related games test
  async function testRelatedGames(testCase) {
    // Mock fetch based on the test case
    global.fetch.mockImplementation((url) => {
      // Extract the endpoint patterns from APP_CONFIG
      const gameDetailsPattern = APP_CONFIG.ENDPOINTS.GAME_DETAILS.split('{id}')[0];
      const relatedGamesPattern = APP_CONFIG.ENDPOINTS.RELATED_GAMES.split('{id}')[0];
      
      if (url.includes(gameDetailsPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ id: 'test-game', title: 'Test Game' })
        });
      } else if (url.includes(relatedGamesPattern)) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve(testCase.relatedGames)
        });
      }
      return Promise.reject(new Error(`Unhandled fetch URL: ${url}`));
    });
    
    // Act
    await window.openGameDetailsModal('test-game');
    await new Promise(process.nextTick);
    
    // Assert based on the test case
    const relatedGamesContainer = document.getElementById('relatedGamesContainer');
    
    // Run all assertions from the test case
    testCase.assertions(relatedGamesContainer);
  }
  
  // Define test cases
  const relatedGamesTestCases = [
    {
      name: 'handles empty related games response gracefully',
      relatedGames: [], // Empty array
      assertions: (container) => {
        expect(container.innerHTML).toContain('No similar games found');
      }
    },
    {
      name: 'handles single related game correctly',
      relatedGames: [{ id: 'single-related', title: 'Single Related Game' }],
      assertions: (container) => {
        // Should contain the game title
        expect(container.innerHTML).toContain('Single Related Game');
        
        // Should have exactly one list item
        const listItems = container.querySelectorAll('li');
        expect(listItems.length).toBe(1);
        
        // The list item should have the correct class
        expect(listItems[0].className).toContain('related-game-item');
      }
    },
    {
      name: 'handles multiple related games correctly',
      relatedGames: [
        { id: 'related-1', title: 'Related Game 1' },
        { id: 'related-2', title: 'Related Game 2' },
        { id: 'related-3', title: 'Related Game 3' }
      ],
      assertions: (container) => {
        // Should contain all game titles
        expect(container.innerHTML).toContain('Related Game 1');
        expect(container.innerHTML).toContain('Related Game 2');
        expect(container.innerHTML).toContain('Related Game 3');
        
        // Should have exactly three list items
        const listItems = container.querySelectorAll('li');
        expect(listItems.length).toBe(3);
      }
    },
    {
      name: 'shows only top 3 related games when more are returned',
      relatedGames: [
        { id: 'related-1', title: 'Related Game 1', rating: 85 },
        { id: 'related-2', title: 'Related Game 2', rating: 92 },
        { id: 'related-3', title: 'Related Game 3', rating: 78 },
        { id: 'related-4', title: 'Related Game 4', rating: 90 },
        { id: 'related-5', title: 'Related Game 5', rating: 88 }
      ],
      assertions: (container) => {
        // Should contain only the top 3 rated games (2, 4, 5)
        expect(container.innerHTML).toContain('Related Game 2'); // 92
        expect(container.innerHTML).toContain('Related Game 4'); // 90
        expect(container.innerHTML).toContain('Related Game 5'); // 88
        
        // Should NOT contain the lower rated games
        expect(container.innerHTML).not.toContain('Related Game 1'); // 85
        expect(container.innerHTML).not.toContain('Related Game 3'); // 78
        
        // Should have exactly three list items
        const listItems = container.querySelectorAll('li');
        expect(listItems.length).toBe(3);
      }
    }
  ];
  
  // Generate tests from test cases
  relatedGamesTestCases.forEach(testCase => {
    test(testCase.name, async () => {
      await testRelatedGames(testCase);
    });
  });
});
