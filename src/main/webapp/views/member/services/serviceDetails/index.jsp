<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Services.Service" %>
<%@ page import="Persons.Person" %>
<%@ page import="Reviews.Review" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cleanify</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/scrollreveal/4.0.5/scrollreveal.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/crypto-js@3.1.9-1/crypto-js.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=arrow_forward" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
	<style>
	 @charset "UTF-8";

        /* General Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body */
        body {
            background: linear-gradient(#eceffe, #ced6fb);
            font-family: Arial, sans-serif;
        }

        /* Service Container */
        .service-container {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 70vh;
            padding: 2rem;
        }

        /* Service Box */
        .service-box {
            width: 100%;
            max-width: 1200px;
            background-color: #f8f9fa;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            padding: 2rem;
        }

        /* Service Detail */
        .service-detail {
            display: grid;
            grid-template-columns : 1fr 1fr;
            align-items: start;
            gap: 1.5rem;
            border-radius: 20px;
            width: 100%;
        }

        /* Image Container */
        .service-image {
            width: 100%;
            min-width: 500px;
            min-height: 300px;
            height: auto;
            overflow: hidden;
            border-radius: 10px;
        }

        .service-image img {
            width: 100%;
            height: auto;
            border-radius: 6px;
        }

        /* Text Container */
        .service-text {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
        }

        .service-name {
            font-size: 2.5rem;
            font-weight: bold;
            color: #000;
        }

        .service-description {
            font-size: 1.25rem;
        }
        
        #btns {
		    display: flex;
		    justify-content: center;
		    gap: 1.4rem;
		}
		
		button.btn {
		    margin-top: 1rem;
		    padding: 0.5rem 1.5rem;
		    font-size: 1rem;
		    border: none;
		    cursor: pointer;
		    transition: background-color 0.3s ease, color 0.3s ease;
		    color: #fff;
		}
		
		.add-to-cart-btn {
		    background-color: var(--bg-primary);
		}
		
		.add-to-cart-btn:hover {
		    background-color: #333333;
		    color: #ffffff;
		}
		
		.save-for-later-btn {
		    background-color: var(--bg-secondary);
		}
		
		.save-for-later-btn:hover {
		    background-color: #138496;
		    color: #ffffff;
		}
		        

        /* Error Message */
        .error-message {
            color: red;
            font-size: 1.2rem;
        }
        
        .review-container {
		    display: flex;
		    flex-wrap: wrap;
		    gap: 20px;
		    justify-content: space-between;
		}
		
		.review {
		    width: calc(33.33% - 20px);
		    padding: 15px;
		    border: 1px solid #ccc;
		    border-radius: 8px;
		    background-color: #f9f9f9;
		    margin-bottom: 20px;
		}
		
		.review-rating,
		.review-comment,
		.review-date {
		    margin-bottom: 10px;
		}
		
		.review-rating strong,
		.review-comment strong,
		.review-date strong {
		    color: #333;
		}
		
		.review-comment,
		.review-date {
		    font-size: 14px;
		    color: #555;
		}
		
		@media (max-width: 768px) {
		    .review {
		        width: calc(50% - 20px);
		    }
		}
		
		@media (max-width: 480px) {
		    .review {
		        width: 100%;
		    }
		}


		        
        
        

        /* Responsive Design */
        @media (max-width: 768px) {
            .service-box {
                padding: 1rem;
            }

            .service-detail {
                padding: 1rem;
                margin: 1rem;
            }

            .service-image {
                max-width: 100%;
            }

            .service-name {
                font-size: 2rem;
            }

            .service-description {
                font-size: 1rem;
            }
        }

        .back-button {
            position: absolute;
            top: 150px;
            left: 20px;
            background-color: #000;
            color: #fff;
            padding: 10px;
            border-radius: 5px;
            text-decoration: none;
            font-size: 1rem;
        }

        .back-button:hover {
            opacity: 0.8;
        }
        #btns {
		    display: grid;
		    grid-template-columns: repeat(2, 1fr);
		    gap: 1.4rem; /* Adjusted spacing between buttons */
		    justify-content: center;
		    align-items: center;
		    max-width: 600px;
		    margin: 0 auto;
		}
		
		.save-for-later {
		    grid-column: span 2;
		}
		
		.btn {
		    display: block;
		    width: 100%;
		    font-size: 1rem;
		    font-weight: 600;
		    text-align: center;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		    transition: transform 0.2s ease, background-color 0.3s ease;
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
		
		/* "Save to Wishlist" Button */
		.tertiary-btn {
		    background-color: #FF9800; /* Orange */
		    color: white;
		}
		
		.tertiary-btn:hover {
		    background-color: #F57C00; /* Darker Orange */
		    transform: translateY(-2px);
		}
		
		.btn:focus {
		    outline: 2px solid #000;
		    outline-offset: 2px;
		}
		

		        
        
        </style>
</head>

<body>

    <%@ include file="/views/Util/components/header/header.jsp" %>


    
    <a class="back-button" href="${pageContext.request.contextPath}/views/member/services/index.jsp">Back to Services</a>
    <div class="service-container">
	    <div class="service-box">
	    <%
	        Service service = (Service) session.getAttribute("service");
	        if (service != null) {
	    %>
	    <div id="<%= service.getId() %>" class="service-detail">
	        <div class="service-image">
	            <img src="<%=service.getImageUrl() %>" alt="<%= service.getName() %>" class="img-fluid rounded">
	        </div>
	        <div class="service-text">
	            <h1 class="service-name"><%= service.getName() %></h1>
	            <p class="service-description"><%= service.getDescription() %></p>
	            <p class="service-description">Estimated Duration: <%= service.getEstDuration() %> Hour(s)</p>
	            <p class="service-description">Price: $<%= service.getPrice() %></p>
	            
	            <% if (person != null && person.getRoleId() == 2) { %>
	            <!-- Form for Add to Cart -->
	            <div id="btns">
	                <form action="${pageContext.request.contextPath}/CartServlet" method="post">
	                    <input type="hidden" name="action" value="add">
	                    <input type="hidden" name="bookNow" value="True">
	                    <input type="hidden" name="serviceId" value="<%= service.getId() %>">
	                    <button class="btn primary-btn" type="submit">Book Now</button>
	                </form>
	                <form action="${pageContext.request.contextPath}/CartServlet" method="post">
	                    <input type="hidden" name="action" value="add">
	                    <input type="hidden" name="bookNow" value="False">
	                    <input type="hidden" name="serviceId" value="<%= service.getId() %>">
	                    <button class="btn secondary-btn" type="submit">Add to Cart</button>
	                </form>
	                <form action="${pageContext.request.contextPath}/SavedServlet" method="post" class="save-for-later">
	                    <input type="hidden" name="action" value="add">
	                    <input type="hidden" name="person" value="<%= person %>">
	                    <input type="hidden" name="service_id" value="<%= service.getId() %>">
	                    <button class="btn tertiary-btn" type="submit">Save for Later</button>
	                </form>
	            </div>
	            <% } %>
	        </div>
	    </div>
	
	    <!-- Button to toggle reviews collapse -->
	    <p>
	        <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#collapseWidthExample" aria-expanded="false" aria-controls="collapseWidthExample">
	            Reviews
	        </button>
	    </p>
	
	    <!-- Collapsible Section for Reviews -->
	    <div>
	        <div id="reviewContainer">
	            <div class="card card-body">
	                <% 
	                    // Retrieve reviews for the current service
	                    List<Review> reviews = service.getReviews(); 
	                %>
	                <div class="review-container">
	                    <% if (reviews != null && !reviews.isEmpty()) { 
	                        for (Review review : reviews) { 
	                    %>
	                    <div class="review">
	                        <div class="review-rating">
	                            <strong>Rating:</strong>
	                            <% 
	                                int rating = review.getRating();
	                                for (int i = 0; i < rating; i++) { 
	                            %>
	                            <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
	                            <% } %>	
	                        </div>
	                        <div class="review-comment">
	                            <p>
	                                <strong>Comment:</strong> <em><%= review.getContent() %></em>
	                            </p>
	                        </div>
	                        <div class="review-date">
	                            <p>
	                                <strong>Posted on:</strong> <span class="date"><%= review.getFormattedDateCreated() %></span>
	                            </p>
	                        </div>
	                    </div>
	                    <% 
	                        } 
	                    } else { 
	                    %>
	                    <p>No reviews yet for this service.</p>
	                    <% } %>
	                </div>
	            </div>
	        </div>
	    </div>
	    <% 
	        } else {
	            out.println("<h3 class='error-message'>Service not found.</h3>");
	        }
	    %>
	</div>
</div>


    <!-- Include the JavaScript File -->
    <script src="index.js"></script>
    <%@ include file="/views/Util/components/swiper/serviceSwiper.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>
    <%@ include file="/views/Util/components/footer/footer.jsp" %>

</body>
</html>
