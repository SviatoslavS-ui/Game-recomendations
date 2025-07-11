/**
 * Game Card Component JavaScript
 * Handles interactions with game cards
 */
document.addEventListener('DOMContentLoaded', function() {
    // Add click event listeners to all game card detail buttons
    document.querySelectorAll('.view-details-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            const gameId = this.getAttribute('data-game-id');
            if (gameId) {
                openGameDetailsModal(gameId);
            }
        });
    });
});