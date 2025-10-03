/**
 * Game Recommender - Recommendations Page JavaScript
 * Handles filter selection and recommendation requests
 * Uses server-side rendering for game cards
 */

// Use IIFE to create a local scope and prevent global variable conflicts
(function () {
    // Create a module-specific debug function
    const debug = createDebugger('Recommendations');

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
            button.addEventListener('click', handleFilterButtonClick.bind(null, button, selectedFilters));
        });

        /**
         * Handle filter button click events
         * @param {HTMLElement} button - The clicked filter button
         * @param {Object} selectedFilters - The current filter state
         */
        function handleFilterButtonClick(button, selectedFilters) {
            const filterType = button.dataset.filter;
            const filterValue = button.dataset.value;

            button.classList.toggle('active');

            // Update selected filters
            if (button.classList.contains('active')) {
                // Add filter if not already in the array
                if (!selectedFilters[filterType].includes(filterValue)) {
                    selectedFilters[filterType].push(filterValue);
                    debug(`Added ${filterType} filter:`, filterValue);
                }
            } else {
                // Remove filter
                const index = selectedFilters[filterType].indexOf(filterValue);
                if (index > -1) {
                    selectedFilters[filterType].splice(index, 1);
                    debug('Removed filter:', filterType, filterValue);
                }
            }

            debug('Selected filters updated:', { ...selectedFilters });
        }

        // Handle get recommendations button click
        getRecommendationsButton.addEventListener('click', () => fetchRecommendations(selectedFilters));

        /**
         * Fetch recommendations from the server based on selected filters
         * @param {Object} filters - The selected genre and tag filters
         * @returns {Promise<void>}
         */
        async function fetchRecommendations(filters) {
            debug('Get recommendations button clicked');
            debug('Sending filters to server:', { ...filters });

            try {
                // Show loading state
                setLoadingState(true);

                // Make API request to get recommendations
                const response = await fetch(APP_CONFIG.ENDPOINTS.RECOMMENDATIONS, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(filters)
                });

                if (!response.ok) {
                    throw new Error(`Error: ${response.status}`);
                }

                // Get HTML fragment from server
                const htmlFragment = await response.text();
                debug('Received HTML fragment from server', { length: htmlFragment.length });

                // Update results container with server-rendered HTML
                updateResultsContainer(htmlFragment);

                // Add click handlers to the newly added game cards
                attachGameCardHandlers();
                debug('Attached click handlers to game cards');

            } catch (error) {
                console.error('Failed to get recommendations:', error);
                debug('Error fetching recommendations:', error);
                showErrorMessage(error.message);
            } finally {
                // Reset button state
                setLoadingState(false);
            }
        }

        /**
         * Set the loading state of the UI
         * @param {boolean} isLoading - Whether the UI should show loading state
         */
        function setLoadingState(isLoading) {
            getRecommendationsButton.textContent = isLoading ? 'Finding Games...' : 'Get Recommendations';
            getRecommendationsButton.disabled = isLoading;
            loadingOverlay?.classList.toggle('active', isLoading);
        }

        /**
     * Update the results container with new HTML content
     * @param {string} htmlContent - The HTML content to display
     */
        function updateResultsContainer(htmlContent) {
            resultsContainer.innerHTML = htmlContent;
            resultsContainer.style.display = 'grid';
            debug('Updated results container with new HTML');

            // Scroll to the recommendation section
            scrollToRecommendations();
        }

        /**
         * Scroll to the recommendation results section with an offset of 50px
         * to ensure the title is visible
         */
        function scrollToRecommendations() {
            const recommendationSection = document.querySelector('.recommendation-section');
            if (recommendationSection) {
                debug('Scrolling to recommendation section with offset');
                const rect = recommendationSection.getBoundingClientRect();
                const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
                const targetPosition = scrollTop + rect.top - 100; // 50px offset
                // Scroll to the calculated position
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        }

        /**
         * Show an error message in the results container
         * @param {string} errorMessage - The error message to display
         */
        function showErrorMessage(errorMessage) {
            resultsContainer.innerHTML = `
            <div class="no-results error-message">
                <p>Failed to get recommendations. Please try again.</p>
                <p class="error-details">${errorMessage}</p>
            </div>
        `;
            resultsContainer.style.display = 'block';
        }

        /**
         * Attach click handlers to game cards
         */
        function attachGameCardHandlers() {
            const gameCards = document.querySelectorAll('#recommendation-results .game-card');
            debug('Found game cards to attach handlers to:', gameCards.length);

            gameCards.forEach(card => {
                card.addEventListener('click', () => handleGameCardClick(card));
            });
        }

        /**
         * Handle game card click events
         * @param {HTMLElement} card - The clicked game card
         */
        function handleGameCardClick(card) {
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
            };
        }
    });
})();