/**
 * Custom Modal JavaScript
 * A reusable modal component with image and text content
 */

function setupAboutModalEventListeners() {
    const closeBtn = document.getElementById('aboutModalClose');
    if (closeBtn) {
        closeBtn.addEventListener('click', closeAboutModal);
    }

    const overlay = document.getElementById('modalOverlay');
    if (overlay) {
        overlay.addEventListener('click', function(event) {
            // Only close if clicking directly on the overlay, not its children
            if (event.target === overlay) {
                closeAboutModal();
            }
        });
    }

    const aboutBtn = document.getElementById('aboutBtn');
    if (aboutBtn) {
        aboutBtn.addEventListener('click', () => {
            openAboutModal(
                'About Game Recommender',
                '/images/about.jpg',
                `
                    <p>Welcome to Game Recommender - your cyberpunk-themed guide to the gaming universe!</p>
                    <p>As an avid gamer with thousands of hours spent across various genres, I developed this application with great respect and passion for everyone who shares a love for video games. This app is designed to help you discover games based on your preferences — including your favorite genre, game features, developer studio, publisher, and release date.</p>
                    <p>I would like to note in advance that this collection contains very few multiplayer titles such as Call of Duty or World of Warcraft. It is primarily focused on single-player games with rich storytelling, immersive atmospheres, and a variety of genres.<p>

                `
            );
        });
    }

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            closeAboutModal();
        }
    });
}

function closeAboutModal() {
    document.getElementById('modalOverlay').classList.add('hidden');
    document.getElementById('aboutModal').classList.add('hidden');
    document.body.style.overflow = '';
}

function openAboutModal(title, imageUrl, text) {
    document.getElementById('modalOverlay').classList.remove('hidden');
    document.getElementById('aboutModal').classList.remove('hidden');
    document.body.style.overflow = 'hidden';
    displayAboutModalContent({ title, imageUrl, text });
}

function displayAboutModalContent(modalData) {
    const titleEl = document.getElementById('aboutModalTitle');
    if (titleEl) {
        titleEl.textContent = modalData.title;
    }

    const imageEl = document.getElementById('aboutModalImage');
    if (imageEl && modalData.imageUrl) {
        imageEl.src = modalData.imageUrl;
        imageEl.alt = modalData.title;
    }

    const textEl = document.getElementById('aboutModalText');
    if (textEl) {
        textEl.innerHTML = modalData.text;
    }
}

document.addEventListener('DOMContentLoaded', setupAboutModalEventListeners);

window.openAboutModal = openAboutModal;
window.closeAboutModal = closeAboutModal;
