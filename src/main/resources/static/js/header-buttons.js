// Header Buttons Handler
// Handles button interactions and navigation

document.addEventListener('DOMContentLoaded', () => {
    const infoBtn = document.getElementById('aboutBtn');
    const searchBtn = document.getElementById('searchBtn');

    infoBtn.addEventListener('click', function() {
        openAboutModal();
    });
    
    searchBtn.addEventListener('click', function() {
        openSearchModal();
    });
    
});

        