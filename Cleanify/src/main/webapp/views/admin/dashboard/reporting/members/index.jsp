<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="Persons.MemberReport, java.util.*, Utils.*" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%@ include file="/views/Util/components/header/header.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Members Data Analysis</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/reporting/members/index.css">
<script src="https://maps.googleapis.com/maps/api/js?key=<%= ConfigLoader.get("POSTAL_CODE_LOOK_UP_API_KEY") %>&libraries=marker"></script>
    <style>
        #map {
            height: 500px;
            width: 100%;
            margin-top: 20px;
        }
    </style>
</head>

<%
    // Ensure data is only fetched once
    boolean dataLoaded = (request.getAttribute("membersByPostal") != null);
%>

<body>
    <%
        if (!dataLoaded) { 
    %>
    <!-- Auto-fetch data on page load -->
    <form id="fetchForm" action="<%= request.getContextPath() %>/members-analysis" method="GET">
        <input type="hidden" name="getALL" value="all" />
        <script>
            if (!window.location.search.includes("getALL=all")) {
                document.getElementById('fetchForm').submit();
            }
        </script>
    </form>
    <%
        } 
    %>

    <div class="container my-5">
        <h2 class="text-primary mb-4 text-center">Members Data Analysis</h2>

        <div class="row">
            <!-- Members by Postal Code -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-info text-white text-center">
                        Members by Postal Code
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped mb-0">
                            <thead class="thead-dark">
                                <tr>
                                    <th scope="col">Postal Code</th>
                                    <th scope="col">Name</th>
                                    <th scope="col">Location</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                List<MemberReport> membersByPostal = (List<MemberReport>) request.getAttribute("membersByPostal");

                                if (membersByPostal != null && !membersByPostal.isEmpty()) {
                                    for (MemberReport member : membersByPostal) {
                                %>
                                    <tr>
                                        <td><%= member.getPostalCode() %></td>
                                        <td><%= member.getName() %></td>
                                        <td><%= member.getLocation() %></td>
                                    </tr>
                                <% } } else { %>
                                    <tr>
                                        <td colspan="3" class="text-center text-muted">No data available</td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Top 5 Most Active Members -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-success text-white text-center">
                        Top 5 Most Active Members
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped mb-0">
                            <thead class="thead-dark">
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Total Bookings</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                List<MemberReport> mostActiveMembers = (List<MemberReport>) request.getAttribute("mostActiveMembers");

                                if (mostActiveMembers != null && !mostActiveMembers.isEmpty()) {
                                    for (MemberReport member : mostActiveMembers) {
                                %>
                                    <tr>
                                        <td><%= member.getName() %></td>
                                        <td><%= member.getTotalBookings() %></td>
                                    </tr>
                                <% } } else { %>
                                    <tr>
                                        <td colspan="2" class="text-center text-muted">No data available</td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
		
		<h2>All Members Location on Google Map</h2>
        <!-- Google Map Integration -->
        <div id="map"></div>
    </div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const singapore = { lat: 1.3521, lng: 103.8198 }; // Center of Singapore
        const map = new google.maps.Map(document.getElementById("map"), {
            zoom: 11,
            center: singapore,
            mapId: "c75b7789e7bccb0c"
        });

        // Add members data
        const members = [];
        <% if (membersByPostal != null && !membersByPostal.isEmpty()) { 
            for (MemberReport member : membersByPostal) { 
                String[] latLng = PostalCodeLookup.getLatLng(member.getPostalCode()).split(",");
        %>
        members.push({
            name: "<%= member.getName()%>",
            location: "<%= member.getLocation()%>",
            lat: parseFloat("<%= latLng[0] %>"),
            lng: parseFloat("<%= latLng[1] %>"),
            postalCode: "<%= member.getPostalCode() %>"
        });
        <% } } %>

        // Add markers and info windows for each member
        members.forEach(function (member) {
            // Create a custom marker
            var markerContent = document.createElement("div");
            markerContent.style.backgroundColor = "#f00";
            markerContent.style.color = "#fff";
            markerContent.style.padding = "5px 10px";
            markerContent.style.borderRadius = "5px";
            markerContent.style.cursor = "pointer";
            markerContent.textContent = member.name;

            // Advanced marker for customization
            var advancedMarker = new google.maps.marker.AdvancedMarkerElement({
                position: { lat: member.lat, lng: member.lng },
                map: map,
                content: markerContent,
            });

            // Info window for details
            var infoWindowContent =
                '<div style="background-color: #fff; padding: 10px; border: 1px solid #ccc; border-radius: 5px; max-width: 200px;">' +
                '<p><strong>Location:</strong> ' + member.location + '</p>' +
                '<p><strong>Postal Code:</strong> ' + member.postalCode + '</p>' +
                '</div>';
            var infoWindow = new google.maps.InfoWindow({
                content: infoWindowContent,
            });

            // Display info window on marker click
            advancedMarker.addListener("click", function () {
                infoWindow.open({
                    anchor: advancedMarker,
                    map: map,
                });
            });
        });
    });
</script>


</body>
</html>
