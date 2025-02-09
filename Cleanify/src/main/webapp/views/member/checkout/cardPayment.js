// ✅ Initialize Stripe
var stripe = Stripe("pk_test_51QopKmLLgHtQcF9Hr8GpFovhnRUYyorbvf4hQRWxRuUSVbpSJf7d5vqVrQBkDvzYAvIrzUlxquIVjcsg8z7ANUXU008bKLQhAa");
var elements = stripe.elements();

// ✅ Create and mount the card element with validation
var card = elements.create('card', {
    hidePostalCode: true, // ✅ Ensure ZIP code is hidden
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

// ✅ Handle form submission for Stripe payment
document.getElementById('submit-payment').addEventListener('click', function (event) {
    event.preventDefault();
    var contextPath = document.getElementById('contextPath').value;

    // ✅ Retrieve form values
    var memberId = document.getElementById('memberId').value;
    var phoneNumber = document.getElementById('phoneNumber').value.trim();
    var address = document.getElementById('address').value.trim();
    var postalCode = document.getElementById('postalCode').value.trim();
    var specialRequest = document.getElementById('specialRequest').value.trim();
    var appointmentDate = document.getElementById('appointmentDate').value;
    var appointmentTime = document.getElementById('appointmentTime').value.trim();
    var amount = document.getElementById('totalPrice').value.trim();
    amount = Math.round(parseFloat(amount));

    // ✅ Construct billing address (combine address and city)
    let billingFullAddress = document.getElementById('billingAddress').value.trim() + ", " +
                             document.getElementById('billingCity').value.trim();
    let billingPostalCode = document.getElementById('billingPostalCode').value.trim();

    // ✅ Validate card fields before proceeding
    stripe.createPaymentMethod({
        type: 'card',
        card: card,
        billing_details: {
            address: {
                line1: document.getElementById('billingAddress').value.trim(),
                city: document.getElementById('billingCity').value.trim(),
                postal_code: document.getElementById('billingPostalCode').value.trim(),
            }
        }
    }).then(function (result) {
        console.log("DEBUG: PaymentMethod Response:", result); // ✅ Print Stripe response

        if (result.error) {
            document.getElementById('card-errors').textContent = result.error.message;
        } else {
            var paymentMethodId = result.paymentMethod.id;
            console.log("DEBUG: PaymentMethod ID:", paymentMethodId); // ✅ Log payment method ID

            if (!paymentMethodId) {
                document.getElementById('card-errors').textContent = "Failed to create Payment Method. Please try again.";
                return;
            }

            // ✅ Send Payment & Booking Data to `StripePaymentServlet`
            fetch(contextPath + "/StripePaymentServlet", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({
                    paymentMethodId: paymentMethodId,
                    amount: amount,
                    memberId: memberId,
                    phoneNumber: phoneNumber,
                    address: address,
                    postalCode: postalCode,
                    specialRequest: specialRequest,
                    appointmentDate: appointmentDate,
                    appointmentTime: appointmentTime,
                    billingAddress: billingFullAddress,
                    billingPostalCode: billingPostalCode,
                    bookingCart: bookingCartArray
                })
            })
            .then(response => response.json())
            .then(result => {
                console.log("DEBUG: Server Response:", result); // ✅ Print full response

                if (result.success) {
                    window.location.href = result.redirect_url; 
                } else {
                    document.getElementById('card-errors').textContent = result.error;
                }
            })
            .catch(error => {
                console.error("DEBUG: Fetch Error:", error); // ✅ Print detailed fetch errors
                document.getElementById('card-errors').textContent = "Payment process failed. Please try again.";
            });
        }
    });
});
