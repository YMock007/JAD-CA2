<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper, java.util.List" %>
<%@ page import="Bookings.BookingReport, java.time.LocalDate" %>
<%@ include file="/views/Util/components/header/header.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking Analysis</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/reporting/bookings/index.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <%
        // Default year and month for navigation
        Integer selectedYear = (Integer) request.getAttribute("selectedYear");
        Integer selectedMonth = (Integer) request.getAttribute("selectedMonth");

        if (selectedYear == null) selectedYear = LocalDate.now().getYear();
        if (selectedMonth == null) selectedMonth = LocalDate.now().getMonthValue();

        // Safely retrieve data lists
        List<BookingReport> bookingsList = (List<BookingReport>) request.getAttribute("bookingsByDate");
        List<BookingReport> topCustomersList = (List<BookingReport>) request.getAttribute("topCustomers");
        List<BookingReport> serviceBookingsList = (List<BookingReport>) request.getAttribute("serviceBookings");

        int totalBookings = 0;
        double totalBookingValue = 0.0;
        int totalServiceBookings = 0;

        // Calculate totals if the lists are not null
        if (bookingsList != null && !bookingsList.isEmpty()) {
            totalBookings = bookingsList.stream()
                                        .mapToInt(BookingReport::getCount)
                                        .sum();
        }

        if (topCustomersList != null && !topCustomersList.isEmpty()) {
            totalBookingValue = topCustomersList.stream()
                                                .mapToDouble(BookingReport::getValue)
                                                .sum();
        }

        if (serviceBookingsList != null && !serviceBookingsList.isEmpty()) {
            totalServiceBookings = serviceBookingsList.stream()
                                                      .mapToInt(BookingReport::getCount)
                                                      .sum();
        }

        // Convert Java objects to JSON using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        String bookingsByDateJson = objectMapper.writeValueAsString(bookingsList);
        String topCustomersJson = objectMapper.writeValueAsString(topCustomersList);
        String serviceBookingsJson = objectMapper.writeValueAsString(serviceBookingsList);

        // Debugging logs
        System.out.println("Bookings By Date JSON: " + bookingsByDateJson);
        System.out.println("Top Customers JSON: " + topCustomersJson);
        System.out.println("Service Bookings JSON: " + serviceBookingsJson);

        // Auto-fetch data if not already fetched
        boolean dataLoaded = bookingsList != null;
        if (!dataLoaded) { 
    %>
    <form id="fetchForm" action="<%= request.getContextPath() %>/bookings-analysis" method="GET">
        <input type="hidden" name="fetchType" value="all" />
        <input type="hidden" name="year" value="<%= selectedYear %>" />
        <input type="hidden" name="month" value="<%= selectedMonth %>" />
        <script>
            document.getElementById('fetchForm').submit();
        </script>
    </form>
    <% } %>

    <div class="container my-5">
        <h2 class="text-primary text-center">Booking Data Analysis</h2>

        <!-- Bookings by Date -->
		<div class="card my-4">
		    <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
		        <span>Bookings by Date</span>
		        <!-- Filter Form -->
		        <form action="<%= request.getContextPath() %>/bookings-analysis" method="GET" class="form-inline">
		            <input type="hidden" name="fetchType" value="all" />
		            <label for="year" class="mr-2 text-white">Year:</label>
		            <select id="year" name="year" class="form-control form-control-sm mr-2">
		                <% for (int year = selectedYear - 5; year <= selectedYear + 5; year++) { %>
		                    <option value="<%= year %>" <%= year == selectedYear ? "selected" : "" %>><%= year %></option>
		                <% } %>
		            </select>
		            <label for="month" class="mr-2 text-white">Month:</label>
		            <select id="month" name="month" class="form-control form-control-sm mr-2">
		                <% for (int month = 1; month <= 12; month++) { %>
		                    <option value="<%= month %>" <%= month == selectedMonth ? "selected" : "" %>><%= month %></option>
		                <% } %>
		            </select>
		            <button type="submit" class="btn btn-light btn-sm">Filter</button>
		        </form>
		    </div>
		    <div class="card-body">
		        <% if (dataLoaded && !bookingsList.isEmpty()) { %>
		        <canvas id="bookingsByDate"></canvas>
		        <% } else { %>
		        <div class="text-center text-muted">
		            <h5>No Bookings Available for Selected Period</h5>
		        </div>
		        <% } %>
		    </div>
		</div>

        <% if (topCustomersList != null && !topCustomersList.isEmpty()) { %>
        <!-- Top 10 Customers by Booking Value -->
        <div class="card my-4">
            <div class="card-header bg-success text-white">Top 10 Customers by Booking Value</div>
            <div class="card-body">
                <canvas id="topCustomers"></canvas>
            </div>
        </div>
        <% } %>

        <% if (serviceBookingsList != null && !serviceBookingsList.isEmpty()) { %>
        <!-- Customers Booking Certain Services -->
        <div class="card my-4">
            <div class="card-header bg-warning text-dark">Customers Booking Certain Services</div>
            <div class="card-body">
                <canvas id="serviceBookings"></canvas>
            </div>
        </div>
        <% } %>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const bookingsByDate = <%= bookingsByDateJson %>;
            const topCustomers = <%= topCustomersJson %>;
            const serviceBookings = <%= serviceBookingsJson %>;
            const totalBookings = <%= totalBookings %>;
            const totalBookingValue = <%= totalBookingValue %>;
            const totalServiceBookings = <%= totalServiceBookings %>;

            <% if (dataLoaded && !bookingsList.isEmpty()) { %>
            // Bookings by Date Chart
            new Chart(document.getElementById('bookingsByDate'), {
                type: 'bar',
                data: {
                    labels: bookingsByDate.map(item => item.label),
                    datasets: [{
                        label: 'Total Bookings (' + totalBookings + ')',
                        data: bookingsByDate.map(item => item.count),
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: {
                            display: true,
                            text: 'Bookings by Date'
                        }
                    }
                }
            });
            <% } %>

            // Top Customers Chart
            new Chart(document.getElementById('topCustomers'), {
                type: 'bar',
                data: {
                    labels: topCustomers.map(item => item.label),
                    datasets: [{
                        label: 'Total Value: $' + totalBookingValue.toFixed(2),
                        data: topCustomers.map(item => item.value),
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: {
                            display: true,
                            text: 'Top 10 Customers by Booking Value'
                        }
                    }
                }
            });

            // Service Bookings Chart (Doughnut)
            new Chart(document.getElementById('serviceBookings'), {
                type: 'doughnut',
                data: {
                    labels: serviceBookings.map(item => item.label),
                    datasets: [{
                        data: serviceBookings.map(item => item.count),
                        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: {
                            display: true,
                            text: 'Total Services Booked (' + totalServiceBookings + ')'
                        }
                    }
                }
            });
        });
    </script>
</body>
</html>
