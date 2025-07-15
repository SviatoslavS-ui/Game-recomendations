/**
 * Game Card Component JavaScript
 * Handles interactions with game cards
 */
document.addEventListener('DOMContentLoaded', () => {
    // Add click event listeners to all game cards
    document.querySelectorAll('.game-card').forEach(card => {
        card.addEventListener('click', function() {
            // Note: Using traditional function here to preserve 'this' context
            const gameId = this.getAttribute('data-game-id');
            if (gameId) {
                openGameDetailsModal(gameId);
            }
        });
    });
});