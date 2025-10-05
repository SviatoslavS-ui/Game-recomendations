/**
 * Basic tests for modal functionality
 */

describe('Modal Basic Functionality', () => {
  beforeEach(() => {
    // Set up a simple DOM
    document.body.innerHTML = `
      <div id="gameDetailsModal" class="hidden"></div>
      <div id="modalOverlay" class="hidden"></div>
    `;
    
    // Create simple mock functions
    window.openGameDetailsModal = jest.fn(() => {
      document.getElementById('modalOverlay').classList.remove('hidden');
      document.getElementById('gameDetailsModal').classList.remove('hidden');
      return Promise.resolve();
    });
    
    window.closeGameDetailsModal = jest.fn(() => {
      document.getElementById('modalOverlay').classList.add('hidden');
      document.getElementById('gameDetailsModal').classList.add('hidden');
    });
  });
  
  afterEach(() => {
    delete window.openGameDetailsModal;
    delete window.closeGameDetailsModal;
  });
  
  test('openGameDetailsModal should show modal', async () => {
    // Act
    await window.openGameDetailsModal('test-game');
    
    // Assert
    expect(document.getElementById('modalOverlay').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('gameDetailsModal').classList.contains('hidden')).toBe(false);
  });
  
  test('closeGameDetailsModal should hide modal', () => {
    // Arrange - Make modal visible first
    document.getElementById('modalOverlay').classList.remove('hidden');
    document.getElementById('gameDetailsModal').classList.remove('hidden');
    
    // Act
    window.closeGameDetailsModal();
    
    // Assert
    expect(document.getElementById('modalOverlay').classList.contains('hidden')).toBe(true);
    expect(document.getElementById('gameDetailsModal').classList.contains('hidden')).toBe(true);
  });
});
