<%@ include file="/views/Util/auth/memberAuth.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "Saved.Saved, Saved.SavedServlet, Saved.SavedList, Persons.Person, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEJao8i4U2C5ndp5o6Ps7vcs6b4gT4w42Z1o90W1tBz2Fh9l/g9P7pc2qVSdC" crossorigin="anonymous">
	<style>
	/* General Reset */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* Body */
body {
    background: linear-gradient(#eceffe, #ced6fb);
    min-height: 100vh;
    font-family: Arial, sans-serif;
}

/* Container for the grid */
.container {
    display: flex;
    justify-content: center;
    padding: 20px;
    height: 100%
}

/* Grid layout */
.grid {
height: auto;
    display: grid;
    grid-template-columns: repeat(3, 1fr); 
    gap: 20px;
    width: 100%;
    max-width: 1200px;
}


/* Card style */
.card {
height: auto;
    background-color: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    cursor: pointer;
    paddding : auto;
}

/* Image inside the card */
.card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
}

/* Card body */
.card-body {
    padding: 15px;
}

/* Card text */
.card-text .servicenName {
    font-size: 17px;
    font-weight: 700;
}

.card-text p {
    font-size: 14px;
    color: gray;
}

#btns {
    display: flex;
    gap: 1.4rem;
    align-items: center;
	  justify-content: center;
	  padding: 20px;
}

/* General button styles */
button.btn {
    color: white;
    border: none;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s ease;
}

/* Add to Cart button */
.add-to-cart-btn {
    background-color: #007bff; 
}

.add-to-cart-btn:hover {
    background-color: #0056b3;
}

/* Remove button */
.remove-btn {
    background-color: #ff4d4d;
}

.remove-btn:hover {
    background-color: #e60000;
}

/* Card hover effects */
.card:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.text-center p {
    font-size: 18px;
    color: gray;
    margin-top: 20px;
}

.card-item-submit-Btn{
	border: none;
	outline : none;
	text-align : center;
	width: 100%;
	background : transparent;
}

/* "Book Now" Button */
		.primary-btn {
		    background-color: #4CAF50; /* Green */
		    color: white;
		}
		
		.primary-btn:hover {
		    background-color: #388E3C; 
		    transform: translateY(-2px);
		}
		
		/* "Add to Cart" Button */
		.secondary-btn {
		    background-color: #2196F3; /* Blue */
		    color: white;
		}
		
		.secondary-btn:hover {
		    background-color: #1565C0; /* Darker Blue */
		    transform: translateY(-2px);
		}


/* Responsive design for smaller screens */
@media screen and (max-width: 768px) {
    .grid {
        grid-template-columns: repeat(2, 1fr); /* Two columns on medium screens */
    }
}

@media screen and (max-width: 480px) {
    .grid {
        grid-template-columns: 1fr; /* One column on small screens */
    }
}
	
	</style>
</head>
<body>
    <%@ include file="/views/Util/components/header/header.jsp" %>
    
    <div class="container">
    <% 
    List<Saved> savedItems = person.getSavedItems();
    if (savedItems.isEmpty()) { 
    %>
        <div class="text-center">
            <p>No saved services available.</p>
        </div>
    <% } else { %>
        <div class="grid" id="cards">
		    <% if (savedItems != null && !savedItems.isEmpty()) { %>
		        <% for (Saved saved : savedItems) { %>
		            <div class="card">
		                <form action="${pageContext.request.contextPath}/serviceServlet" method="GET" id="cart-container">
		                    <input type="hidden" name="service_id" value="<%= saved.getServiceId() %>" />
		                    <button class="card-item-submit-Btn" type="submit" class="btn btn-secondar">
			                    <img src="<%= saved.getServiceImageUrl() %>" 
	                         		alt="<%= saved.getServiceName() %>" class="img-fluid rounded">
				                <div class="card-body">
				                    <p class="serviceName">Service name: <%= saved.getServiceName() %></p>
				                    <p>Category: <%= saved.getCategoryName() %></p>
				                </div>
				              </button>
		                </form>
		                <div id="btns">
		                    <form action="${pageContext.request.contextPath}/CartServlet" method="post">
						        <input type="hidden" name="action" value="add">
						        <input type="hidden" name="bookNow" value="True">
						        <input type="hidden" name="serviceId" value="<%=  saved.getServiceId() %>">
						        <button class="btn primary-btn" type="submit">Book Now</button>
						    </form>
						    <form action="${pageContext.request.contextPath}/CartServlet" method="post">
						        <input type="hidden" name="action" value="add">
						        <input type="hidden" name="bookNow" value="false">
						        <input type="hidden" name="serviceId" value="<%=  saved.getServiceId() %>">
						        <button class="btn secondary-btn" type="submit">Add to Cart</button>
						    </form>
		
		                    <!-- Remove Saved Item Form -->
		                    <form action="${pageContext.request.contextPath}/SavedServlet" method="post">
		                        <input type="hidden" name="action" value="remove">
		                        <input type="hidden" name="personId" value="<%= person.getId() %>">
		                        <input type="hidden" name="saved_id" value="<%= saved.getSavedId() %>">
		                        <button class="btn remove-btn" type="submit">Remove</button>
		                    </form>
		                </div>
		            </div>
		        <% } %>
		    <% } else { %>
		        <p>No saved items available.</p>
		    <% } %>
		</div>

    <% } %>
</div>

    <%@ include file="/views/Util/notification.jsp" %>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pzjw8f+ua7Kw1TIq0nB0FuXuSgCd0mY5u+G2A3vD61BpkH39OjAKDAAaX20G4NdO" crossorigin="anonymous"></script>

</body>
</html>
