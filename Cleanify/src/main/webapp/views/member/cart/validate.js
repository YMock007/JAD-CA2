// Get today's date and set it to tomorrow
const today = new Date();
today.setDate(today.getDate() + 1); // Set to tomorrow (exclude today)

// Format date as YYYY-MM-DD
const formattedDate = today.toISOString().split('T')[0];

// Set the min attribute dynamically for appointment date
document.getElementById('appointmentDate').setAttribute('min', formattedDate);

// JavaScript Validation
function validateForm() {
    let phoneNumber = document.getElementById('phoneNumber').value.trim();
    let postalCode = document.getElementById('postalCode').value.trim();
    let appointmentDate = document.getElementById('appointmentDate').value;
    let cleanerId = document.getElementById('cleanerId').value;
    let serviceId = document.getElementById('serviceId').value;
    let address = document.getElementById('address').value.trim();
    let appointmentTime = document.getElementById('appointmentTime').value.trim();
    let valid = true;

    // Clear any previous error messages
    sessionStorage.removeItem('phoneNumberError');
    sessionStorage.removeItem('postalCodeError');
    sessionStorage.removeItem('appointmentDateError');
    sessionStorage.removeItem('cleanerIdError');
    sessionStorage.removeItem('serviceIdError');
    sessionStorage.removeItem('addressError');
    sessionStorage.removeItem('appointmentTimeError');

    // Validate phone number (must be numeric and length of 10)
    if (!/^\d{8}$/.test(phoneNumber)) {
        sessionStorage.setItem('phoneNumberError', 'Please enter a valid phone number with 8 digits.');
        valid = false;
    }

    // Validate postal code (must be numeric and 6 digits long)
    if (!/^\d{6}$/.test(postalCode)) {
        sessionStorage.setItem('postalCodeError', 'Please enter a valid postal code with 6 digits.');
        valid = false;
    }

    // Validate appointment date (cannot be in the past)
    let todayDate = new Date();
    todayDate.setHours(0, 0, 0, 0); // Strip time for comparison
    let selectedDate = new Date(appointmentDate);
    selectedDate.setHours(0, 0, 0, 0); // Strip time for comparison
    if (selectedDate < todayDate) {
        sessionStorage.setItem('appointmentDateError', 'Please select a future appointment date.');
        valid = false;
    }

    // Validate cleaner selection (must be selected)
    if (!cleanerId) {
        sessionStorage.setItem('cleanerIdError', 'Please select a cleaner.');
        valid = false;
    }

    // Validate service selection (must be selected)
    if (!serviceId) {
        sessionStorage.setItem('serviceIdError', 'Please select a service.');
        valid = false;
    }

    // Validate address (cannot be empty)
    if (!address) {
        sessionStorage.setItem('addressError', 'Please enter your address.');
        valid = false;
    }

    // Validate appointment time (must be selected)
    if (!appointmentTime) {
        sessionStorage.setItem('appointmentTimeError', 'Please select an appointment time.');
        valid = false;
    }

    // If invalid, reload the page with error messages
    if (!valid) {
        window.location.reload();
    }

    return valid; // Form is valid
}

// Load session errors on page load
window.onload = function () {
    let phoneError = sessionStorage.getItem('phoneNumberError');
    let postalError = sessionStorage.getItem('postalCodeError');
    let dateError = sessionStorage.getItem('appointmentDateError');
    let cleanerError = sessionStorage.getItem('cleanerIdError');
    let serviceError = sessionStorage.getItem('serviceIdError');
    let addressError = sessionStorage.getItem('addressError');
    let timeError = sessionStorage.getItem('appointmentTimeError');

    // Set custom validity messages if there are errors
    if (phoneError) {
        document.getElementById('phoneNumber').setCustomValidity(phoneError);
    }
    if (postalError) {
        document.getElementById('postalCode').setCustomValidity(postalError);
    }
    if (dateError) {
        document.getElementById('appointmentDate').setCustomValidity(dateError);
    }
    if (cleanerError) {
        document.getElementById('cleanerId').setCustomValidity(cleanerError);
    }
    if (serviceError) {
        document.getElementById('serviceId').setCustomValidity(serviceError);
    }
    if (addressError) {
        document.getElementById('address').setCustomValidity(addressError);
    }
    if (timeError) {
        document.getElementById('appointmentTime').setCustomValidity(timeError);
    }
};

// Helper function to sanitize inputs
function sanitizeInput(input) {
    return input.replace(/<script.*?>.*?<\/script>/gi, '') // Remove potential script injections
                .replace(/<.*?javascript:.*?>/gi, '') // Remove javascript links
                .replace(/[^a-zA-Z0-9\s]/g, ''); // Remove non-alphanumeric characters (optional)
}

// Add sanitization to inputs before submission
document.getElementById('appointmentForm').onsubmit = function () {
    // Retrieve and sanitize form inputs
    let phoneNumber = document.getElementById('phoneNumber').value.trim();
    let postalCode = document.getElementById('postalCode').value.trim();
    let address = document.getElementById('address').value.trim();
    let appointmentTime = document.getElementById('appointmentTime').value.trim();

    // Sanitize input values
    phoneNumber = sanitizeInput(phoneNumber);
    postalCode = sanitizeInput(postalCode);
    address = sanitizeInput(address);
    appointmentTime = sanitizeInput(appointmentTime);

    // Ensure sanitized values are set back to the fields before submission
    document.getElementById('phoneNumber').value = phoneNumber;
    document.getElementById('postalCode').value = postalCode;
    document.getElementById('address').value = address;
    document.getElementById('appointmentTime').value = appointmentTime;
};
