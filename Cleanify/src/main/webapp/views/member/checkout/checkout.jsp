<%@ include file="/views/Util/auth/memberAuth.jsp" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map, java.util.stream.Collectors" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Services.Service, Services.ServiceList, Persons.Person, Persons.PersonList, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cleanify</title>
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
        margin-top: 30px;
    }
    
    #bookingForm{
    display: grid;
        grid-template-columns: 1fr 1fr;
        justify-content: center;
        gap: 20px;
    }


    .form-group label {
    	font-size: 18px;
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
        border: 2px solid var(--text-teal);
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
        margin-bottom: 10px;
    }

    .tab-button.active {
        background: var(--text-teal);
        color: white;
        font-weight: bold;
        transition: background 0.3s;
    }

    .tab-button {
        flex: 1;
        padding: 5px;
        text-align: center;
        cursor: pointer;
        background: #fff;
        color: var(--text-teal);
        border: 3px solid var(--text-teal);
   
    }

    .tab-content {
        display: none;
        background: white;
        border-radius: 5px;
    }

    .tab-content.active {
        display: block;
    }
    
    .priceSummary {
	    width: 100%;
	}
	
	.summaryRow {
	    display: flex;
	    justify-content: space-between;
	    padding: 5px;
	    border-bottom: 1px solid #eee;
	}
	
	.totalRow {
	    font-weight: bold;
	    color: #007bff; 
	    border-top: 2px solid #ddd;
	    padding-top: 5px;
	}
	
	.label {
	    font-weight: 500;
	    color: #333;
	}
	
	.value {
	    font-weight: bold;
	    color: #222;
	}


    /* Stripe Card Element */
    #card-element {
        padding: 10px;
        border: 1px solid var(--text-teal);
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
    .payment-button, .payment-button-email {
        width: 100%;
        padding: 12px;
        background: var(--text-teal);
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 20px;
        margin-top: 10px;
        transition: background 0.3s ease-in-out;
    }

    .payment-button:hover,.payment-button-email:hover {
        background: var(--button-hover);
    }

    /* Disabled Button */
    .payment-button:disabled, .payment-button-email:disabled {
        background: gray;
        cursor: not-allowed;
    }
    
    

