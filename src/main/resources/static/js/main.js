document.addEventListener('DOMContentLoaded', function() {
    initDatePickers();
    initSearchForm();
    initPaymentMethods();
    initRatingSystem();

    setupEventListeners();
});

function initDatePickers() {
    const checkInInput = document.getElementById('check-in');
    const checkOutInput = document.getElementById('check-out');
    
    if (checkInInput && checkOutInput) {
        const today = new Date().toISOString().split('T')[0];
        checkInInput.min = today;
        checkInInput.addEventListener('change', function() {
            checkOutInput.min = checkInInput.value;
            if (checkOutInput.value && checkOutInput.value < checkInInput.value) {
                const nextDay = new Date(checkInInput.value);
                nextDay.setDate(nextDay.getDate() + 1);
                checkOutInput.value = nextDay.toISOString().split('T')[0];
            }
        });
    }
}

function initSearchForm() {
    const searchForm = document.querySelector('.search-form form');
    
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            const destination = document.getElementById('destination');
            const checkIn = document.getElementById('check-in');
            const checkOut = document.getElementById('check-out');
            
            if (destination && !destination.value.trim()) {
                e.preventDefault();
                alert('Por favor, informe o destino.');
                destination.focus();
                return false;
            }
            
            if (checkIn && !checkIn.value) {
                e.preventDefault();
                alert('Por favor, selecione a data de check-in.');
                checkIn.focus();
                return false;
            }
            
            if (checkOut && !checkOut.value) {
                e.preventDefault();
                alert('Por favor, selecione a data de check-out.');
                checkOut.focus();
                return false;
            }
        });
    }
}

// Inicializa os métodos de pagamento
function initPaymentMethods() {
    const paymentMethods = document.querySelectorAll('.payment-method');
    
    if (paymentMethods.length > 0) {
        paymentMethods.forEach(method => {
            method.addEventListener('click', function() {

                paymentMethods.forEach(m => m.classList.remove('active'));

                this.classList.add('active');

                const methodType = this.querySelector('span').textContent.toLowerCase();

                document.querySelectorAll('.payment-specific-form').forEach(form => {
                    form.style.display = 'none';
                });

                const specificForm = document.querySelector(`.${methodType}-form`);
                if (specificForm) {
                    specificForm.style.display = 'block';
                }
            });
        });
    }
}


function initRatingSystem() {
    const ratingSelect = document.querySelector('.rating-select');
    
    if (ratingSelect) {
        const stars = ratingSelect.querySelectorAll('i');
        
        stars.forEach((star, index) => {
            star.addEventListener('click', function() {
                stars.forEach(s => s.classList.remove('active'));
                for (let i = 0; i <= index; i++) {
                    stars[i].classList.add('active');
                }
                
                const ratingInput = document.getElementById('rating-value');
                if (ratingInput) {
                    ratingInput.value = index + 1;
                }
            });
            
            star.addEventListener('mouseover', function() {
                for (let i = 0; i <= index; i++) {
                    stars[i].classList.add('hover');
                }
            });
            
            star.addEventListener('mouseout', function() {
                stars.forEach(s => s.classList.remove('hover'));
            });
        });
    }
}

function setupEventListeners() {
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const navLinks = document.querySelector('.nav-links');
    
    if (mobileMenuToggle && navLinks) {
        mobileMenuToggle.addEventListener('click', function() {
            navLinks.classList.toggle('active');
            this.classList.toggle('active');
        });
    }

    const shareButton = document.querySelector('.share-save .btn:first-child');
    const saveButton = document.querySelector('.share-save .btn:last-child');
    
    if (shareButton) {
        shareButton.addEventListener('click', function() {
            if (navigator.share) {
                navigator.share({
                    title: document.title,
                    url: window.location.href
                }).catch(console.error);
            } else {
                alert('Compartilhe este link: ' + window.location.href);
            }
        });
    }
    
    if (saveButton) {
        saveButton.addEventListener('click', function() {
            const icon = this.querySelector('i');
            if (icon.classList.contains('far')) {
                icon.classList.remove('far');
                icon.classList.add('fas');
                alert('Hotel adicionado aos favoritos!');
            } else {
                icon.classList.remove('fas');
                icon.classList.add('far');
                alert('Hotel removido dos favoritos!');
            }
        });
    }
    
    // Cálculo dinâmico do preço total na reserva
    const guestsSelect = document.getElementById('guests');
    const roomTypeSelect = document.getElementById('room-type');
    
    if (guestsSelect && roomTypeSelect) {
        const updateTotalPrice = function() {
            const basePrice = 450; // Preço base por noite
            const checkIn = new Date(document.getElementById('check-in').value);
            const checkOut = new Date(document.getElementById('check-out').value);
            
            if (checkIn && checkOut) {
                // Calcula o número de noites
                const nights = Math.round((checkOut - checkIn) / (1000 * 60 * 60 * 24));
                
                if (nights > 0) {
                    // Calcula o preço base
                    let totalPrice = basePrice * nights;
                    
                    // Adiciona taxa por hóspede adicional
                    const guests = parseInt(guestsSelect.value);
                    if (guests > 2) {
                        totalPrice += (guests - 2) * 50 * nights; // 50 por hóspede adicional por noite
                    }
                    
                    // Adiciona taxa por tipo de quarto
                    const roomType = roomTypeSelect.value;
                    if (roomType === 'superior') {
                        totalPrice *= 1.2; // 20% a mais para quarto superior
                    } else if (roomType === 'deluxe') {
                        totalPrice *= 1.5; // 50% a mais para quarto deluxe
                    }
                    
                    // Adiciona impostos
                    const taxes = totalPrice * 0.1; // 10% de impostos
                    
                    // Atualiza os elementos na página
                    document.querySelector('.summary-item:first-child span:last-child').textContent = 
                        `R$ ${basePrice} x ${nights} noites`;
                    
                    document.querySelector('.summary-item:nth-child(2) span:last-child').textContent = 
                        `R$ ${taxes.toFixed(2)}`;
                    
                    document.querySelector('.summary-total span:last-child').textContent = 
                        `R$ ${(totalPrice + taxes).toFixed(2)}`;
                }
            }
        };
        
        // Atualiza quando os valores mudam
        guestsSelect.addEventListener('change', updateTotalPrice);
        roomTypeSelect.addEventListener('change', updateTotalPrice);
        
        const checkInInput = document.getElementById('check-in');
        const checkOutInput = document.getElementById('check-out');
        
        if (checkInInput && checkOutInput) {
            checkInInput.addEventListener('change', updateTotalPrice);
            checkOutInput.addEventListener('change', updateTotalPrice);
        }
    }
}

function fetchAddressByCEP(cep) {
    const zipInput = document.getElementById('zip-code');
    
    if (zipInput && zipInput.value.length === 8) {
        fetch(`https://viacep.com.br/ws/${zipInput.value}/json/`)
            .then(response => response.json())
            .then(data => {
                if (!data.erro) {
                    document.getElementById('street').value = data.logradouro;
                    document.getElementById('neighborhood').value = data.bairro;
                    document.getElementById('city').value = data.localidade;
                    document.getElementById('state').value = data.uf;

                    document.getElementById('number').focus();
                }
            })
            .catch(error => console.error('Erro ao buscar CEP:', error));
    }
}

function initMap() {
    console.log('Mapa inicializado');
}
