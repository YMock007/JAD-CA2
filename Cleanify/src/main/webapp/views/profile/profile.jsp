<%@ page import="Persons.Person" %>
<%@ include file="/views/Util/auth/memberAuth.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>Profile with Overlay</title>
</head>
<style>

    :root {
        --bg-primary: #474747;
        --bg-secondary: #17a2b8;
        --text-white: #ffffff;
        --text-blue: #007bff;
        --text-teal: #17a2b8;
        --input-bg: #f9f9f9;
        --input-border: #ccc;
        --button-hover: #45a049;
    }

body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
}

.overlay-container {
    display: flex;
    width: 1280px;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.95);
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    overflow: hidden;
    margin-top: 30px;
    height: 80vh;
}

.sidebar {
    width: 25%;
    color: #000;
    padding: 20px;
    border: 5px solid var(--text-teal);
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
}

.sidebar ul {
    list-style: none;
    padding: 0;
    margin: 0;
    
}

.sidebar ul li {
    padding: 15px;
    text-align: center;
    cursor: pointer;
    border-radius: 5px;
    margin-bottom: 10px;
    background: var(--text-teal);
}

.sidebar ul li.active,
.sidebar ul li:hover {
    background: rgb(23,170,193);
	background: linear-gradient(113deg, rgba(23,170,193,1) 0%, rgba(146,192,207,1) 84%);
}

.form-container {
    width: 100%;
    padding: 40px;
    background: #fff;
    overflow-y: auto;
    border: 5px solid var(--text-teal);
    border-left: none;
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
}

.tab-content {
    display: none;
    gap: 10px;
}

.tab-content.active {
    display: block;
}

.form-row {
    display: flex;
    gap: 20px;
    justify-content: space-between;
    margin-bottom: 20px;
}

.form-group {
    flex: 1;
    min-width: 100%  
}   


label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
    color: #555;
}

input:focus {
    border-color: #0077B6;
    box-shadow: 0 0 5px rgba(0, 119, 182, 0.5);
    outline: none;
}

input {
    width: 50%;
    padding: 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
    box-sizing: border-box; 
    margin-bottom: 5px;
}

input:focus {
    border-color: #0077B6;
    box-shadow: 0 0 5px rgba(0, 119, 182, 0.5);
    outline: none;
}

button.btn {
    width: auto;
    padding: 10px 20px;
    cursor: pointer;
    border-radius: 8px;
    font-size: 16px;
}

button.btn-primary {
    background-color: var(--text-teal);
    color: white;
    border: none;
    transition: background 0.3s;
}

button.btn-primary:hover {
    background-color: #005f8c;
}

button.btn-danger {
    background-color: #d9534f;
    color: white;
    border: none;
    transition: background 0.3s;
}

button.btn-danger:hover {
    background-color: #b52b2b;
}

button[disabled] {
    background-color: #ccc;
    color: #666;
    cursor: not-allowed;
}

h2.form-title {
    font-size: 24px;
    margin-bottom: 20px;
    color: #333;
    font-weight: bold;
}

button#editProfile {
    margin-top: 20px;
}

button#updateProfileSection {
    margin-top: 20px;
}


h2.form-title {
    font-size: 28px; /* Larger and bold title */
    margin-bottom: 20px; /* Space below title */
    color: #333;
    font-weight: bold;
}




.toggle-password:hover {
    color: #0077B6; /* Change color on hover */
}



.toggle-password{
    position: relative;
    right: 40px;
}


.error-message {
    color: red; /* Set text color to red */
    font-size: 12px; /* Adjust font size */
    margin-top: 5px; /* Add spacing between input and error message */
    display: block; /* Ensure it takes up its own line */
}

</style>

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


