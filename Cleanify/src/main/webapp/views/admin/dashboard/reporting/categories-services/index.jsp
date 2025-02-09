<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="Services.ServiceReport, java.util.List,java.util.*, java.time.*, Reporting.DailyRevenueService"%>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%@ include file="/views/Util/components/header/header.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Service Data Analysis</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/reporting/categories-services/index.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<%
    // Ensure data is only fetched once
    boolean dataLoaded = (request.getAttribute("bestRatedServices") != null);
%>

<body>

	<%
	    Integer selectedYear = (Integer) request.getAttribute("selectedYear");
	    Integer selectedMonth = (Integer) request.getAttribute("selectedMonth");
	
	    if (selectedYear == null) {
	        selectedYear = java.time.LocalDate.now().getYear(); // Set default year
	    }
	    if (selectedMonth == null) {
	        selectedMonth = java.time.LocalDate.now().getMonthValue(); // Set default month
	    }
	    
	 // Retrieve daily revenue trends
	    List<DailyRevenueService> dailyRevenueTrends = (List<DailyRevenueService>) request.getAttribute("dailyRevenueTrends");
	    if (dailyRevenueTrends == null) {
	        dailyRevenueTrends = new ArrayList<>();
	    }
	    
	 // Prepare data for Chart.js
	    ObjectMapper objectMapper = new ObjectMapper();
	    String dailyLabelsJson = objectMapper.writeValueAsString(
	        dailyRevenueTrends.stream().map(DailyRevenueService::getDay).toArray()
	    );
	    String dailyDataJson = objectMapper.writeValueAsString(
	        dailyRevenueTrends.stream().map(DailyRevenueService::getRevenue).toArray()
	    );

	    // Calculate total revenue
	    double totalRevenue = dailyRevenueTrends.stream().mapToDouble(DailyRevenueService::getRevenue).sum();
	    
		if (!dataLoaded) { %>
	    <!-- Auto-fetch data on page load, but only if it hasn't been loaded yet -->
	    <form id="fetchForm" action="<%= request.getContextPath() %>/services-analysis" method="GET">
	        <input type="hidden" name="getALL" value="all" />
	        <input type="hidden" name="year" value="<%= selectedYear %>" />
	        <input type="hidden" name="month" value="<%= selectedMonth %>" />
	        <script>
	            if (!window.location.search.includes("getALL=all")) {
	                document.getElementById('fetchForm').submit();
	            }
	        </script>
	    </form>
	<% } %>


    <div class="container my-5">
        <h2 class="text-primary mb-4 text-center">Service Data Analysis</h2>

        <%
            // Retrieve attributes safely
            List<ServiceReport> bestRatedServices = (List<ServiceReport>) request.getAttribute("bestRatedServices");
            List<ServiceReport> lowestRatedServices = (List<ServiceReport>) request.getAttribute("lowestRatedServices");

            bestRatedServices = (bestRatedServices != null) ? bestRatedServices : new java.util.ArrayList<>();
            lowestRatedServices = (lowestRatedServices != null) ? lowestRatedServices : new java.util.ArrayList<>();
        %>

        <!-- Best & Lowest Rated Services -->
        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-success text-white">Best Rated Services</div>
                    <div class="card-body">
                        <ul class="list-group">
                            <% if (!bestRatedServices.isEmpty()) { %>
                                <% for (ServiceReport service : bestRatedServices) { %>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <%= service.getName() %>
                                        <span class="badge badge-success badge-pill">
                                            <%= service.getAvgRating() %> ⭐
                                        </span>
                                    </li>
                                <% } %>
                            <% } else { %>
                                <li class="list-group-item text-muted">No data available</li>
                            <% } %>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-danger text-white">Lowest Rated Services</div>
                    <div class="card-body">
                        <ul class="list-group">
                            <% if (!lowestRatedServices.isEmpty()) { %>
                                <% for (ServiceReport service : lowestRatedServices) { %>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <%= service.getName() %>
                                        <span class="badge badge-danger badge-pill">
                                            <%= service.getAvgRating() %> ⭐
                                        </span>
                                    </li>
                                <% } %>
                            <% } else { %>
                                <li class="list-group-item text-muted">No data available</li>
                            <% } %>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

		<!-- Charts -->
		<%
		
		    // Retrieve attributes
		    List<String> highDemandServices = (List<String>) request.getAttribute("highDemandServiceNames");
		    List<Integer> highDemandBookings = (List<Integer>) request.getAttribute("highDemandBookings");
		    List<String> revenueCategories = (List<String>) request.getAttribute("categoryNames");
		    List<Double> revenueAmounts = (List<Double>) request.getAttribute("categoryRevenues");
		    List<String> mostBookedServices = (List<String>) request.getAttribute("mostBookedServiceNames");
		    List<Integer> mostBookedBookings = (List<Integer>) request.getAttribute("mostBookedBookings");
		
		    // Convert Java Lists to JSON using Jackson
		    String highDemandServicesJson = objectMapper.writeValueAsString(highDemandServices);
		    String highDemandBookingsJson = objectMapper.writeValueAsString(highDemandBookings);
		    String revenueCategoriesJson = objectMapper.writeValueAsString(revenueCategories);
		    String revenueAmountsJson = objectMapper.writeValueAsString(revenueAmounts);
		    String mostBookedServicesJson = objectMapper.writeValueAsString(mostBookedServices);
		    String mostBookedBookingsJson = objectMapper.writeValueAsString(mostBookedBookings);

		%>

				
        <div class="row mt-4">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">Cleaning Services with High Demand</div>
                    <div class="card-body">
                        <canvas id="highDemandChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-4">
        	<div class="col-md-6">
                <div class="card">
                    <div class="card-header">Top 5 Most Booked Services</div>
                    <div class="card-body">
                        <canvas id="mostBookedChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">Revenue Breakdown by Category</div>
                    <div class="card-body">
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>

        </div>
        <div class="row mt-4">
		    <div class="col-md-12">
		        <div class="card">
		            <div class="card-header">
		                <div class="d-flex justify-content-between align-items-center">
		                    <span>Daily Revenue Trends</span>
		                    <!-- Year and Month Selection Form -->
		                    <form action="<%= request.getContextPath() %>/services-analysis" method="GET" class="form-inline">
		                        <input type="hidden" name="getALL" value="all" />
		                        <label for="year" class="mr-2">Year:</label>
		                        <select id="year" name="year" class="form-control form-control-sm mr-3">
		                            <% for (int year = selectedYear - 5; year <= selectedYear + 5; year++) { %>
		                                <option value="<%= year %>" <%= year == selectedYear ? "selected" : "" %>><%= year %></option>
		                            <% } %>
		                        </select>
		                        <label for="month" class="mr-2">Month:</label>
		                        <select id="month" name="month" class="form-control form-control-sm mr-3">
		                            <% for (int month = 1; month <= 12; month++) { %>
		                                <option value="<%= month %>" <%= month == selectedMonth ? "selected" : "" %>><%= month %></option>
		                            <% } %>
		                        </select>
		                        <button type="submit" class="btn btn-primary btn-sm">Filter</button>
		                    </form>
		                </div>
		            </div>
		            <div class="card-body">
		                <h5>Total Revenue for <%= selectedMonth %>/<%= selectedYear %>: SGD <%= String.format("%.2f", totalRevenue) %></h5>
		                <canvas id="dailyRevenueChart"></canvas>
		            </div>
		        </div>
		    </div>
		</div>

	
	<script>
	    // Retrieve data from JSP-generated JSON
	    const highDemandServices = <%= highDemandServicesJson %>;
	    const highDemandBookings = <%= highDemandBookingsJson %>;

	    const mostBookedServices = <%= mostBookedServicesJson %>;
	    const mostBookedBookings = <%= mostBookedBookingsJson %>;
	    
	    const revenueCategories = <%= revenueCategoriesJson %>;
	    const revenueAmounts = <%= revenueAmountsJson %>;
		    
	    
	    // Chart: High Demand Services
	    const highDemandCtx = document.getElementById('highDemandChart').getContext('2d');
	    new Chart(highDemandCtx, {
	        type: 'bar',
	        data: {
	            labels: highDemandServices,
	            datasets: [{
	                label: 'Number of Bookings',
	                data: highDemandBookings,
	                backgroundColor: 'rgba(75, 192, 192, 0.6)',
	                borderColor: 'rgba(75, 192, 192, 1)',
	                borderWidth: 1
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                legend: { display: true },
	                title: { display: true, text: 'High Demand Services' }
	            }
	        }
	    });
	
	    // Chart: Most Booked Services
	    const mostBookedCtx = document.getElementById('mostBookedChart').getContext('2d');
	    new Chart(mostBookedCtx, {
	        type: 'doughnut',
	        data: {
	            labels: mostBookedServices,
	            datasets: [{
	                label: 'Bookings',
	                data: mostBookedBookings,
	                backgroundColor: [
	                    'rgba(54, 162, 235, 0.6)',
	                    'rgba(255, 206, 86, 0.6)',
	                    'rgba(75, 192, 192, 0.6)',
	                    'rgba(153, 102, 255, 0.6)',
	                    'rgba(255, 159, 64, 0.6)'
	                ],
	                borderColor: [
	                    'rgba(54, 162, 235, 1)',
	                    'rgba(255, 206, 86, 1)',
	                    'rgba(75, 192, 192, 1)',
	                    'rgba(153, 102, 255, 1)',
	                    'rgba(255, 159, 64, 1)'
	                ],
	                borderWidth: 1
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                legend: { display: true },
	                title: { display: true, text: 'Top 5 Most Booked Services' }
	            }
	        }
	    });

	
	    // Chart: Revenue Breakdown
	    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
	    new Chart(revenueCtx, {
	        type: 'pie',
	        data: {
	            labels: revenueCategories,
	            datasets: [{
	                label: 'Revenue',
	                data: revenueAmounts,
	                backgroundColor: [
	                    'rgba(255, 99, 132, 0.6)',
	                    'rgba(54, 162, 235, 0.6)',
	                    'rgba(255, 206, 86, 0.6)',
	                    'rgba(75, 192, 192, 0.6)',
	                    'rgba(153, 102, 255, 0.6)'
	                ],
	                borderColor: [
	                    'rgba(255, 99, 132, 1)',
	                    'rgba(54, 162, 235, 1)',
	                    'rgba(255, 206, 86, 1)',
	                    'rgba(75, 192, 192, 1)',
	                    'rgba(153, 102, 255, 1)'
	                ],
	                borderWidth: 1
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                legend: { display: true },
	                title: { display: true, text: 'Revenue Breakdown by Category' }
	            }
	        }
	    });
	
	    // JavaScript Data
	    const dailyLabels = <%= dailyLabelsJson %>;
	    const dailyData = <%= dailyDataJson %>;
	    const selectedYear = <%= selectedYear %>;
	    const selectedMonth = <%= selectedMonth %>;

	    // Daily Revenue Trends Chart
	    new Chart(document.getElementById('dailyRevenueChart'), {
	        type: 'line',
	        data: {
	            labels: dailyLabels,
	            datasets: [{
	                label: 'Daily Revenue (SGD)',
	                data: dailyData,
	                borderColor: 'rgba(75, 192, 192, 1)',
	                backgroundColor: 'rgba(75, 192, 192, 0.2)',
	                fill: true,
	                tension: 0.3
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                legend: { display: true },
	                title: { display: true, text: `Daily Revenue Trends for ${selectedMonth}-${selectedYear}` }
	            },
	            scales: {
	                x: { title: { display: true, text: 'Day' } },
	                y: { title: { display: true, text: 'Revenue (SGD)' } }
	            }
	        }
	    });

	</script>
		
</body>
</html>
