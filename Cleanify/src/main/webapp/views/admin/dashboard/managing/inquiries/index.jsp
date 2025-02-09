<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, Inquiry.Inquiry" %>
<%@ include file="/views/Util/components/header/header.jsp" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Manage Inquiries</title>
    
    <!-- FontAwesome Icons -->
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }

        section .container {
            max-width: 1280px;
            margin: 20px auto;
            background: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
            color: #333;
        }

        table {
            width: 100%;
        }

        .table th {
            background-color: #007bff;
            color: white;
            text-align: center;
        }

        .btn-success {
            font-size: 14px;
            padding: 6px 12px;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <section>
    <div class="container">
        <h2>Inquiry Management</h2>

        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    // Retrieve the inquiries list from request scope
                    List<Inquiry> inquiries = (List<Inquiry>) request.getAttribute("inquiries");

                    if (inquiries != null && !inquiries.isEmpty()) {
                        for (Inquiry inquiry : inquiries) {
                %>
                <tr>
                    <td><%= inquiry.getName() %></td>
                    <td><%= inquiry.getEmail() %></td>
                    <td><%= inquiry.getTitle() %></td>
                    <td><%= inquiry.getDescription() %></td>
                    <td class="text-center">
                        <form action="<%= request.getContextPath() %>/InquiryAdminServlet" method="post">
                            <input type="hidden" name="id" value="<%= inquiry.getId() %>">
                            <input type="hidden" name="action" value="complete">
                            <button type="submit" class="btn btn-success">Update Complete</button>

                        </form>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="5" class="text-center text-muted">No inquiries found.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div></section>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