<body>
<%@ include file="/views/Util/components/header/header.jsp" %>




    <div class="overlay-container">
        <!-- Sidebar -->
        <div class="sidebar">
            <ul>
                <li class="active" data-tab="general">General</li>
                <li data-tab="change-password">Change Password</li>
                <li data-tab="delete-account">Delete Account</li>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="form-container">
            <!-- General Tab -->
            <div class="tab-content active" id="general">
                <h2 class="form-title">General Information</h2>
                <form id="generalForm" action="<%=request.getContextPath()%>/UpdateProfileServlet" method="POST" >
				    <div class="form-column">
				        <div class="form-group">
						    <label for="name">Name</label>
						    <input type="text" id="name" name="name" value="<%= person.getName() %>" oninput="validateNameInline()">
						    <small class="error-message" id="nameError"></small>
						</div>
						
						<div>
						    <label for="email">Email</label>
						    <input type="email" id="email" name="email" value="<%= person.getEmail() %>" readonly>
						    <small class="error-message" id="emailError"></small>
						</div>
						
						<div class="form-group">
						    <label for="phone">Phone Number</label>
						    <input type="text" id="phone" name="phone" value="<%= person.getPhNumber() %>" oninput="validatePhoneInline()">
						    <small class="error-message" id="phoneError"></small>
						</div>
						
						<div class="form-group">
						    <label for="address">Address</label>
						    <input type="text" id="address" name="address" value="<%= person.getAddress() %>" oninput="validateAddressInline()">
						    <small class="error-message" id="addressError"></small>
						</div>
						
						<div class="form-group">
						    <label for="postalCode">Postal Code</label>
						    <input type="text" id="postalCode" name="postalCode" value="<%= person.getPostalCode() %>" oninput="validatePostalCodeInline()">
						    <small class="error-message" id="postalCodeError"></small>
						</div>

				        
				        <input type="hidden" id="is_google_user" name="is_google_user" value="<%= person.getIsGoogleUser() %>">
				    </div>
				    <div class="button-group">
				        <button type="submit" class="btn btn-primary" id="updateProfileSection">Update Profile</button>
				    </div>
				</form>
            </div>


            <!-- Change Password Tab -->
            <div class="tab-content" id="change-password">
                <h2>Change Password</h2>
                <form id="changePasswordForm" action="<%=request.getContextPath()%>/UpdatePasswordServlet" method="POST">
                    <div class="form-group">
				        <label for="oldPassword">Old Password:</label>
				        <input type="password" id="oldPassword" name="oldPassword" required>
				        <i class="fas fa-eye toggle-password" onclick="togglePassword('oldPassword')"></i>
				        <small class="error-message" id="oldPasswordError"></small>
				    </div>
				    <div class="form-group">
				        <label for="newPassword">New Password:</label>
				        <input type="password" id="newPassword" name="newPassword" required oninput="validateNewPassword()">
				        <i class="fas fa-eye toggle-password" onclick="togglePassword('newPassword')"></i>
				        <small class="error-message" id="newPasswordError"></small>
				    </div>
				    <div class="form-group">
				        <label for="confirmPassword">Repeat New Password:</label>
				        <input type="password" id="confirmPassword" name="confirmPassword" required oninput="validateConfirmPassword()">
				        <i class="fas fa-eye toggle-password" onclick="togglePassword('confirmPassword')"></i>
				        <small class="error-message" id="confirmPasswordError"></small>
				    </div>
				    <button type="submit" id="updatePasswordSection" class="btn btn-primary">Save Changes</button>
                </form>
            </div>

            <!-- Delete Account Tab -->
            <div class="tab-content" id="delete-account">
                <h2>Delete Account</h2>
                <p>Are you sure you want to delete your account? This action cannot be undone.</p>
                <form action="<%=request.getContextPath()%>/DeletePersonServlet" method="POST" onsubmit="return confirmDelete()">
                    <input type="hidden" name="userId" value="<%= person.getId() %>">
                    <button type="submit" class="btn btn-danger">Delete Account</button>
                </form>
            </div>
        </div>
    </div>

    <script src="profile.js"></script>
</body>

</html>

