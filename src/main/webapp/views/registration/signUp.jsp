<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="signUp.css">
    <script src="https://accounts.google.com/gsi/client" async defer></script>
    
    <title>Sign up</title>
 
</head>
<body>
<% 
   String successMessage = (String) session.getAttribute("successMessage");
   String errorMessage = (String) session.getAttribute("errorMessage");
%>

<% if (successMessage != null) { %>
<script>alert("<%=successMessage %>");</script>
<% session.removeAttribute("successMessage"); %>
<% } %>

<% if (errorMessage != null) { %>
<script>alert("<%=errorMessage %>");</script>
<% session.removeAttribute("errorMessage"); %>
<% } %>


<div class="container" id="container">
    <div class="form-container sign-up-container">
        <form id="mainSignUpForm" onsubmit="">
            <h1 id="createAccount">Create Account</h1>
           
            <div class="social-container">
                <div id="g_id_onload"
                     data-client_id="148492486604-8a1dvme4m8u4b0pjljgto9ebplism9uu.apps.googleusercontent.com"
                     data-callback="forSignUp"
                     data-auto_prompt="false">
                </div>
                <div class="g_id_signin" data-type="standard"></div>
            </div>
            <div class="input-container">
                <i class="fas fa-envelope"></i>
                <input type="email" id="email" placeholder="Email" required />
            </div>
            <div class="input-container">
                <i class="fas fa-lock"></i>
                <input type="password" id="password" placeholder="Password" required oninput="validatePasswordInline(this.value)"/>
                <i class="fas fa-eye toggle-password" onclick="togglePassword('password')"></i>
            </div>
            <span id="passwordError" class="password-check-error-message"></span>
            <div class="input-container">
                <i class="fas fa-lock"></i>
                <input type="password" id="confirmPassword" placeholder="Re-enter Password" required />
                <i class="fas fa-eye toggle-password" onclick="togglePassword('confirmPassword')"></i>
            </div>
            <span id="confirmPasswordError" class="password-check-error-message"></span>
            <button type="submit">Sign Up</button>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-left">
                <h1>Your Clean Journey Awaits!</h1>
                <p>We’ve missed you! Sign in to continue exploring the cleanest deals and services.s</p>
                <button class="ghost" id="signInRedirect">Sign In</button>
            </div>
        </div>
    </div>
</div>

<!-- Popup Form -->
<div id="popupForm" class="popup">
    <div class="popup-content">
        <span id="closePopup" class="close-btn">&times;</span>
        <h2>Additional Information</h2>
        <form id="popupFormFields" action="<%= request.getContextPath() %>/SignupServlet" method="POST" onsubmit="return validatePopupForm()">
            <!-- Hidden fields for email and password -->
            <input type="hidden" id="popupEmail" name="email">
            <input type="hidden" id="popupPassword" name="password">

            <!-- Full Name Field -->
            <div class="input-container">
                <i class="fas fa-user"></i>
                <input type="text" name="name" id="name" placeholder="Full Name" required />
            </div>

            <!-- Phone Number Field -->
            <div class="input-container">
			    <i class="fas fa-phone phone-icon"></i>
			    <input type="text" name="phnumber" id="phnumber" placeholder="Phone Number" />
			</div>


            <!-- Address Field -->
            <div class="input-container">
                <i class="fas fa-map-marker-alt"></i>
                <input type="text" name="address" id="address" placeholder="Address" required />
            </div>

            <!-- Postal Code Field -->
            <div class="input-container">
                <i class="fas fa-map-pin"></i>
                <input type="text" name="postalcode" id="postalcode" placeholder="Postal Code" required />
            </div>

            <button type="submit">Submit</button>
        </form>
    </div>
</div>

<div id="customPopup" class="popup-message">
    <h5 id="popupMessage"></h5>
    <button onclick="hidePopup()">OK</button>
</div>
<script>
document.getElementById("mainSignUpForm").addEventListener("submit", function (e) {
    e.preventDefault();

    // Basic form validation
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const confirmPasswordError = document.getElementById("confirmPasswordError");
    

    if (!email || !password || !confirmPassword ) {
        showPopup("Please fill in all fields.");
        return false;
    }

    if (password !== confirmPassword) {
        confirmPasswordError.textContent = "Passwords do not match!";
        return false;
    } else {
        confirmPasswordError.textContent = ""; // Clear the error message
    }

    // Set data for popup form
    document.getElementById("popupEmail").value = email;
    document.getElementById("popupPassword").value = password;

    // Show the popup form
    document.getElementById("popupForm").style.display = "flex";
});

function validatePopupForm() {
    // Get form inputs
    const name = document.getElementById("name").value.trim();
    const phnumber = document.getElementById("phnumber").value.trim();
    const address = document.getElementById("address").value.trim();
    const postalcode = document.getElementById("postalcode").value.trim();

    // Regex Patterns
    const phoneRegex = /^[0-9]{8,15}$/; // 8-15 digits
    const postalCodeRegex = /^[0-9]{4,6}$/; // 4-6 digits

    // Validate Full Name
    if (name.length < 2 || name.length > 50) {
    	showPopup("Full Name must be between 2 and 50 characters.");
        return false;
    }

    // Validate Phone Number
    if (phnumber && !phoneRegex.test(phnumber)) {
    	showPopup("Phone Number must be numeric and between 8 to 15 digits.");
        return false;
    }

    // Validate Address
    if (address.length < 5) {
    	showPopup("Address must be at least 5 characters long.");
        return false;
    }

    // Validate Postal Code
    if (!postalCodeRegex.test(postalcode)) {
    	showPopup("Postal Code must be numeric and between 4 to 6 digits.");
        return false;
    }

    return true; // If all validations pass
}

function validatePasswordInline(password) {
    const passwordError = document.getElementById("passwordError");

    const minLength = 8;
    const maxLength = 20;
    const uppercaseRegex = /[A-Z]/; // At least one uppercase letter
    const lowercaseRegex = /[a-z]/; // At least one lowercase letter
    const digitRegex = /[0-9]/;     // At least one digit
    const specialCharRegex = /[@#$%^&*(),.?":{}|<>]/; // At least one special character

    // Check conditions and set error messages
    if (password.length < minLength || password.length > maxLength) {
        passwordError.textContent = "Password must be between "+minLength+" and "+maxLength+" characters long.";
    } else if (!uppercaseRegex.test(password)) {
        passwordError.textContent = "Password must include at least one uppercase letter.";
    } else if (!lowercaseRegex.test(password)) {
        passwordError.textContent = "Password must include at least one lowercase letter.";
    } else if (!digitRegex.test(password)) {
        passwordError.textContent = "Password must include at least one number.";
    } else if (!specialCharRegex.test(password)) {
        passwordError.textContent = "Password must include at least one special character (e.g., @, #, $, %).";
    } else {
        passwordError.textContent = ""; // Clear the error message
    }
}

// Close Popup on Clicking the "X" Button
document.getElementById("closePopup").onclick = function () {
    document.getElementById("popupForm").style.display = "none";
};
</script>

<script src="index.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jwt-decode/build/jwt-decode.min.js"></script>
</body>
</html>