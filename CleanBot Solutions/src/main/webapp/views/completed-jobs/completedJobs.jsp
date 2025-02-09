<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, model.CompletedBookingDTO, service.BookingServiceClient" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Completed Jobs</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 15px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        .empty-message {
            font-size: 16px;
            color: #888;
            margin-top: 20px;
            text-align: center;
        }

        /* Pagination */
        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 20px;
        }

        .pagination button {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 8px 15px;
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
            margin: 0 10px;
            color: #333;
        }
        .navbar {
            height: 30px;
        }

        .footer {
            width: 100%;
            position: fixed;
            bottom: 0;
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
    <p class="empty-message">You are not logged in. Please log in to view your completed jobs.</p>
<%
    } else {
        List<CompletedBookingDTO> completedBookings = BookingServiceClient.fetchCompletedBookings(workerId);
        int totalBookings = completedBookings.size();
        int itemsPerPage = 5;
        int totalPages = (int) Math.ceil((double) totalBookings / itemsPerPage);

        if (totalBookings == 0) {
%>
            <p class="empty-message">No completed bookings yet.</p>
<%
        } else {
%>
<div class="container">
    <h2>My Completed Jobs</h2>
    <table id="completed-bookings-table">
        <thead>
            <tr>
                <th>Service</th>
                <th>Address</th>
                <th>Date</th>
                <th>Time</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody>
            <% int count = 0; %>
            <% for (CompletedBookingDTO booking : completedBookings) { %>
                <tr class="booking-row" data-index="<%= count %>">
                    <td><%= booking.getService().getName() %></td>
                    <td><%= booking.getAddress() %></td>
                    <td><%= booking.getDateRequested() %></td>
                    <td><%= booking.getTimeRequested() %></td>
                    <td>$<%= booking.getService().getPrice() %></td>
                </tr>
                <% count++; %>
            <% } %>
        </tbody>
    </table>

    <!-- Pagination -->
    <div class="pagination">
        <button id="prevBtn" onclick="changePage(-1)" disabled>Previous</button>
        <span id="pageIndicator">1</span> / <span><%= totalPages %></span>
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
    const itemsPerPage = 5;
    const rows = document.querySelectorAll(".booking-row");
    const totalPages = Math.ceil(rows.length / itemsPerPage);

    function showPage(page) {
        let start = (page - 1) * itemsPerPage;
        let end = start + itemsPerPage;

        rows.forEach((row, index) => {
            row.style.display = (index >= start && index < end) ? "table-row" : "none";
        });

        document.getElementById("pageIndicator").textContent = page;
        document.getElementById("prevBtn").disabled = (page === 1);
        document.getElementById("nextBtn").disabled = (page === totalPages);
    }

    function changePage(step) {
        currentPage += step;
        showPage(currentPage);
    }

    showPage(1);
</script>

</body>
</html>
