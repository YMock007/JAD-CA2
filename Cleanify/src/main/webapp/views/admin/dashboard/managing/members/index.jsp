<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="Persons.*"%>
<%
request.setAttribute("includedFromParent", true);
%>
<%@ include file="/views/Util/components/header/header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Manage Categories & Services</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/managing/members/index.css">
</head>

<body>
    <div class="container my-5">
        <h2 class="text-primary mb-4">Manage People</h2>
        
        <!-- Add Person Button -->
        <button type="button" class="btn btn-info mb-3" data-toggle="modal" data-target="#addPersonModal">
            <i class="fas fa-user-plus"></i> Add New Person
        </button>
        
        <!-- Table -->
		<table class="table table-bordered">
		    <thead class="thead-light">
		        <tr>
		            <th>ID</th>
		            <th>Name</th>
		            <th>Email</th>
		            <th>Phone</th>
		            <th>Address</th>
		            <th>Role</th>
		            <th>Actions</th>
		        </tr>
		    </thead>
		    <tbody>
		        <% 
		        // Loop through the list of people and display their information
		        for (Person member : PersonList.getPeople()) { 
		        %>
		            <tr>
						<td><%= member.getId() %></td>
						<td><%= member.getName() %></td>
						<td><%= member.getEmail() %></td>
						<td><%= (member.getPhNumber() == null || member.getPhNumber().isEmpty()) ? "-" : member.getPhNumber() %></td>
						<td><%= (member.getAddress() == null || member.getAddress().isEmpty()) ? "-" : member.getAddress() %></td>
						<td><%= member.getRoleName() %></td>
		                <td>
		                    <button class="btn btn-secondary btn-sm" data-toggle="modal" data-target="#updateMemberModal<%= member.getId() %>">
							    <i class="fas fa-edit"></i>
							</button>
		                    <button class="btn btn-danger btn-sm" data-toggle="modal" data-target="#deleteMemberModal<%= member.getId() %>">
		                        <i class="fas fa-trash"></i>
		                    </button>
		                </td>
		            </tr>
		        <% 
		        } 
		        %>
		    </tbody>
		</table>
    </div>
	
	<!-- Include Modals -->
	<%@ include file="/views/admin/dashboard/managing/members/modals.jsp" %>
	<%@ include file="/views/admin/dashboard/managing/members/dynamicModals.jsp" %>

	<!-- Notification (Optional) -->
	<%@ include file="/views/Util/notification.jsp" %>
	
	<!-- Custom JS for the Page -->
	<script src="<%= request.getContextPath() %>/views/admin/dashboard/managing/members/index.js" type="text/javascript"></script>
	
	<!-- jQuery and Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
