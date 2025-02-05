<%@ include file="/views/Util/auth/memberAuth.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Services.Service, Services.ServiceList, Persons.Person, Persons.PersonList, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cleanify</title>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://js.stripe.com/v3/"></script>
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

    * {
        padding: 0;
        margin: 0;
        box-sizing: border-box;
        font-family: Arial, sans-serif;
    }

    #container {
        max-width: 1280px;
        height: 80vh;
        margin: auto;
        display: grid;
        grid-template-columns: 1fr 1fr;
        justify-content: center;
        gap: 20px;
        margin-top: 30px;
    }

    /* Form Styling */
    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        font-weight: bold;
        display: block;
        margin-bottom: 5px;
    }

    .form-group input, .form-group textarea, .form-group select {
        width: 100%;
        padding: 10px;
        border: 1px solid var(--input-border);
        border-radius: 4px;
        font-size: 16px;
        background: var(--input-bg);
        transition: border 0.3s ease-in-out;
    }

    .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
        border-color: var(--text-teal);
        outline: none;
    }

    /* Container Styling */
    .paymentInfo, .personalInfo {
        padding: 20px;
        border: 2px solid var(--bg-primary);
        border-radius: 5px;
        height: 100%;
        overflow-y: auto;
        background: white;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    }

    /* Payment Form */
    .tab-buttons {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
    }

    .tab-button {
        flex: 1;
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border: none;
        background: var(--text-teal);
        color: white;
        font-weight: bold;
        transition: background 0.3s;
    }

    .tab-button.active {
        background: #fff;
        color: var(--text-teal);
        border-bottom: 3px solid var(--text-teal);
    }

    .tab-content {
        display: none;
        padding: 20px;
        background: white;
        border-radius: 5px;
    }

    .tab-content.active {
        display: block;
    }

    /* Stripe Card Element */
    #card-element {
        padding: 10px;
        border: 1px solid var(--input-border);
        border-radius: 4px;
        background: var(--input-bg);
    }
    
     .useMyEmail{
     	margin-top: 10px;
	    display: grid;
	    grid-template-columns: 0.1fr 1fr;'
	    align-items: center;
    }

    /* Payment Button */
    .payment-button {
        width: 100%;
        padding: 12px;
        background: var(--text-teal);
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
        margin-top: 10px;
        transition: background 0.3s ease-in-out;
    }

    .payment-button:hover {
        background: var(--button-hover);
    }

    /* Disabled Button */
    .payment-button:disabled {
        background: gray;
        cursor: not-allowed;
    }

