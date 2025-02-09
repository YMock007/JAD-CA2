<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.BookingCategory, model.Booking, service.BookingServiceClient" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Accepted Jobs</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f5f7fa;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            max-width: 1100px;
            margin: 50px auto;
            margin-bottom: 80px;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            font-size: 26px;
            margin-bottom: 25px;
        }

        /* Booking Grid */
        .booking-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        /* Booking Cards */
        .booking-card {
            background: #ffffff;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            padding: 20px;
            display: flex;
            align-items: center;
            gap: 20px;
            transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
            border-left: 6px solid #4CAF50;
        }

        .booking-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 20px rgba(0, 0, 0, 0.2);
        }

        .booking-card img {
            width: 180px;
            height: 140px;
            object-fit: cover;
            border-radius: 10px;
            border: 3px solid #eee;
        }

        .booking-details {
            flex: 1;
            text-align: left;
        }

        .booking-details h3 {
            margin: 0;
            font-size: 20px;
            font-weight: 600;
            color: #333;
        }

        .booking-details p {
            margin: 5px 0;
            font-size: 15px;
            color: #555;
            line-height: 1.4;
        }

        .highlight {
            font-weight: 600;
            color: #222;
        }

        /* Buttons */
        .btn-complete {
            padding: 12px 18px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            transition: 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .btn-complete:hover {
            background: #218838;
            transform: scale(1.05);
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 25px;
            gap: 10px;
        }

        .pagination button {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 10px 18px;
            font-size: 14px;
            cursor: pointer;
            border-radius: 5px;
            transition: 0.2s ease-in-out;
        }

        .pagination button:hover {
            background: #388E3C;
        }

        .pagination button:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .page-indicator {
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .empty-message {
            font-size: 16px;
            color: #888;
            margin-top: 20px;
        }

        .navbar {
            height: 30px;
        }

        .footer {
            width: 100%;
            position: fixed;
            bottom: 0;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .booking-grid {
                grid-template-columns: 1fr;
            }

            .booking-card {
                flex-direction: column;
                text-align: center;
                padding: 15px;
            }

            .booking-card img {
                width: 100%;
                max-width: 280px;
                height: auto;
            }

            .btn-complete {
                width: 100%;
                padding: 10px;
            }
        }
    </style>
</head>
<body>
<div class="navbar"><%@ include file="/resources/asset/nav.jsp" %></div>

<%
    HttpSession userSession = request.getSession();
    Integer workerId = (Integer) userSession.getAttribute("workerId");

    if (workerId == null) {
%>
    <p class="empty-message">You are not logged in. Please log in to view your accepted jobs.</p>
<%
    } else {
        List<BookingCategory> acceptedBookings = BookingServiceClient.fetchAcceptedBookings(workerId);
        int totalBookings = 0;
        for (BookingCategory category : acceptedBookings) {
            totalBookings += category.getBookings().size();
        }
        int itemsPerPage = 6;
        int totalPages = (int) Math.ceil((double) totalBookings / itemsPerPage);

        if (totalBookings == 0) {
%>
            <p class="empty-message">No accepted bookings yet.</p>
<%
        } else {
%>
<div class="container">
    <h2>My Accepted Jobs</h2>
    <div class="booking-grid" id="accepted-bookings-container">
        <% int count = 0; %>
        <% for (BookingCategory category : acceptedBookings) { %>
            <% for (Booking booking : category.getBookings()) { %>
                <div class="booking-card" data-index="<%= count %>">
                    <img src="<%= booking.getService().getImageUrl() %>" alt="<%= booking.getService().getName() %>">
                    <div class="booking-details">
                        <h3><%= booking.getService().getName() %></h3>
                        <p><span class="highlight">Requester:</span> <%= booking.getRequesterName() %></p>
                        <p><span class="highlight">Phone:</span> <%= booking.getRequesterPhone() %></p>
                        <p><span class="highlight">Address:</span> <%= booking.getAddress() %></p>
                        <p><span class="highlight">Date:</span> <%= booking.getDateRequested() %></p>
                        <p><span class="highlight">Time:</span> <%= booking.getTimeRequested() %></p>
                        <p><span class="highlight">Price:</span> $<%= booking.getService().getPrice() %></p>
                        
                        <form action="<%= request.getContextPath() %>/CompleteBookingServlet" method="POST">
            				<input type="hidden" name="workerId" value="<%= userSession.getAttribute("workerId") %>">
                			<input type="hidden" name="bookingId" value="<%= booking.getId() %>">
                			<input type="hidden" name="statusId" value="2">
                			<button class="btn-complete"><i class="fas fa-check"></i> Mark as Complete</button>
            		</form>
                    </div>
                </div>
                <% count++; %>
            <% } %>
        <% } %>
    </div>

    <div class="pagination">
        <button id="prevBtn" onclick="changePage(-1)" disabled>Previous</button>
        <span id="pageIndicator">1</span> / <span id="totalPages"><%= totalPages %></span>
        <button id="nextBtn" onclick="changePage(1)">Next</button>
    </div>
</div>
<div class="footer"><%@ include file="/resources/asset/footer.jsp" %></div>
<%
        }
    }
%>

<script>
    let currentPage = 1;
    const itemsPerPage = 6;
    const bookings = document.querySelectorAll('.booking-card');
    let totalPages = Math.ceil(bookings.length / itemsPerPage);

    function showPage(page) {
        currentPage = page;
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        bookings.forEach((booking, index) => {
            booking.style.display = (index >= start && index < end) ? "flex" : "none";
        });

        // Update page number display
        document.getElementById("pageIndicator").innerText = currentPage;
        document.getElementById("totalPages").innerText = totalPages;

        // Enable/Disable buttons based on current page
        document.getElementById("prevBtn").disabled = (currentPage === 1);
        document.getElementById("nextBtn").disabled = (currentPage === totalPages || totalPages === 0);
    }

    function changePage(direction) {
        if ((currentPage + direction) >= 1 && (currentPage + direction) <= totalPages) {
            showPage(currentPage + direction);
        }
    }

    window.onload = () => showPage(1);
</script>


</body>
</html>
