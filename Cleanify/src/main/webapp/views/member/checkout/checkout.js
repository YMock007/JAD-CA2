// Function to switch tabs between payment methods
function switchTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.tab-button').forEach(button => button.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    event.target.classList.add('active');
}

// Initialize Stripe
var stripe = Stripe("pk_test_51QopKmLLgHtQcF9Hr8GpFovhnRUYyorbvf4hQRWxRuUSVbpSJf7d5vqVrQBkDvzYAvIrzUlxquIVjcsg8z7ANUXU008bKLQhAa");
var elements = stripe.elements();

// Create and mount the card element with validation
var card = elements.create('card', {
    style: {
        base: {
            fontSize: '16px',
            color: '#32325d',
            '::placeholder': {
                color: '#aab7c4'
            }
        },
        invalid: {
            color: '#fa755a',
            iconColor: '#fa755a'
        }
    }
});
card.mount('#card-element');

// Handle form submission for Stripe payment
document.getElementById('submit-payment').addEventListener('click', function (event) {
    event.preventDefault();
    var contextPath = document.getElementById('contextPath').value;

    // Retrieve form values
    var memberId = document.getElementById('memberId').value;
    var phoneNumber = document.getElementById('phoneNumber').value.trim();
    var address = document.getElementById('address').value.trim();
    var postalCode = document.getElementById('postalCode').value.trim();
    var specialRequest = document.getElementById('specialRequest').value.trim();
    var appointmentDate = document.getElementById('appointmentDate').value;
    var appointmentTime = document.getElementById('appointmentTime').value.trim();
    var amount = document.getElementById('totalPrice').value.trim();

    // Convert amount to cents
    amount = Math.round(parseFloat(amount) * 100);

    // Validate card fields before proceeding
    stripe.createPaymentMethod({
        type: 'card',
        card: card,
        billing_details: {
            address: {
                line1: document.getElementById('billingAddress').value.trim(),
                city: document.getElementById('city').value.trim(),
            }
        }
    }).then(function (result) {
        if (result.error) {
            document.getElementById('card-errors').textContent = result.error.message;
        } else {
            var paymentMethodId = result.paymentMethod.id.trim();

            var xhr = new XMLHttpRequest();
            xhr.open("POST", contextPath + "/StripePaymentServlet", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    try {
                        var response = JSON.parse(xhr.responseText.trim());
                        if (response.success) {
                            alert('Payment Successful! Redirecting...');
                            window.location.href = response.redirect_url;
                        } else {
                            document.getElementById('card-errors').textContent = response.error;
                        }
                    } catch (e) {
                        document.getElementById('card-errors').textContent = "Unexpected server response. Please try again.";
                    }
                }
            };

            // Send data in one request
            var postData = `paymentMethodId=${encodeURIComponent(paymentMethodId)}
                &amount=${encodeURIComponent(amount)}
                &memberId=${encodeURIComponent(memberId)}
                &phoneNumber=${encodeURIComponent(phoneNumber)}
                &address=${encodeURIComponent(address)}
                &postalCode=${encodeURIComponent(postalCode)}
                &specialRequest=${encodeURIComponent(specialRequest)}
                &appointmentDate=${encodeURIComponent(appointmentDate)}
                &appointmentTime=${encodeURIComponent(appointmentTime)}`;

            xhr.send(postData);
        }
    });
});

// Date validation: Set min appointment date to tomorrow
const today = new Date();
today.setDate(today.getDate() + 1);
const formattedDate = today.toISOString().split('T')[0];
document.getElementById('appointmentDate').setAttribute('min', formattedDate);

// Function to check if form is complete
function checkFormCompletion() {
    let requiredFields = ['phoneNumber', 'address', 'postalCode', 'appointmentDate', 'appointmentTime', 'billingAddress', 'city'];
    let allFilled = requiredFields.every(id => document.getElementById(id).value.trim() !== "");

    // Enable or disable card payment button
    document.getElementById("submit-payment").disabled = !allFilled;

    // Enable or disable QR payment button
    let userEmail = document.getElementById("userEmail").value.trim();
    let qrButton = document.getElementById("sendQrButton");
    qrButton.disabled = !(userEmail !== "" && allFilled);
}

// Auto-fill email if checkbox is selected
function fillEmail() {
    let emailInput = document.getElementById("userEmail");
    let useMyEmail = document.getElementById("useMyEmail");

    if (useMyEmail.checked) {
        emailInput.value = "<%= person.getEmail() %>";
        emailInput.readOnly = true; 
    } else {
        emailInput.value = "";
        emailInput.readOnly = false; 
    }
    checkFormCompletion(); 
}

// Add event listeners to all input fields to check form completion
document.addEventListener("DOMContentLoaded", function () {
    let fields = document.querySelectorAll("input, select, textarea");
    fields.forEach(field => {
        field.addEventListener("input", checkFormCompletion);
    });
});

// Function to sanitize inputs
function sanitizeInput(input) {
    return input.replace(/<script.*?>.*?<\/script>/gi, '')
                .replace(/<.*?javascript:.*?>/gi, '')
                .replace(/[^a-zA-Z0-9\s]/g, '');
}

// Sanitize inputs before form submission
document.getElementById('appointmentForm').onsubmit = function () {
    let phoneNumber = document.getElementById('phoneNumber').value.trim();
    let postalCode = document.getElementById('postalCode').value.trim();
    let address = document.getElementById('address').value.trim();
    let appointmentTime = document.getElementById('appointmentTime').value.trim();

    phoneNumber = sanitizeInput(phoneNumber);
    postalCode = sanitizeInput(postalCode);
    address = sanitizeInput(address);
    appointmentTime = sanitizeInput(appointmentTime);

    document.getElementById('phoneNumber').value = phoneNumber;
    document.getElementById('postalCode').value = postalCode;
    document.getElementById('address').value = address;
    document.getElementById('appointmentTime').value = appointmentTime;
};
