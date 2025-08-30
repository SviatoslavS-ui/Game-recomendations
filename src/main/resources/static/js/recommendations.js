/**
 * Game Recommender - Recommendations Page JavaScript
 * Handles filter selection and recommendation requests
 * Uses server-side rendering for game cards
 */

const RECOMMENDATIONS_DEBUG = true;

function debug(message, data) {
    if (RECOMMENDATIONS_DEBUG) {
        console.log(`[Recommendations Debug] ${message}`, data || '');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    debug('Recommendations page loaded');
    // Selected filters storage
    const selectedFilters = {
        genre: [],
        tag: []
    };

    // Get DOM elements
    const filterButtons = document.querySelectorAll('.filter-button');
    const getRecommendationsButton = document.getElementById('get-recommendations');
    const resultsContainer = document.getElementById('recommendation-results');
    const loadingOverlay = document.getElementById('loading-overlay');
    
    debug('DOM elements found:', {
        filterButtons: filterButtons.length,
        getRecommendationsButton: !!getRecommendationsButton,
        resultsContainer: !!resultsContainer,
        loadingOverlay: !!loadingOverlay
    });

    // Handle filter button clicks
    filterButtons.forEach(button => {
        button.addEventListener('click', () => {
            const filterType = button.dataset.filter;
            const filterValue = button.dataset.value;

            button.classList.toggle('active');

            // Use filter value directly - no mapping needed
            const formattedValue = filterValue;
            
            // Update selected filters
            if (button.classList.contains('active')) {
                // Add filter if not already in the array
                if (!selectedFilters[filterType].includes(formattedValue)) {
                    selectedFilters[filterType].push(formattedValue);
                    debug(`Added ${filterType} filter:`, formattedValue);
                }
            } else {
                // Remove filter
                const index = selectedFilters[filterType].indexOf(formattedValue);
                if (index > -1) {
                    selectedFilters[filterType].splice(index, 1);
                    debug(`Removed ${filterType} filter:`, formattedValue);
                }
            }
            
            debug('Current selected filters:', JSON.parse(JSON.stringify(selectedFilters)));
        });
    });

    // Handle get recommendations button click
    getRecommendationsButton.addEventListener('click', async () => {
        debug('Get recommendations button clicked');
        debug('Sending filters to server:', JSON.parse(JSON.stringify(selectedFilters)));
        try {
            // Show loading state
            getRecommendationsButton.textContent = 'Finding Games...';
            getRecommendationsButton.disabled = true;
            if (loadingOverlay) loadingOverlay.classList.add('active');
            
            // Make API request to get recommendations
            const response = await fetch('/recommendations/results', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(selectedFilters)
            });

            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }

            // Get HTML fragment from server
            const htmlFragment = await response.text();
            debug('Received HTML fragment from server', { length: htmlFragment.length });
            
            // Update results container with server-rendered HTML
            resultsContainer.innerHTML = htmlFragment;
            resultsContainer.style.display = 'grid';
            debug('Updated results container with new HTML');
            
            // Add click handlers to the newly added game cards
            attachGameCardHandlers();
            debug('Attached click handlers to game cards');
            
        } catch (error) {
            console.error('Failed to get recommendations:', error);
            debug('Error getting recommendations:', error.message);
            // Show error message to user
            resultsContainer.innerHTML = `
                <div class="no-results error-message">
                    <p>Failed to get recommendations. Please try again.</p>
                    <p class="error-details">${error.message}</p>
                </div>
            `;
            resultsContainer.style.display = 'block';
        } finally {
            // Reset button state
            getRecommendationsButton.textContent = 'Get Recommendations';
            getRecommendationsButton.disabled = false;
            if (loadingOverlay) loadingOverlay.classList.remove('active');
        }
    });

    /**
     * Attach click handlers to game cards
     */
    function attachGameCardHandlers() {
        const gameCards = document.querySelectorAll('#recommendation-results .game-card');
        debug('Found game cards to attach handlers to:', gameCards.length);
        
        gameCards.forEach(card => {
            card.addEventListener('click', () => {
                const gameId = card.getAttribute('data-game-id');
                const gameTitle = card.querySelector('.game-title')?.textContent || 'Unknown';
                debug('Game card clicked:', { id: gameId, title: gameTitle });
                
                if (gameId && typeof openGameDetailsModal === 'function') {
                    debug('Opening game details modal for:', gameId);
                    openGameDetailsModal(gameId);
                } else {
                    debug('Cannot open modal:', { 
                        hasGameId: !!gameId, 
                        hasModalFunction: typeof openGameDetailsModal === 'function' 
                    });
                }
            });
        });
    }
});