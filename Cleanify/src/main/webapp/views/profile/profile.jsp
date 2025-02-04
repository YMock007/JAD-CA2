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
    <link rel="stylesheet" href="profile.css">
</head>

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

