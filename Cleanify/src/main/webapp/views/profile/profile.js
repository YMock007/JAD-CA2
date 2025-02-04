/**
 * 
 */


// Handle tab switching
        document.querySelectorAll('.sidebar ul li').forEach(tab => {
            tab.addEventListener('click', () => {
                document.querySelector('.sidebar ul li.active').classList.remove('active');
                tab.classList.add('active');

                document.querySelector('.tab-content.active').classList.remove('active');
                document.getElementById(tab.getAttribute('data-tab')).classList.add('active');
            });
        });

        


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

     // Validate Name Inline
        function validateNameInline() {
            const name = document.getElementById("name").value.trim();
            const nameError = document.getElementById("nameError");

            if (name.length < 2 || name.length > 50) {
                nameError.textContent = "Name must be between 2 and 50 characters.";
            } else {
                nameError.textContent = ""; // Clear error message
            }
        }

        // Validate Phone Inline
        function validatePhoneInline() {
            const phone = document.getElementById("phone").value.trim();
            const phoneError = document.getElementById("phoneError");
            const phoneRegex = /^[0-9]{8,15}$/;

            if (phone && !phoneRegex.test(phone)) {
                phoneError.textContent = "Phone Number must be numeric and between 8 to 15 digits.";
            } else {
                phoneError.textContent = ""; // Clear error message
            }
        }

        // Validate Address Inline
        function validateAddressInline() {
            const address = document.getElementById("address").value.trim();
            const addressError = document.getElementById("addressError");

            if (address.length < 5) {
                addressError.textContent = "Address must be at least 5 characters long.";
            } else {
                addressError.textContent = ""; // Clear error message
            }
        }

        // Validate Postal Code Inline
        function validatePostalCodeInline() {
            const postalCode = document.getElementById("postalCode").value.trim();
            const postalCodeError = document.getElementById("postalCodeError");
            const postalCodeRegex = /^[0-9]{4,6}$/;

            if (!postalCodeRegex.test(postalCode)) {
                postalCodeError.textContent = "Postal Code must be numeric and between 4 to 6 digits.";
            } else {
                postalCodeError.textContent = ""; // Clear error message
            }
        }
                


        // Attach validation to the form submission event
       document.getElementById("generalForm").addEventListener("submit", function (event) {
		    console.log("Submitting form...");
		    const userConfirmed = confirm("Are you sure you want to update your profile?");
		    if (!userConfirmed) {
		        console.log("Submission cancelled.");
		        event.preventDefault();
		    }else{

		    	 // Form submission proceeds here
		        const form = document.getElementById("generalForm");
		        form.submit(); // Explicitly submit the form to the servlet
		        console.log("Form submitted to servlet.");

		        
			    }
		});



        document.addEventListener("DOMContentLoaded", function () {
            const formFields = document.querySelectorAll("#generalForm input:not([readonly])");
            const updateButton = document.getElementById("updateProfileSection");

            // Initially disable the update button
            updateButton.disabled = true;

            // Track original field values
            const originalValues = {};
            formFields.forEach((field) => {
                originalValues[field.id] = field.value;
            });

            // Add event listener to each form field
            formFields.forEach((field) => {
                field.addEventListener("input", () => {
                    let isChanged = false;

                    // Check if any field value has changed
                    formFields.forEach((field) => {
                        if (field.value !== originalValues[field.id]) {
                            isChanged = true;
                        }
                    });

                    // Enable or disable the update button based on changes
                    updateButton.disabled = !isChanged;
                });
            });
        });



        // for password validation
        
        
                
        document.addEventListener("DOMContentLoaded", function () {
            const oldPasswordInput = document.getElementById("oldPassword");
            const newPasswordInput = document.getElementById("newPassword");
            const confirmPasswordInput = document.getElementById("confirmPassword");
            const saveChangesButton = document.getElementById("updatePasswordSection");

            const passwordError = document.getElementById("newPasswordError");
            const confirmPasswordError = document.getElementById("confirmPasswordError");
            const oldPasswordError = document.getElementById("oldPasswordError");

            const minLength = 8;
            const maxLength = 20;
            const uppercaseRegex = /[A-Z]/;
            const lowercaseRegex = /[a-z]/;
            const digitRegex = /[0-9]/;
            const specialCharRegex = /[@#$%^&*(),.?":{}|<>]/;

            // Validate New Password
            function validateNewPassword() {
                const password = newPasswordInput.value.trim();
                let isValid = true;

                if (password.length < minLength || password.length > maxLength) {
                    passwordError.textContent = "Password must be between "+minLength+" and "+maxLength+" characters long.";	
                    isValid = false;
                } else if (!uppercaseRegex.test(password)) {
                    passwordError.textContent = "Password must include at least one uppercase letter.";
                    isValid = false;
                } else if (!lowercaseRegex.test(password)) {
                    passwordError.textContent = "Password must include at least one lowercase letter.";
                    isValid = false;
                } else if (!digitRegex.test(password)) {
                    passwordError.textContent = "Password must include at least one number.";
                    isValid = false;
                } else if (!specialCharRegex.test(password)) {
                    passwordError.textContent = "Password must include at least one special character.";
                    isValid = false;
                } else {
                    passwordError.textContent = ""; // Clear error
                }

                return isValid;
            }

            // Validate Confirm Password
            function validateConfirmPassword() {
                const newPassword = newPasswordInput.value.trim();
                const confirmPassword = confirmPasswordInput.value.trim();

                if (newPassword !== confirmPassword) {
                    confirmPasswordError.textContent = "Passwords do not match.";
                    return false;
                } else {
                    confirmPasswordError.textContent = ""; // Clear error
                    return true;
                }
            }

            // Validate Form
            function validateForm() {
                const isNewPasswordValid = validateNewPassword();
                const isConfirmPasswordValid = validateConfirmPassword();
                const isOldPasswordValid = oldPasswordInput.value.trim().length > 0;

                oldPasswordError.textContent = isOldPasswordValid
                    ? ""
                    : "Old password is required.";

                return isNewPasswordValid && isConfirmPasswordValid && isOldPasswordValid;
            }

            // Update Save Button State
            function updateSaveButtonState() {
                saveChangesButton.disabled = !validateForm();
            }

            // Attach event listeners for real-time validation
            oldPasswordInput.addEventListener("input", updateSaveButtonState);
            newPasswordInput.addEventListener("input", updateSaveButtonState);
            confirmPasswordInput.addEventListener("input", updateSaveButtonState);

            // Attach validation to form submission
            document.getElementById("changePasswordForm").addEventListener("submit", function (event) {
                if (!validateForm()) {
                    event.preventDefault(); // Prevent form submission if validation fails
                    alert("Please fix the errors before submitting.");
                }
            });
        });


	// for deleting account 
        function confirmDelete() {
            return confirm("Are you sure you want to delete your account? This action cannot be undone.");
        }
            