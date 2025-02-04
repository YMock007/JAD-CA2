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
    <style>
        @charset "UTF-8";
        :root {
            --bg-primary: #474747;
            --bg-secondary: #17a2b8;
            --text-white: #ffffff;
            --text-blue: #007bff;
            --text-teal: #17a2b8;
        }

        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
        }

        #main-section {
        	max-width: 1280px;
        	margin: 1.5rem auto;
            min-height: 50vh;
            display: flex;
            align-items: space-between;
            justify-content: center;
            border: 1px solid black;
            box-shadow: 4px 5px 4px #474747;
            border-radius: 1.4rem;
        }

        #detailForService {
            display: flex;
            justify-content: space-evenly;
            align-items: center;
        }

        #cartItemsContainer {
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
            align-items: flex-end;
            background: var(--text-teal);
            border-top-left-radius: 1.4rem;
            border-bottom-left-radius: 1.4rem;
            position: relative;
        }
        
        #headerForCartItems {
            width: 100%;
            display: flex;
            justify-content: space-between;
            padding: 1.4rem;
        }

        #headerForCartItems > * {
            background: #fff;
            padding: 5px 20px;
            border-radius: 50px;
        }

        #cart-container {
            width: 100%;
        }

        #cartItems {
            min-height: 30vh;
            height: auto;
            width: 100%;
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
            padding: 0 1.4rem 1.4rem;
        }

        #cartImgContainer img {
            width: 200px;
            height: 150px;
            border-top-left-radius: 50px;
            border-bottom-right-radius: 50px;
        }

        .cart-item {
            width: 90%;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
            padding: 1.4rem;
            border: none;
            border-bottom: 2px solid #474747;
            color: #fff;
            text-decoration: none;
            cursor: pointer;
            background: transparent;
        }

        .cart-item:hover {
            text-decoration: none;
            cursor: pointer;
        }

        .cart-item:nth-child(1) {
            border-top: 2px solid #474747;
            border-bottom: 2px solid #474747;
        }

        #totalPriceCart {
            color: #fff;
            text-align: end;
            padding: 0 1.4rem 1.4rem;
            position: absolute;
            bottom: 20px;
        }
        
        

        #emptyCartMessage{
        	color: #474747;
        	font-size : 18px;
        	display : flex;
        	align-items : center;
        	justify-content : center;
        }

        input[type="checkbox"] {
            transform: scale(1.5);
            margin-right: 10px;
        }

        #body_header {
            text-align: center;
            font-size: 20px;
        }

        h1 {
            margin: 0 0 30px 0;
            text-align: center;
        }

        input[type="text"], input[type="password"], input[type="date"], input[type="datetime"], input[type="email"], input[type="number"], input[type="search"], input[type="tel"], input[type="time"], input[type="url"], textarea, select {
            background: rgba(255, 255, 255, 0.1);
            border: none;
            font-size: 16px;
            padding: 8px;
            width: 100%;
            background-color: #e8eeef;
            color: black;
            box-shadow: 0 1px 0 rgba(0, 0, 0, 0.03) inset;
            margin-bottom: 5px;
            outline: none;
        }

        input[type="radio"], input[type="checkbox"] {
            margin: 0 4px 8px 0;
        }

        select {
            padding: 6px;
            height: 32px;
            border-radius: 2px;
        }

        button {
            padding: 19px 39px 18px 39px;
            color: #FFF;
            background-color: #4bc970;
            font-size: 18px;
            text-align: center;
            font-style: normal;
            border-radius: 5px;
            width: 100%;
            border: 1px solid #3ac162;
            margin-bottom: 10px;
            box-sizing: border-box;
        }

        fieldset {
            border: none;
        }

        legend {
            margin-bottom: 10px;
        }

        label {
            display: block;
            margin-bottom: 8px;
        }

        label.light {
            font-weight: 300;
            display: inline;
        }

        .number {
            background-color: #5fcf80;
            color: #fff;
            display: inline-block;
            font-size: 0.8em;
            margin-right: 4px;
            line-height: 30px;
            text-align: center;
            text-shadow: 0 1px 0 rgba(255, 255, 255, 0.2);
            border-radius: 100%;
        }

        #inputsUserData {
            max-height: 80vh;
            overflow-y: auto;
        }

        @media only screen and (min-width: 1360px) {
            .cd__main {
                max-width: 1280px;
                margin-left: auto;
                margin-right: auto;
                padding: 24px;
            }
        }
    </style>
