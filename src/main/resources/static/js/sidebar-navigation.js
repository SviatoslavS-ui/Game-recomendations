/**
 * Sidebar Navigation Handler
 * Handles navigation with active state prevention
 */

document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.nav-item');
    
    navItems.forEach(function(navItem) {
        const targetHref = navItem.getAttribute('data-href');
        const isActive = navItem.classList.contains('active');
        
        // Set appropriate cursor based on active state
        navItem.style.cursor = isActive ? 'default' : 'pointer';
        
        // Remove any attributes that might cause status display
        navItem.removeAttribute('href');
        navItem.removeAttribute('title');
        navItem.removeAttribute('alt');
        
        navItem.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // Prevent navigation if this item is already active
            if (navItem.classList.contains('active')) {
                console.log('Navigation prevented: Already on this page');
                return;
            }
            
            if (targetHref) {
                window.location.href = targetHref;
            }
        });
        
        navItem.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                e.stopPropagation();
                
                // Prevent navigation if this item is already active
                if (navItem.classList.contains('active')) {
                    console.log('Keyboard navigation prevented: Already on this page');
                    return;
                }
                
                if (targetHref) {
                    window.location.href = targetHref;
                }
            }
        });
        
        // Prevent context menu and drag operations
        navItem.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        });
        
        navItem.addEventListener('dragstart', function(e) {
            e.preventDefault();
        });
        
        // Prevent any mouse events that might trigger status
        navItem.addEventListener('mousedown', function(e) {
            e.preventDefault();
        });
        
        navItem.setAttribute('tabindex', '0');
        navItem.setAttribute('role', 'button');
    });
});
