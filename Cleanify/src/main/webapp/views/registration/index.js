// Redirects for switching between Sign-Up and Sign-In
document.addEventListener("DOMContentLoaded", () => {
    const signUpRedirect = document.getElementById("signUpRedirect");
    const signInRedirect = document.getElementById("signInRedirect");

    if (signUpRedirect) {
        signUpRedirect.addEventListener("click", () => {
            document.body.classList.add("page-transition-out");
            setTimeout(() => {
                window.location.href = "signUp.jsp";
            }, 600); // Match the CSS animation duration
        });
    }

    if (signInRedirect) {
        signInRedirect.addEventListener("click", () => {
            document.body.classList.add("page-transition-out");
            setTimeout(() => {
                window.location.href = "signIn.jsp";
            }, 600); // Match the CSS animation duration
        });
    }
});




// Add password visibility toggle
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = input.nextElementSibling;
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("fa-eye");
        icon.classList.add("fa-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("fa-eye-slash");
        icon.classList.add("fa-eye");
    }
}



function showPopup(message) {
    const popup = document.getElementById("customPopup");
    const popupMessage = document.getElementById("popupMessage");
    popupMessage.textContent = message;
    popup.style.display = "block";
}

// Hide the popup message
function hidePopup() {
    const popup = document.getElementById("customPopup");
    popup.style.display = "none";
}

// Inline password validation
function validatePasswordInline(password) {
    const passwordError = document.getElementById("passwordError");

    const minLength = 8;
    const maxLength = 20;
    const uppercaseRegex = /[A-Z]/;
    const lowercaseRegex = /[a-z]/;
    const digitRegex = /[0-9]/;
    const specialCharRegex = /[@#$%^&*(),.?":{}|<>]/;

    if (password.length < minLength || password.length > maxLength) {
        passwordError.textContent = `Password must be between ${minLength} and ${maxLength} characters long.`;
    } else if (!uppercaseRegex.test(password)) {
        passwordError.textContent = "Password must include at least one uppercase letter.";
    } else if (!lowercaseRegex.test(password)) {
        passwordError.textContent = "Password must include at least one lowercase letter.";
    } else if (!digitRegex.test(password)) {
        passwordError.textContent = "Password must include at least one number.";
    } else if (!specialCharRegex.test(password)) {
        passwordError.textContent = "Password must include at least one special character (e.g., @, #, $, %).";
    } else {
        passwordError.textContent = ""; // Clear error
    }
}

// Form submission handler for Sign-Up
function handleSignUpFormSubmission(e) {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const confirmPasswordError = document.getElementById("confirmPasswordError");

    if (!email || !password || !confirmPassword) {
        showPopup("Please fill in all fields.");
        return false;
    }

    if (password !== confirmPassword) {
        confirmPasswordError.textContent = "Passwords do not match!";
        return false;
    } else {
        confirmPasswordError.textContent = ""; // Clear error
    }

    // Set data for popup form
    document.getElementById("popupEmail").value = email;
    document.getElementById("popupPassword").value = password;

    // Show the popup form
    document.getElementById("popupForm").style.display = "flex";
}

// Validate the popup form
function validatePopupForm() {
    const name = document.getElementById("name").value.trim();
    const phnumber = document.getElementById("phnumber").value.trim();
    const address = document.getElementById("address").value.trim();
    const postalcode = document.getElementById("postalcode").value.trim();

    const phoneRegex = /^[0-9]{8,15}$/;
    const postalCodeRegex = /^[0-9]{4,6}$/;

    if (name.length < 2 || name.length > 50) {
        showPopup("Full Name must be between 2 and 50 characters.");
        return false;
    }

    if (phnumber && !phoneRegex.test(phnumber)) {
        showPopup("Phone Number must be numeric and between 8 to 15 digits.");
        return false;
    }

    if (address.length < 5) {
        showPopup("Address must be at least 5 characters long.");
        return false;
    }

    if (!postalCodeRegex.test(postalcode)) {
        showPopup("Postal Code must be numeric and between 4 to 6 digits.");
        return false;
    }

    return true;
}

// Close the popup form
function closePopup() {
    document.getElementById("popupForm").style.display = "none";
}

// Google Sign-In and Sign-Up callbacks
function forSignUp(response) {
	    try {

	    	console.log("Google Sign-Up triggered");
		    
	        // Decode the Google ID token
	        const userData = jwt_decode(response.credential);

	        // Extract user's email and name
	        const email = userData.email;
	        const name = userData.name;

	        // Show the popup form for additional information
	        document.getElementById("popupForm").style.display = "flex";

	        // Pre-fill the popup form with Google user details
	        document.getElementById("popupEmail").value = email;
	        document.getElementById("name").value = name;

	        // Use a placeholder password to mark this as a Google user
	        document.getElementById("popupPassword").value = "Google_OAuth";

	    } catch (error) {
	        console.error("Error during Google Sign-Up:", error);
	        alert("There was an error processing your Google Sign-Up. Please try again.");
	    }
	}



	function forSignIn(response) {
	    try {

	    	console.log("Google Sign-In triggered");
		    
	        // Decode the Google ID token
	        const userData = jwt_decode(response.credential);

	        // Extract user's email
	        const email = userData.email;

	        // Send data to the SigninServlet via a form submission
	        const form = document.createElement('form');
	        form.method = 'POST';
	        form.action = '<%= request.getContextPath() %>/GoogleSigninServlet';

	        // Create hidden input fields for email and a placeholder password
	        const emailField = document.createElement('input');
	        emailField.type = 'hidden';
	        emailField.name = 'signinEmail';
	        emailField.value = email;

	        const passwordField = document.createElement('input');
	        passwordField.type = 'hidden';
	        passwordField.name = 'signinPassword';
	        passwordField.value = 'Google_OAuth'; // Placeholder password for Google users

	        form.appendChild(emailField);
	        form.appendChild(passwordField);

	        // Append form to body and submit
	        document.body.appendChild(form);
	        form.submit();
	    } catch (error) {
	        console.error("Error during Google Sign-In:", error);
	        alert("An error occurred during Google Sign-In. Please try again.");
	    }
	}

	
	
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