</head>
<body>
    <%@ include file="/views/Util/components/header/header.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>

    <%
		HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
		HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");
		
		if (person != null) {
		    boolean isCartEmpty = (cart == null || cart.isEmpty());
		    boolean isBookingEmpty = (booking == null || booking.isEmpty());
		
		    if (isCartEmpty && isBookingEmpty) {
		%>
		    <div class="container-fluid">
		        <div id="emptyCartMessage" class="text-center">
		            <p>No service in the cart</p>
		        </div>
		    </div>
		<%
		    } else {
		%>
		    <div class="container-fluid">
		        <div class="row" id="main-section">
		            <div id="cartItemsContainer" class="col-lg-8 col-md-8 col-sm-12">
		                <%
		                List<Service> cartItems = ServiceList.getServicesByCart(cart);
		                %>
		                <div id="headerForCartItems">
		                    <div>Your Cart...</div>
		                    <span><%= cartItems.size() %></span>
		                </div>
		                <div id="cartItems">
		                    <%
		                    float totalPriceForCart = 0;
		                    if (cartItems.size() > 0) {
		                        for (Service service : cartItems) {
		                    %>                  
		                        <div id="detailForService">
		                            <form action="${pageContext.request.contextPath}/CartServlet" method="POST" class="d-inline-block">
		                                <input type="hidden" name="serviceId" value="<%= service.getId() %>">
		                                <input type="hidden" name="bookNow" value="true">
		                                <%
		                                boolean isChecked = (booking != null && booking.containsKey(service.getId()));
		                                String actionValue = isChecked ? "remove" : "add";
		                                %>
		                                <input type="hidden" name="action" value="<%= actionValue %>">
		                                <label for="serviceCheck_<%= service.getId() %>" style="background : transparent">
		                                    <input type="checkbox" id="serviceCheck_<%= service.getId() %>" name="serviceCheck" <%= isChecked ? "checked" : "" %> onchange="this.form.submit()"/>
		                                </label>
		                            </form>
		                            <form action="${pageContext.request.contextPath}/serviceServlet" method="GET" id="cart-container">
		                                <input type="hidden" name="service_id" value="<%= service.getId() %>" />
		                                <button class="cart-item" type="submit">
		                                    <div id="cartImgContainer">
		                                        <img src="<%=service.getImageUrl() %>" alt="<%= service.getName() %>" class="img-fluid rounded">
		                                    </div>
		                                    <div><%= service.getName() %></div>
		                                    <div id="cartPrice">S$<%= service.getPrice() %></div>
		                                </button>
		                            </form>
		                            <form action="${pageContext.request.contextPath}/CartServlet" method="POST" class="d-inline-block">
		                                <input type="hidden" name="bookNow" value="false">
		                                <input type="hidden" name="action" value="remove">
		                                <input type="hidden" name="serviceId" value="<%= service.getId() %>">
		                                <button id="removeCartBtn" type="submit" class="btn btn-danger">
		                                    <i class="fa-solid fa-xmark"></i>
		                                </button>
		                            </form>
		                        </div>
		                        <%
		                        totalPriceForCart += service.getPrice();
		                        }
		                    }
		                    %>  
		                </div>
		                <div id="totalPriceCart">Total Payable Price: S$<%= totalPriceForCart %></div>
		            </div>
		            <div id="inputsUserData" class="col-lg-4 col-md-4 col-sm-12">
					    <form action="<%= request.getContextPath() %>/BookingServlet" method="post" onsubmit="return validateForm()">
					        <input type="hidden" name="memberId" value="<%= person.getId() %>">
					    
					        <h1>Booking Form</h1>
					    
					        <!-- Phone Number Input -->
					        <div class="form-group">
					            <label for="phoneNumber">Phone Number</label>
					            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" pattern="\d{8}" required>
					        </div>
					    
					        <!-- Address Input -->
					        <div class="form-group">
					            <label for="address">Address</label>
					            <input type="text" class="form-control" id="address" name="address" required>
					        </div>
					    
					        <!-- Postal Code Input -->
					        <div class="form-group">
					            <label for="postalCode">Postal Code</label>
					            <input type="text" class="form-control" id="postalCode" name="postalCode" pattern="\d{6}" required>
					        </div>
					    
					        <!-- Appointment Date Input -->
					        <div class="form-group">
					            <label for="appointmentDate">Appointment Date</label>
					            <input type="date" class="form-control" id="appointmentDate" name="appointmentDate" required>
					        </div>
					    
					        <!-- Appointment Time Input -->
					        <div class="form-group">
					            <label for="appointmentTime">Appointment Time</label>
					            <select class="form-select" id="appointmentTime" name="appointmentTime"  style="font-size : 14px;" required>
					                <option value="" selected disabled></option>
					                <% 
					                    int startHour = 8;
					                    int interval = 4;
					                    int endHour = 20;
					                    for (int hour = startHour; hour <= endHour; hour += interval) {
					                        String timeValue = String.format("%02d:00", hour); 
					                        String displayTime = (hour > 12 ? (hour - 12) : hour) + ":00 " + (hour >= 12 ? "PM" : "AM");
					                %>
					                    <option value="<%= timeValue %>"><%= displayTime %></option>
					                <% 
					                    }
					                %>
					            </select>
					        </div>
					    
					        <!-- Special Request Text Area -->
					        <div class="form-group">
					            <label for="specialRequest">Special Request</label>
					            <textarea id="specialRequest" name="specialRequest" class="form-control" placeholder="Enter any special requests here" style="max-height: 150px; min-height: 100px; overflow-y: auto;"></textarea>
					        </div>
					
					        <% 
					            if (booking == null || booking.isEmpty()) {
					        %>
					            <div class="alert alert-warning" role="alert">
					                Please select a service for booking.
					            </div>
					        <% 
					            }
					        %>
					    
					        <!-- Submit Button -->
					        <button type="submit" class="btn btn-primary" <% if (isBookingEmpty) { %> disabled <% } %>>Checkout</button>
					    </form>
					</div>
		        </div>
		    </div>
		<%
		    }
		} else {
		%>
		<div class="alert alert-danger">You must be logged in to view this page.</div>
		
		<%
		}
		%>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<%= request.getContextPath() %>/views/member/cart/validate.js" type="text/javascript"></script>
    
</body>
</html>
