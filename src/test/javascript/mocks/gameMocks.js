/**
 * Mock game data for testing
 */

// Sample complete game data object
const mockGameData = {
  id: 'test-game-1',
  title: 'Test Game Title',
  description: 'This is a test game description',
  imageUrl: 'https://example.com/test-game/original.jpg',
  thumbnailUrl: 'https://example.com/test-game/thumbnail.jpg',
  developer: 'Test Developer',
  publisher: 'Test Publisher',
  genres: ['Action', 'Adventure'],
  tags: ['Single-player', 'Story-rich'],
  metacriticScore: 85,
  ageRating: 'PEGI 16',
  userScore: 8.7,
  releaseDate: '2023-05-15',
  platforms: ['PC', 'PlayStation 5', 'Xbox Series X'],
  price: 59.99,
  isMultiplayer: true,
  playtimeHours: 40,
  detailsHtml: '<p>This is the HTML content for the game details.</p><p>It includes multiple paragraphs.</p>'
};

// Sample game data with missing fields
const mockIncompleteGameData = {
  id: 'test-game-2',
  title: 'Incomplete Game',
  description: 'This game has missing data',
  imageUrl: 'https://example.com/incomplete-game/original.jpg',
  developer: 'Unknown Developer',
  genres: ['RPG'],
  metacriticScore: 70
  // Other fields are intentionally missing
};

// Sample API response for successful request
const mockSuccessResponse = {
  ok: true,
  json: () => Promise.resolve(mockGameData)
};

// Sample API response for failed request
const mockErrorResponse = {
  ok: false,
  status: 404,
  statusText: 'Not Found'
};

module.exports = {
  mockGameData,
  mockIncompleteGameData,
  mockSuccessResponse,
  mockErrorResponse
};
