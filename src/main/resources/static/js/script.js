// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Search form validation
    const searchForm = document.querySelector('form[action*="/hoteis/busca"]');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            const checkIn = document.getElementById('check_in');
            const checkOut = document.getElementById('check_out');
            
            if (checkIn && checkOut) {
                const checkInDate = new Date(checkIn.value);
                const checkOutDate = new Date(checkOut.value);
                const today = new Date();
                today.setHours(0, 0, 0, 0);

                if (checkInDate < today) {
                    e.preventDefault();
                    alert('A data de check-in não pode ser anterior a hoje.');
                    return;
                }

                if (checkOutDate <= checkInDate) {
                    e.preventDefault();
                    alert('A data de check-out deve ser posterior à data de check-in.');
                    return;
                }
            }
        });
    }

    // Set minimum dates for check-in and check-out
    const checkInInput = document.getElementById('check_in');
    const checkOutInput = document.getElementById('check_out');
    
    if (checkInInput && checkOutInput) {
        const today = new Date().toISOString().split('T')[0];
        checkInInput.min = today;
        
        checkInInput.addEventListener('change', function() {
            const nextDay = new Date(this.value);
            nextDay.setDate(nextDay.getDate() + 1);
            checkOutInput.min = nextDay.toISOString().split('T')[0];
            
            if (checkOutInput.value && new Date(checkOutInput.value) <= new Date(this.value)) {
                checkOutInput.value = nextDay.toISOString().split('T')[0];
            }
        });
    }

    // Filter form handling
    const filterForm = document.querySelector('.filter-form');
    if (filterForm) {
        filterForm.addEventListener('change', function() {
            this.submit();
        });
    }

    // Clear filters button
    const clearFiltersBtn = document.querySelector('.clear-filters');
    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener('click', function() {
            const filterInputs = document.querySelectorAll('.filter-form input, .filter-form select');
            filterInputs.forEach(input => {
                if (input.type === 'checkbox' || input.type === 'radio') {
                    input.checked = false;
                } else {
                    input.value = '';
                }
            });
            filterForm.submit();
        });
    }

    // Handle loading state
    const showLoading = () => {
        const overlay = document.createElement('div');
        overlay.className = 'spinner-overlay';
        overlay.innerHTML = `
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Carregando...</span>
            </div>
        `;
        document.body.appendChild(overlay);
    };

    const hideLoading = () => {
        const overlay = document.querySelector('.spinner-overlay');
        if (overlay) {
            overlay.remove();
        }
    };

    // Add loading state to forms
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', () => {
            showLoading();
        });
    });

    // Handle back button and show loading
    window.addEventListener('popstate', () => {
        showLoading();
    });

    // Hide loading when page is fully loaded
    window.addEventListener('load', () => {
        hideLoading();
    });

    // Toast notifications
    const showToast = (message, type = 'success') => {
        const toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            const container = document.createElement('div');
            container.className = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');

        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;

        document.querySelector('.toast-container').appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();

        toast.addEventListener('hidden.bs.toast', () => {
            toast.remove();
        });
    };

    // Handle success and error messages from server
    const successMessage = document.querySelector('.alert-success');
    const errorMessage = document.querySelector('.alert-danger');

    if (successMessage) {
        showToast(successMessage.textContent, 'success');
        successMessage.remove();
    }

    if (errorMessage) {
        showToast(errorMessage.textContent, 'danger');
        errorMessage.remove();
    }

    // Scroll to top button
    const scrollTopBtn = document.createElement('button');
    scrollTopBtn.className = 'btn btn-primary position-fixed bottom-0 end-0 m-3 rounded-circle';
    scrollTopBtn.style.display = 'none';
    scrollTopBtn.innerHTML = '<i class="fas fa-arrow-up"></i>';
    scrollTopBtn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    document.body.appendChild(scrollTopBtn);

    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            scrollTopBtn.style.display = 'block';
        } else {
            scrollTopBtn.style.display = 'none';
        }
    });

    // Handle image loading errors
    document.querySelectorAll('img').forEach(img => {
        img.addEventListener('error', function() {
            this.src = '/images/placeholder.jpg';
        });
    });

    // Lazy loading for images
    if ('loading' in HTMLImageElement.prototype) {
        document.querySelectorAll('img[loading="lazy"]').forEach(img => {
            img.src = img.dataset.src;
        });
    } else {
        // Fallback for browsers that don't support lazy loading
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js';
        document.body.appendChild(script);
    }
}); 