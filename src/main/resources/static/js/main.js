(function () {
    var toggle = document.getElementById('navToggle');
    var links = document.getElementById('navLinks');
    if (toggle && links) {
        toggle.addEventListener('click', function () {
            links.classList.toggle('is-open');
            toggle.classList.toggle('is-active');
        });
    }

    initCheckoutPaymentFields();

    var hero = document.querySelector('.hero-bg-slides');
    if (hero) {
        var slides = hero.querySelectorAll('.hero-slide');
        if (slides.length > 1) {
            var index = 0;
            slides[0].classList.add('is-active');
            setInterval(function () {
                slides[index].classList.remove('is-active');
                index = (index + 1) % slides.length;
                slides[index].classList.add('is-active');
            }, 5000);
        } else if (slides.length === 1) {
            slides[0].classList.add('is-active');
        }
    }
})();

function initCheckoutPaymentFields() {
    var form = document.getElementById('checkoutForm');
    if (!form) {
        return;
    }

    var cardPanel = document.getElementById('cardPaymentDetails');
    var onlinePanel = document.getElementById('onlinePaymentDetails');
    var cardInput = document.getElementById('cardAccountNumber');
    var providerSelect = document.getElementById('onlineProvider');
    var walletInput = document.getElementById('walletAccountNumber');
    var radios = form.querySelectorAll('input[name="paymentMethod"]');

    function setRequired(el, required) {
        if (!el) {
            return;
        }
        if (required) {
            el.setAttribute('required', 'required');
        } else {
            el.removeAttribute('required');
        }
    }

    function updatePanels() {
        var selected = form.querySelector('input[name="paymentMethod"]:checked');
        var method = selected ? selected.value : 'CASH';

        if (cardPanel) {
            cardPanel.classList.toggle('is-hidden', method !== 'CARD');
        }
        if (onlinePanel) {
            onlinePanel.classList.toggle('is-hidden', method !== 'ONLINE');
        }

        setRequired(cardInput, method === 'CARD');
        setRequired(providerSelect, method === 'ONLINE');
        setRequired(walletInput, method === 'ONLINE');

        if (method !== 'CARD' && cardInput) {
            cardInput.value = '';
        }
        if (method !== 'ONLINE') {
            if (providerSelect) {
                providerSelect.value = '';
            }
            if (walletInput) {
                walletInput.value = '';
            }
        }
    }

    radios.forEach(function (radio) {
        radio.addEventListener('change', updatePanels);
    });

    form.addEventListener('submit', function (e) {
        var selected = form.querySelector('input[name="paymentMethod"]:checked');
        var method = selected ? selected.value : 'CASH';

        if (method === 'CARD' && cardInput && !cardInput.value.trim()) {
            e.preventDefault();
            cardInput.focus();
            return;
        }
        if (method === 'ONLINE') {
            if (providerSelect && !providerSelect.value) {
                e.preventDefault();
                providerSelect.focus();
                return;
            }
            if (walletInput && !walletInput.value.trim()) {
                e.preventDefault();
                walletInput.focus();
                return;
            }
        }
    });

    updatePanels();
}
