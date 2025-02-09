<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, Inquiry.Inquiry" %>
<%@ include file="/views/Util/components/header/header.jsp" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin - Manage Inquiries</title>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
</head>
<body>
    <h2>Inquiry Management</h2>

    <table border="1">
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Title</th>
            <th>Description</th>
            <th>Action</th>
        </tr>
        <% 
            List<Inquiry> inquiries = (List<Inquiry>) request.getAttribute("inquiries");
            if (inquiries != null) {
                for (Inquiry inquiry : inquiries) {
        %>
        <tr>
            <td><%= inquiry.getName() %></td>
            <td><%= inquiry.getEmail() %></td>
            <td><%= inquiry.getTitle() %></td>
            <td><%= inquiry.getDescription() %></td>
            <td>
                <form action="Inquiry" method="post">
                    <input type="hidden" name="id" value="<%= inquiry.getId() %>">
                    <input type="hidden" name="action" value="complete">
                    <button type="submit">Complete</button>
                </form>
            </td>
        </tr>
        <% 
                }
            }
        %>
    </table>
</body>
</html>
