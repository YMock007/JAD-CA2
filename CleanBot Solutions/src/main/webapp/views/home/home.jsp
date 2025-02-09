<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.BookingCategory, model.Booking, service.BookingServiceClient" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Worker Dashboard | Available Bookings</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        .navbar {
            height: 50px;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 80px auto 100px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        }

        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .booking-card {
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        }

        .booking-card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }

        .booking-details {
            padding: 15px;
            text-align: left;
        }

        .booking-details h3 {
            margin: 0;
            font-size: 18px;
        }

        .booking-details p {
            margin: 5px 0;
            font-size: 14px;
        }

        .btn-accept {
            display: block;
            width: 100%;
            padding: 10px;
            background: #4CAF50;
            color: white;
            text-align: center;
            font-size: 16px;
            cursor: pointer;
            border: none;
            border-radius: 0 0 8px 8px;
        }

        .btn-accept:hover {
            background: #45a049;
        }

        .pagination {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .pagination button {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 8px 12px;
            margin: 0 5px;
            cursor: pointer;
            border-radius: 5px;
        }

        .pagination button:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .filter-container {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .filter-container input {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 200px;
        }

        .clear-btn {
            background: #d9534f;
            color: white;
            border: none;
            padding: 8px 15px;
            cursor: pointer;
            border-radius: 5px;
        }

        .clear-btn:hover {
            background: #c9302c;
        }
        .footer {
        margin-top: auto; /* Pushes footer to bottom */
        width: 100%;
        position: fixed; /* Remove fixed positioning */
   		bottom: 0;
    }
    
    </style>
</head>
<body>

<div class="navbar"><%@ include file="/resources/asset/nav.jsp" %></div>

<div class="container">
    <h2>Available Bookings</h2>

    <div class="filter-container">
        <input type="text" id="searchService" placeholder="Search by Service Name" onkeyup="filterBookings()">
        <input type="date" id="filterDate" onchange="filterBookings()">
        <button class="clear-btn" onclick="clearFilters()">Clear</button>
    </div>

    <div id="bookings-container" class="grid-container">
        <%
            List<BookingCategory> categories = BookingServiceClient.fetchAvailableBookings();
            HttpSession userSession = request.getSession();
            List<Integer> workerCategories = (List<Integer>) userSession.getAttribute("workerCategories");
            int count = 0;

            for (BookingCategory category : categories) {
                if (workerCategories.contains(category.getId())) { 
                    for (Booking booking : category.getBookings()) { 
                        count++;
        %>
        <div class="booking-card" data-service="<%= booking.getService().getName() %>" data-date="<%= booking.getDateRequested() %>">
            <img src="<%= booking.getService().getImageUrl() %>" alt="<%= booking.getService().getName() %>">
            <div class="booking-details">
                <h3><%= booking.getService().getName() %></h3>
                <p><strong>Address:</strong> <%= booking.getAddress() %></p>
                <p><strong>Date:</strong> <%= booking.getDateRequested() %></p>
                <p><strong>Time:</strong> <%= booking.getTimeRequested() %></p>
                <p><strong>Price:</strong> $<%= booking.getService().getPrice() %></p>
            </div>
            <button class="btn-accept">Accept Booking</button>
        </div>
        <% } } } %>
    </div>

    <div class="pagination">
        <button onclick="changePage(-1)" id="prevBtn">Previous</button>
        <span id="pageNumber">1</span> / <span id="totalPages">1</span>
        <button onclick="changePage(1)" id="nextBtn">Next</button>
    </div>
</div>

<div class="footer"><%@ include file="/resources/asset/footer.jsp" %></div>

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
            booking.style.display = (index >= start && index < end) ? "block" : "none";
        });

        document.getElementById("pageNumber").innerText = currentPage;
        document.getElementById("totalPages").innerText = totalPages;
        document.getElementById("prevBtn").disabled = (currentPage === 1);
        document.getElementById("nextBtn").disabled = (currentPage === totalPages);
    }

    function changePage(direction) {
        if ((currentPage + direction) >= 1 && (currentPage + direction) <= totalPages) {
            showPage(currentPage + direction);
        }
    }

    
    function filterBookings() {
        const searchService = document.getElementById("searchService").value.toLowerCase();
        const filterDate = document.getElementById("filterDate").value;

        bookings.forEach(booking => {
            const serviceName = booking.getAttribute("data-service").toLowerCase();
            const bookingDate = booking.getAttribute("data-date");

            let matchesService = searchService === "" || serviceName.includes(searchService);
            let matchesDate = filterDate === "" || bookingDate === filterDate;

            booking.style.display = (matchesService && matchesDate) ? "block" : "none";
        });

        totalPages = Math.ceil(filteredCount / itemsPerPage);

        totalPages = Math.ceil(filteredCount / itemsPerPage);
        showPage(1);;
    }
    



    function clearFilters() {
        document.getElementById("searchService").value = "";
        document.getElementById("filterDate").value = "";
        showPage(1);;
    }

    window.onload = () => showPage(1);
</script>

</body>
</html>
