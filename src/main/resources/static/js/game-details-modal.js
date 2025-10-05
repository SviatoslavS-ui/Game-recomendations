/**
 * Game Details Modal JavaScript
 * Handles displaying game details in a modal popup
**/

// Use IIFE to create a local scope and prevent global variable conflicts
(function () {
    // Game details cache to avoid redundant API calls
    const gameDetailsCache = {};

    // Create a module-specific debug function
    const debug = createDebugger('Modal');

    document.addEventListener('DOMContentLoaded', function () {
        debug('Modal element exists:', !!document.getElementById('gameDetailsModal'));
        debug('Overlay element exists:', !!document.getElementById('modalOverlay'));
        debug('Close button exists:', !!document.getElementById('modalCloseBtn'));

        setupEventListeners();
    });

    function setupEventListeners() {
        const closeBtn = document.getElementById('modalCloseBtn');
        if (closeBtn) {
            closeBtn.addEventListener('click', closeGameDetailsModal);
        }

        const overlay = document.getElementById('modalOverlay');
        if (overlay) {
            overlay.addEventListener('click', closeGameDetailsModal);
        }

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                closeGameDetailsModal();
            }
        });

        debug('Setting up event listeners');
    }

    function closeGameDetailsModal() {
        document.getElementById('modalOverlay').classList.add('hidden');
        document.getElementById('gameDetailsModal').classList.add('hidden');

        document.body.style.overflow = '';
        debug('Game details modal closed');
    }

    // Helper function to show loading spinner and hide content
    function showLoadingState() {
        const spinner = document.getElementById('loadingSpinner');
        const content = document.getElementById('gameDetailsContent');

        if (spinner) spinner.style.display = 'flex';
        if (content) content.classList.add('hidden');

        debug('Loading spinner shown, content hidden');
    }

    // Helper function to hide loading spinner and show content
    function hideLoadingState() {
        const spinner = document.getElementById('loadingSpinner');
        const content = document.getElementById('gameDetailsContent');

        if (spinner) spinner.style.display = 'none';
        if (content) content.classList.remove('hidden');

        debug('Loading spinner hidden, content shown');
    }

    function openGameDetailsModal(gameId) {
        debug('Opening modal for game:', gameId);

        document.getElementById('modalOverlay').classList.remove('hidden');
        document.getElementById('gameDetailsModal').classList.remove('hidden');

        showLoadingState();

        document.body.style.overflow = 'hidden';
        debug('Game details modal opened, showing loading spinner');

        // Fetch game details from backend and return the promise
        return loadGameDetails(gameId);
    }

    function loadGameDetails(gameId) {
        // check cache first
        if (gameDetailsCache[gameId]) {
            debug('Using cached game details for:', gameId);
            displayGameDetails(gameDetailsCache[gameId]);
            return Promise.resolve(gameDetailsCache[gameId]);
        }

        // Fetch from API
        debug('Fetching game details from API for:', gameId);
        const endpoint = APP_CONFIG.ENDPOINTS.GAME_DETAILS.replace('{id}', gameId);
        return fetch(endpoint)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                gameDetailsCache[gameId] = data;
                displayGameDetails(data);
                return data;
            })
            .catch(error => {
                debug('Error fetching game details:', error);
                // Show error in modal
                document.getElementById('gameDetailsContent').innerHTML =
                    `<div class="error-message">Error loading game details: ${error.message}</div>`;
                document.getElementById('gameDetailsContent').style.display = 'block';
                throw error;
            });
    }

    function displayGameDetails(gameData) {
        // Guard clause for null/undefined gameData
        if (!gameData) {
            debug('No game data provided');
            document.getElementById('gameDetailsContent').innerHTML = 
                '<div class="error-message">Error: No game data available</div>';
            return;
        }
        
        // Set text content for various elements
        setElementText('gameTitle', gameData.title);
        setElementText('gameDeveloper', gameData.developer);
        setElementText('gamePublisher', gameData.publisher);
        setElementText('gameReleaseDate', gameData.releaseDate);

        // Always set text content for scores, but only apply styling if score exists
        setElementText('metacriticScore', gameData.metacriticScore);
        setElementText('userScore', gameData.userScore);
        
        const metacriticScoreEl = document.getElementById('metacriticScore');
        if (metacriticScoreEl && gameData.metacriticScore) {
            const score = parseInt(gameData.metacriticScore);
            metacriticScoreEl.className = 'score-value ' +
                (score >= 80 ? 'score-high' : (score >= 50 ? 'score-medium' : 'score-low'));
        }

        const userScoreEl = document.getElementById('userScore');
        if (userScoreEl && gameData.userScore) {
            const score = parseFloat(gameData.userScore); // Keep original 0-9.9 scale
            userScoreEl.className = 'score-value ' +
                (score >= 8.0 ? 'score-high' : (score >= 5.0 ? 'score-medium' : 'score-low'));
        }

        setElementText('ageRating', gameData.ageRating);
        setElementText('multiplayer', gameData.multiplayer ? 'Yes' : 'No');
        setElementText('playtime', gameData.playtime);
        setElementText('price', gameData.price);

        // Set main image if available
        const mainImage = document.getElementById('gameMainImage');
        if (mainImage && gameData.imageUrl) {
            mainImage.src = gameData.imageUrl;
            mainImage.alt = gameData.title;
            debug('Displaying game details:', gameData.title);
        } else {
            debug('Image not available:', mainImage ? 'Element exists' : 'Element missing', gameData.imageUrl ? 'URL exists' : 'URL missing');
        }

        // Set description from S3 HTML content
        const descriptionEl = document.getElementById('gameDescription');
        if (descriptionEl) {
            if (gameData.detailsHtml) {
                descriptionEl.innerHTML = gameData.detailsHtml;
                debug('Loaded HTML description from S3');
            } else if (gameData.description) {
                descriptionEl.innerHTML = gameData.description;
                debug('Using fallback text description');
            } else {
                descriptionEl.innerHTML = '<p>No description available</p>';
                debug('No description content available');
            }
        }

        if (gameData.platforms && gameData.platforms.length) {
            populateList('platformsList', gameData.platforms);
        }

        if (gameData.genres && gameData.genres.length) {
            populateList('genresList', gameData.genres);
        }

        if (gameData.tags && gameData.tags.length) {
            populateList('tagsList', gameData.tags);
        }

        if (gameData.releaseDate) {
            setElementText('releaseDateDisplay', gameData.releaseDate);
        }

        // Populate related games
        const relatedGamesContainer = document.getElementById('relatedGamesContainer');
        if (relatedGamesContainer) {
            // Clear container but preserve the title
            relatedGamesContainer.innerHTML = '<h3>You might also like</h3>';

            // Create a styled list for related games
            const relatedGamesList = document.createElement('ul');
            relatedGamesList.className = 'related-games-list';
            relatedGamesContainer.appendChild(relatedGamesList);

            // Fetch related games and populate the list
            getRelatedGames(gameData)
                .then(relatedGames => {
                    if (relatedGames && relatedGames.length > 0) {
                        // Related games are already filtered to top 3 by rating in getRelatedGames
                        relatedGames.forEach(game => {
                            const listItem = document.createElement('li');
                            listItem.className = 'related-game-item';
                            listItem.textContent = game.title;

                            // Add click event to open the game details
                            listItem.addEventListener('click', () => {
                                debug('Related game clicked:', game.title);

                                showLoadingState();

                                // Load full game details from API to get HTML content
                                loadGameDetails(game.id)
                                    .catch(error => {
                                        debug('Error loading related game details:', error);
                                    });
                            });

                            relatedGamesList.appendChild(listItem);
                        });
                    } else {
                        // Handle empty collection gracefully
                        const noGamesMsg = document.createElement('p');
                        noGamesMsg.textContent = 'No similar games found';
                        noGamesMsg.className = 'related-games-message';
                        relatedGamesContainer.appendChild(noGamesMsg);
                    }
                })
                .catch(error => {
                    debug('Error loading related games:', error);
                    relatedGamesContainer.innerHTML = `<p class="related-games-message" style="color: var(--danger-color)">Could not load related games</p>`;
                });
        }

        // Hide spinner and show the content with a slight delay for smooth transition
        setTimeout(() => {
            hideLoadingState();
        }, 300); // 300ms delay
    }

    function setElementText(id, value) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = value || 'Not available';
        }
    }

    function populateList(containerId, items) {
        const container = document.getElementById(containerId);
        if (!container || !items || !items.length) {
            return;
        }

        container.innerHTML = '';

        items.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item;
            container.appendChild(li);
        });
    }
    
    /**
     * Get related games for a specific game and return only the top 3 by rating
     * @param {Object} game - The game object to find related games for
     * @returns {Promise<Array>} - Promise resolving to array of top 3 related games
     */
    function getRelatedGames(game) {
        const endpoint = APP_CONFIG.ENDPOINTS.RELATED_GAMES.replace('{id}', game.id);

        return fetch(endpoint)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }
                return response.json();
            })
            .then(games => {
                // Filter to top 3 games by rating
                return [...games]
                    .sort((a, b) => (b.rating || 0) - (a.rating || 0))
                    .slice(0, 3);
            });
    }

    // Make functions globally accessible
    window.openGameDetailsModal = openGameDetailsModal;
    window.closeGameDetailsModal = closeGameDetailsModal;
})();