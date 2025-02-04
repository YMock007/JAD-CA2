<%@ include file="/views/Util/auth/memberAuth.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Bookings.*, Services.*, Persons.*, Reviews.*" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --bg-primary: #474747;
            --bg-secondary: #17a2b8;
            --text-white: #ffffff;
            --text-blue: #007bff;
            --text-teal: #17a2b8;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }

        h1 {
            margin-top: 20px;
            text-align: center;
            color: var(--text-teal);
            font-size: 28px;
        }

        table {
            margin: 20px auto;
            width: 90%;
            border-collapse: collapse;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        th {
            background-color: var(--bg-secondary);
            color: var(--text-blue);
        }

        td, th {
            text-align: center;
            padding: 12px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: rgba(23, 162, 184, 0.2);
        }

        .btn {
            margin: 5px;
        }

        .form-container {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin: 20px;
        }

		.rating-stars {
		    display: flex;
		    justify-content: center;
		    gap: 10px;
		}
		
		.rating-stars input {
		    display: none;
		}
		
		.rating-stars label {
		    font-size: 40px; /* Increased size for larger stars */
		    color: gray;
		    cursor: pointer;
		    transition: color 0.3s ease;
		}
		
		/* Fill selected stars */
		.rating-stars input:checked ~ label {
		    color: gold;
		}
    </style>
</head>
<body>
    <%@ include file="/views/Util/components/header/header.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>

    <h1>My Bookings</h1>

    <%
        // Retrieve the list of bookings
        List<Booking> bookings = BookingList.getBookingsByUser(person.getId());
    %>

    <!-- Only show filter if there are bookings -->
    <% if (bookings != null && !bookings.isEmpty()) { %>
        <!-- Filter by Status -->
        <label for="statusId">Filter by Status:</label>
        <select id="statusId" class="form-select w-auto">
            <option value="">All</option>
            <option value="1" <% if ("1".equals(request.getParameter("statusId"))) { %>selected<% } %>>Pending</option>
            <option value="2" <% if ("2".equals(request.getParameter("statusId"))) { %>selected<% } %>>Completed</option>
            <option value="3" <% if ("3".equals(request.getParameter("statusId"))) { %>selected<% } %>>Cancelled</option>
        </select>
    <% } %>

    <table class="table table-bordered table-hover table-striped">
        <tbody>
		    <%
		        // Retrieve the filter status
		        String statusFilter = request.getParameter("statusId");
		
		        // If status is provided, filter the list
		        if (statusFilter != null && !statusFilter.isEmpty()) {
		            bookings = bookings.stream()
		                                .filter(booking -> booking.getStatusId() == Integer.parseInt(statusFilter))
		                                .collect(Collectors.toList());
		        }
		
		        int index = 1;
		        if (bookings != null && !bookings.isEmpty()) {
		    %>
		    <thead>
		        <tr>
		            <th>#</th>
		            <th>Cleaner</th>
		            <th>Service</th>
		            <th>Status</th>
		            <th>Date</th>
		            <th>Time</th>
		            <th>Address</th>
		            <th>Remark</th>
		            <th>Action</th>
		        </tr>
		    </thead>
		    <tbody>
		        <%
		            for (Booking booking : bookings) {
		                String bookingStatus = booking.getStatusId() == 1 ? "Pending" :
		                                       booking.getStatusId() == 2 ? "Completed" :
		                                       "Cancelled";
		                String serviceName = ServiceList.getServiceById(booking.getServiceId()).getName();
		                String cleanerName = PersonList.getPersonById(booking.getProviderId()).getName();
		        %>
		        <tr>
		            <td><%= index++ %></td>
		            <td><%= cleanerName %></td>
		            <td><%= serviceName %></td>
		            <td><%= bookingStatus %></td>
		            <td><%= booking.getDateRequested() %></td>
		            <td><%= booking.getTimeRequested() %></td>
		            <td><%= booking.getAddress() %></td>
		            <td><%= booking.getRemark() %></td>
		            <td>
		                <% 
		                    boolean reviewExists = ReviewList.isReviewPresentForBooking(booking.getId());
		                    if (booking.getStatusId() == 2 && !reviewExists) { 
		                %>
		                    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#reviewModal" data-booking-id="<%= booking.getId() %>">Review</button>
		                <% 
		                    } else if (reviewExists) { 
		                %>
		                    <button class="btn btn-secondary" disabled>Reviewed</button>
		                <% 
		                    } else { 
		                %>
		                    <button class="btn btn-secondary" disabled>Not Available</button>
		                <% } %>
		            </td>
		        </tr>
		        <%
		            }
		        %>
		    </tbody>
		    <% 
		        } else {
		    %>
		        <tr>
		            <td colspan="9">No bookings found.</td>
		        </tr>
		    <% 
		        }
		    %>
		</tbody>

    </table>

    <!-- Review Modal -->
    <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/reviewServlet" method="post">
                    <input type="hidden" name="action" value="create">
                    <div class="modal-header">
                        <h5 class="modal-title" id="reviewModalLabel">Give Review</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="bookingId" id="modalBookingId">
                        <div class="rating-stars">
                            <% for (int i = 1; i <= 5; i++) { %>
                                <input type="radio" id="star<%= i %>" name="rating" value="<%= i %>">
                                <label for="star<%= i %>">&#9733;</label>
                            <% } %>
                        </div>
                        <div class="form-group mt-3">
                            <label for="reviewContent">Review:</label>
                            <textarea name="reviewContent" id="reviewContent" class="form-control" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Submit Review</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/views/member/booking/index.js" type="text/javascript"></script>
    <!-- Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- JavaScript for filtering -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var statusDropdown = document.getElementById('statusId');
            var urlParams = new URLSearchParams(window.location.search);
            var selectedStatus = urlParams.get('statusId');

            if (selectedStatus) {
                statusDropdown.value = selectedStatus;
            }

            statusDropdown.addEventListener('change', function () {
                window.location.search = 'statusId=' + statusDropdown.value;
            });
        });
    </script>
</body>
</html>
