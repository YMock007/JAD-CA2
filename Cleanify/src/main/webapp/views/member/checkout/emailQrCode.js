// Handle form submission for sending email
document.getElementById('sendQrButton').addEventListener('click', function (event) {
    event.preventDefault();
    console.log("Attempting to send email...");

    // Get the context path
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
    var email = document.getElementById('userEmail').value.trim();

    // Convert amount to cents
    amount = Math.round(parseFloat(amount) * 100);

    // Construct URL-encoded body string
    var body = new URLSearchParams({
        amount: amount,
        memberId: memberId,
        phoneNumber: phoneNumber,
        address: address,
        postalCode: postalCode,
        specialRequest: specialRequest,
        appointmentDate: appointmentDate,
        appointmentTime: appointmentTime,
        email: email
    }).toString();

    // Send form data to the servlet
    fetch(contextPath + "/SendEmailServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: body
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Network response was not ok: " + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert("QR Payment Email Sent Successfully!");
        } else {
            alert("Failed to send email: " + data.message);
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("An error occurred while sending the email.");
    });
});
