<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="signIn.css">
    <script src="https://accounts.google.com/gsi/client" async defer></script>
    <title>Sign In</title>
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
    <div class="form-container sign-in-container">
        <form method="POST">
            <h1 id="createAccount">Sign In</h1>
            <div class="social-container">
                <div id="g_id_onload"
                     data-client_id="399284078220-cap97h60puq7rb28r0335g07p10a81ib.apps.googleusercontent.com"
                     data-callback="handleGoogleSignIn"
                     data-auto_prompt="false">
                </div>
                <div class="g_id_signin" data-type="standard"></div>
            </div>
            <span class="mb-10px">or use your account</span>
            <div class="input-container">
                <i class="fas fa-envelope"></i>
                <input type="email" id="signinEmail" name="signinEmail" placeholder="Email" required />
            </div>
            <div class="input-container">
                <i class="fas fa-lock"></i>
                <input type="password" id="signinPassword" name="signinPassword" placeholder="Password" required />
                <i class="fas fa-eye toggle-password" onclick="togglePassword('signinPassword')"></i>
            </div>
            <button type="submit" formaction="<%= request.getContextPath() %>/SigninServlet">Sign In</button>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <h1>Join the Clean Revolution!</h1>
                <p>Create an account and step into a world of spotless possibilities. Sign up now and let’s make clean happen together!</p>
                <button class="ghost" id="signUpRedirect">Sign Up</button>
            </div>
        </div>
    </div>
</div>

<script>
function handleGoogleSignIn(response) {
    try {
        // Decode the Google ID token
        const userData = jwt_decode(response.credential);

        // Extract user's email
        const email = userData.email;

        // Create a form programmatically to send a POST request
        const form = document.createElement('form');
        form.method = 'POST'; // Specify the method here
        form.action = '<%= request.getContextPath() %>/GoogleSignInServlet'; // Your servlet URL

        // Add email as a hidden input
        const emailField = document.createElement('input');
        emailField.type = 'hidden';
        emailField.name = 'signinEmail';
        emailField.value = email;

        // Add a placeholder for the password
        const passwordField = document.createElement('input');
        passwordField.type = 'hidden';
        passwordField.name = 'signinPassword';
        passwordField.value = 'Google_OAuth'; // Placeholder for Google users

        // Append fields to the form
        form.appendChild(emailField);
        form.appendChild(passwordField);

        // Append form to the document and submit
        document.body.appendChild(form);
        form.submit();
    } catch (error) {
        console.error("Error during Google Sign-In:", error);
        alert("An error occurred during Google Sign-In. Please try again.");
    }
}
	
</script>
<script src="index.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jwt-decode/build/jwt-decode.min.js"></script>
</body>
</html>
