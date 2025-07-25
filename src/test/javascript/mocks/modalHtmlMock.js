/**
 * Mock HTML structure for game details modal testing
 */

// This function returns a mock HTML structure that matches the game-details-modal.html fragment
function setupMockModalHtml() {
  return `
    <div class="modal-overlay hidden" id="modalOverlay"></div>
    
    <div class="modal hidden" id="gameDetailsModal">
      <div class="modal-content">
        <button class="modal-close-btn" id="modalCloseBtn" aria-label="Close modal">
          <span aria-hidden="true">&times;</span>
        </button>            
        
        <div class="loading-spinner" id="loadingSpinner">
          <div class="spinner"></div>
          <p>Loading game details...</p>
        </div>
        
        <div class="game-details hidden" id="gameDetailsContent">
          <header class="game-header">
            <div class="game-title-section">
              <h2 class="game-title" id="gameTitle">Game Title</h2>
              <div class="game-meta">
                <span class="developer" id="gameDeveloper">Developer</span>
                <span class="separator">|</span>
                <span class="publisher" id="gamePublisher">Publisher</span>
                <span class="separator">|</span>
                <span class="release-date" id="gameReleaseDate">Release Date</span>
              </div>
            </div>
            <div class="game-scores">
              <div class="metacritic-score">
                <span class="score-label">Metacritic</span>
                <span class="score-value" id="metacriticScore">85</span>
              </div>
              <div class="user-score">
                <span class="score-label">User Score</span>
                <span class="score-value" id="userScore">8.7</span>
              </div>
            </div>
          </header>
          
          <section class="game-media">
            <div class="main-image-container">
              <img id="gameMainImage" class="main-image" src="" alt="Game screenshot">
            </div>
            <div class="thumbnail-gallery" id="thumbnailGallery" style="display: none;"></div>
          </section>
          
          <section class="game-info-container">
            <div class="game-info-main">
              <div class="game-description" id="gameDescription"></div>                        
            </div>
            
            <aside class="game-info-sidebar">
              <div class="info-block platforms-block">
                <h4>Platforms</h4>
                <ul class="platforms-list" id="platformsList"></ul>
              </div>
              
              <div class="info-block genres-block">
                <h4>Genres</h4>
                <ul class="genres-list" id="genresList"></ul>
              </div>
              
              <div class="info-block tags-block">
                <h4>Tags</h4>
                <ul class="tags-list" id="tagsList"></ul>
              </div>
              
              <div class="info-block release-date-block">
                <h4>Release Date</h4>
                <div class="release-date" id="releaseDateDisplay">Not available</div>
              </div>
              
              <div class="info-block details-block">
                <h4>Details</h4>
                <table class="details-table">
                  <tr>
                    <th>Age Rating</th>
                    <td id="ageRating">PEGI 18</td>
                  </tr>
                  <tr>
                    <th>Multiplayer</th>
                    <td id="multiplayer">Yes</td>
                  </tr>
                  <tr>
                    <th>Average Playtime</th>
                    <td id="playtime">40+ hours</td>
                  </tr>
                  <tr>
                    <th>Price</th>
                    <td id="price">$59.99</td>
                  </tr>
                </table>
              </div>
            </aside>
          </section>
          
          <footer class="modal-footer">
            <div class="related-games">
              <h4>You might also like</h4>
              <div class="related-games-container" id="relatedGamesContainer"></div>
            </div>
          </footer>
        </div>
      </div>
    </div>
    
    <!-- Test elements for dataHandling tests -->
    <div id="testElement">Test Element</div>
    <ul id="testList"></ul>
  `;
}

module.exports = {
  setupMockModalHtml
};