</style>
</head>
<body>

	<%@ include file="/views/Util/components/header/header.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>

    <%
	HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");
	float totalPrice = (Float) session.getAttribute("totalPrice");
	
	%>

	<input type="hidden" id="totalPrice" value="<%= totalPrice * 100 %>"> 
	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"> 


		<div class="container-fluid row" id="container">
		    <div class="personalInfo col-sm-12">
		        <form id="bookingForm" action="${pageContext.request.contextPath}/BookingServlet" method="post">
		            <input type="hidden" id="memberId" name="memberId" value="<%= person.getId() %>">
		
		            <h1>Booking Form</h1>
		
		            <!-- Phone Number -->
		            <div class="form-group">
		                <label for="phoneNumber">Phone Number</label>
		                <input type="tel" id="phoneNumber" name="phoneNumber" pattern="\d{8}" required>
		            </div>
		
		            <!-- Address -->
		            <div class="form-group">
		                <label for="address">Address</label>
		                <input type="text" id="address" name="address" required>
		            </div>
		
		            <!-- Postal Code -->
		            <div class="form-group">
		                <label for="postalCode">Postal Code</label>
		                <input type="text" id="postalCode" name="postalCode" pattern="\d{6}" required>
		            </div>
		
		            <!-- Appointment Date -->
		            <div class="form-group">
		                <label for="appointmentDate">Appointment Date</label>
		                <input type="date" id="appointmentDate" name="appointmentDate" required>
		            </div>
		
		            <!-- Appointment Time -->
		            <div class="form-group">
		                <label for="appointmentTime">Appointment Time</label>
		                <select id="appointmentTime" name="appointmentTime" required>
		                    <option value="" selected disabled>Select a time</option>
		                    <% 
		                        int startHour = 8;
		                        int interval = 3;
		                        int endHour = 18;
		                        for (int hour = startHour; hour <= endHour; hour += interval) {
		                            String timeValue = String.format("%02d:00", hour); 
		                            String displayTime = (hour > 12 ? (hour - 12) : hour) + ":00 " + (hour >= 12 ? "PM" : "AM");
		                    %>
		                        <option value="<%= timeValue %>"><%= displayTime %></option>
		                    <% } %>
		                </select>
		            </div>
		
		            <!-- Special Request -->
		            <div class="form-group">
		                <label for="specialRequest">Special Request</label>
		                <textarea id="specialRequest" name="specialRequest"></textarea>
		            </div>
		        </form>
		    </div>
		
		    <div class="paymentInfo col-sm-12">
		        <div class="tab-buttons">
		            <button class="tab-button active" onclick="switchTab('credit-card')">Bank Card</button>
		            <button class="tab-button" onclick="switchTab('qr-transfer')">Transfer It Now</button>
		        </div>
		
		        <!-- Credit Card Payment -->
		        <div id="credit-card" class="tab-content active">
		            <h4>Billing Address</h4>
		            <div class="form-group">
		                <label for="billingAddress">Street Address</label>
		                <input type="text" id="billingAddress" name="billingAddress" required>
		            </div>
		            <div class="form-group">
		                <label for="city">City</label>
		                <input type="text" id="city" name="city" required>
		            </div>
		
		            <h4>Payment Information</h4>
		            <div id="card-element" class="form-group"></div>
		            <div id="card-errors" style="color: red; margin-top: 10px;"></div>
		
		            <button id="submit-payment" class="payment-button" disabled>Complete Payment</button>
		        </div>
		
		        <!-- QR Code Payment -->
		        <div id="qr-transfer" class="tab-content">
		            <h6>Receive QR Code via Email</h6>
		            <div class="form-group">
		                <label for="userEmail">Email Address</label>
		                <input type="email" id="userEmail" name="userEmail" required>
		                <div class="useMyEmail">
		                    <input type="checkbox" id="useMyEmail" onchange="fillEmail()">
		                    <label for="useMyEmail">Use my registered email</label>
		                </div>
		            </div>
		            <button id="sendQrButton" type="submit" class="payment-button" disabled>Send QR Code to Email</button>
		        </div>
		    </div>
		</div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<%= request.getContextPath() %>/views/member/cart/validate.js" type="text/javascript"></script>
	<script src="<%= request.getContextPath() %>/views/member/checkout/checkout.js" type="text/javascript"></script>
	<script>
	// Function to enable payment buttons only if all required fields are filled
    function checkFormCompletion() {
        let requiredFields = ['phoneNumber', 'address', 'postalCode', 'appointmentDate', 'appointmentTime', 'billingAddress', 'city'];
        let allFilled = requiredFields.every(id => document.getElementById(id).value.trim() !== "");

        // Enable card payment button only if form is filled
        document.getElementById("submit-payment").disabled = !allFilled;

        // Check email and booking fields for QR payment
        let userEmail = document.getElementById("userEmail").value.trim();
        let qrButton = document.getElementById("sendQrButton");

        if (userEmail !== "" && allFilled) {
            qrButton.disabled = false;
        } else {
            qrButton.disabled = true;
        }
    }

    // Function to fill email if checkbox is selected
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
        checkFormCompletion(); // Check if form is completed after setting email
    }

    // Attach event listeners to all input fields to check form completion
    document.addEventListener("DOMContentLoaded", function() {
        let fields = document.querySelectorAll("input, select, textarea");
        fields.forEach(field => {
            field.addEventListener("input", checkFormCompletion);
        });
    });</script>
</body>
</html>