</style>
</head>
<body>

	<%@ include file="/views/Util/components/header/header.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>

    <%
	float bookingPrice = (Float) session.getAttribute("totalPrice");
    float gst = (float) (bookingPrice * 0.09) ;
    float totalPrice = bookingPrice + gst ;

    HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");

    // Extract service IDs (keys) into a List<Integer>
    List<Integer> serviceIds = new ArrayList<>();
    if (booking != null) {
        serviceIds.addAll(booking.keySet()); // Extract keys
    }

    // Convert Java List to JSON Array
    String serviceIdsJson = new Gson().toJson(serviceIds);
 
    // ✅ Convert booking cart to JSON
    JSONObject jsonBooking = new JSONObject();
    if (booking != null) {
        for (Map.Entry<Integer, Integer> entry : booking.entrySet()) {
            jsonBooking.put(String.valueOf(entry.getKey()), entry.getValue());
        }
    }
    
    
	%>

	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"> 


		<div class="container-fluid row" id="container">
	    <form id="bookingForm" action="${pageContext.request.contextPath}/SendEmailServlet" method="post">
	        <div class="personalInfo col-sm-12">
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
	        </div>
	
	        <div class="paymentInfo col-sm-12">
	            <div class="priceSummary">
	                <div class="summaryRow">
	                    <span class="label">Number of Services:</span>
	                    <span class="value"><%= booking.size() %></span>
	                </div>
	                <div class="summaryRow">
	                    <span class="label">Service Price:</span>
	                    <span class="value">S$<%= bookingPrice %></span>
	                </div>
	                <div class="summaryRow">
	                    <span class="label">GST (9%):</span>
	                    <span class="value">S$<%= gst %></span>
	                </div>
	
	                <div class="summaryRow totalRow">
	                    <span class="label">Total Amount:</span>
	                    <span class="value">S$<%= totalPrice %></span>
	                </div>
	            </div>
	
	            <div class="tab-buttons">
	                <button type="button" class="tab-button active" onclick="switchTab('credit-card')">Bank Card</button>
	                <button type="button" class="tab-button" onclick="switchTab('qr-transfer')">Transfer It Now</button>
	            </div>
	
	            <!-- Credit Card Payment -->
	            <div id="credit-card" class="tab-content active">
	                <h4>Billing Address</h4>
	                <div class="form-group">
	                    <label for="billingAddress">Street Address</label>
	                    <input type="text" id="billingAddress" name="billingAddress" value = " " required>
	                </div>
	                <div class="form-group">
	                    <label for="billingCity">City</label>
	                    <input type="text" id="billingCity" name="billingCity"  value = " " required>
	                </div>
	                <div  class="form-group">
	                    <label for="billingPostalCode">Postal Code</label>
	                    <input type="text" id="billingPostalCode" name="billingPostalCode"  value = " " required>
	                </div>
	
	                <h4>Payment Information</h4>
	                <div id="card-element"></div>
	                <div id="card-errors" style="color: red; margin-top: 10px;"></div>
	
	                <button id="submit-payment" class="payment-button" disabled>Pay</button>
	            </div>
	
	            <!-- QR Code Payment -->
	            <div id="qr-transfer" class="tab-content">
	                <h6>Receive QR Code via Email</h6>
	                <div class="form-group">
	                    <label for="userEmail">Email Address</label>
	                    <input type="email" id="userEmail" name="email" required>
	                    <div class="useMyEmail">
	                        <input type="checkbox" id="useMyEmail" onchange="fillEmail()">
	                        <label for="useMyEmail">Use my registered email</label>
	                    </div>
	                </div>
	                <button id="sendQrButton" type="submit" class="payment-button-email">Send QR Code to Email</button>
	                <input type="hidden" id="totalPrice" name="totalPrice" value="<%= totalPrice%>">
	                <input type="hidden" id="totalPrice" name="bookingCart" value="<%= serviceIdsJson%>">

	            </div>
	        </div>
	    </form>
	</div>
		

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<%= request.getContextPath() %>/views/member/checkout/cardPayment.js"></script>
	<script>
	
	// Function to switch tabs between payment methods
	function switchTab(tabId) {
	    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
	    document.querySelectorAll('.tab-button').forEach(button => button.classList.remove('active'));
	    document.getElementById(tabId).classList.add('active');
	    event.target.classList.add('active');
	}
	
	// Function to enable payment buttons only if all required fields are filled
    function checkFormCompletion() {
        let requiredFieldsBankCard = ['phoneNumber', 'address', 'postalCode', 'appointmentDate', 'appointmentTime', 'billingAddress', 'billingCity', 'billingPostalCode'];
        let allFilledBankCard = requiredFieldsBankCard.every(id => document.getElementById(id).value.trim() !== "");

        let requiredFieldsEmail = ['phoneNumber', 'address', 'postalCode', 'appointmentDate', 'appointmentTime', 'userEmail'];
        let allFilledEmail = requiredFieldsEmail.every(id => document.getElementById(id).value.trim() !== "");

        // Enable card payment button only if form is filled
        document.getElementById("submit-payment").disabled = !allFilledBankCard;
        document.getElementById("sendQrButton").disabled = !allFilledEmail;
  
 
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
        checkFormCompletion();
    }

    // Attach event listeners to all input fields to check form completion
    document.addEventListener("DOMContentLoaded", function() {
        let fields = document.querySelectorAll("input, select, textarea");
        fields.forEach(field => {
            field.addEventListener("input", checkFormCompletion);
        });
    });
    
    
    let bookingCart = <%= jsonBooking.toString() %>;

    let bookingCartArray = Object.keys(bookingCart).map(key => parseInt(key));
    </script>
</body>
</html>
