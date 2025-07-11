/**
 * Game Details Modal JavaScript
**/

const DEBUG = true;

const gameDetailsCache = {};

function debug(message, data) {
    if (DEBUG) {
        console.log(`[Modal Debug] ${message}`, data || '');
    }
}

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

    debug('Event listeners set up');
}

function closeGameDetailsModal() {
    document.getElementById('modalOverlay').classList.add('hidden');
    document.getElementById('gameDetailsModal').classList.add('hidden');

    document.body.style.overflow = '';
    debug('Game details modal closed');
}

function openGameDetailsModal(gameId) {
    debug('Opening modal for game:', gameId);

    document.getElementById('modalOverlay').classList.remove('hidden');
    document.getElementById('gameDetailsModal').classList.remove('hidden');

    const content = document.getElementById('gameDetailsContent');
    if (content) content.classList.add('hidden');
    document.body.style.overflow = 'hidden';

    loadGameDetails(gameId);
}

function loadGameDetails(gameId) {
    // check cache first
    if (gameDetailsCache[gameId]) {
        debug('Using cached game details for:', gameId);
        displayGameDetails(gameDetailsCache[gameId]);
        return;
    }

    // Fetch from API
    debug('Fetching game details from API for:', gameId);
    fetch(`/api/games/${gameId}/details`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            // Cache the data
            gameDetailsCache[gameId] = data;
            // Display the data
            displayGameDetails(data);
        })
        .catch(error => {
            console.error('Error fetching game details:', error);
            // Show error in modal
            document.getElementById('gameDetailsContent').innerHTML =
                `<div class="error-message">Error loading game details: ${error.message}</div>`;
            document.getElementById('gameDetailsContent').style.display = 'block';
        });
}

function displayGameDetails(gameData) {
    // Set text content for various elements
    setElementText('gameTitle', gameData.title);
    setElementText('gameDeveloper', gameData.developer);
    setElementText('gamePublisher', gameData.publisher);
    setElementText('gameReleaseDate', gameData.releaseDate);

    // Apply color classes based on score values
    const metacriticScoreEl = document.getElementById('metacriticScore');
    if (metacriticScoreEl && gameData.metacriticScore) {
        setElementText('metacriticScore', gameData.metacriticScore);
        const score = parseInt(gameData.metacriticScore);
        metacriticScoreEl.className = 'score-value ' +
            (score >= 75 ? 'score-high' : (score >= 50 ? 'score-medium' : 'score-low'));
    }

    const userScoreEl = document.getElementById('userScore');
    if (userScoreEl && gameData.userScore) {
        setElementText('userScore', gameData.userScore);
        const score = parseFloat(gameData.userScore) * 10; // Convert to same scale as metacritic
        userScoreEl.className = 'score-value ' +
            (score >= 75 ? 'score-high' : (score >= 50 ? 'score-medium' : 'score-low'));
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
        debug('Setting game image:', gameData.imageUrl);
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

    // Show the content by removing the hidden class
    const content = document.getElementById('gameDetailsContent');
    if (content) {
        content.classList.remove('hidden');
        debug('Game details content is now visible');
    }
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

// Make functions globally accessible
window.openGameDetailsModal = openGameDetailsModal;
window.closeGameDetailsModal = closeGameDetailsModal;