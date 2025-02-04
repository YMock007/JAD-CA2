<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Persons.Person" %>
<%
    // Check if authentication has already been performed to avoid redundant processing
    if (request.getAttribute("authChecked") == null) {
        request.setAttribute("authChecked", true);

        // Retrieve the current user from the request or session
        Person currentUser = (Person) request.getAttribute("person");
        if (currentUser == null) {
            currentUser = (Person) session.getAttribute("person");
            if (currentUser != null) {
                request.setAttribute("person", currentUser);
            }
        }

        // Ensure currentUser is not null before accessing its role
        if (currentUser == null) {
        	response.sendRedirect(request.getContextPath() + "/views/member/home.jsp");
            return;
        }

        int memberAuthRole = currentUser.getRoleId();
        // Redirect to login if the user doesn't have admin/member privileges
        if (memberAuthRole != 2 && memberAuthRole != 1) {
            response.sendRedirect(request.getContextPath() + "/views/member/home.jsp");
            return;
        }
    }
%>
