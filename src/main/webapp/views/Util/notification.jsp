<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String status = (String) session.getAttribute("status");
    String message = (String) session.getAttribute("message");

    if (status != null && message != null) {
%>
    <div class="notification <%= status %>" style="display: block;">
        <%= message %>
    </div>
    <script>
        setTimeout(function() {
            document.querySelector(".notification").style.display = 'none';
        }, 3000);
    </script>

    <style>
        .notification {
            position: fixed;
            bottom: 20px;
            right: 20px;
            padding: 15px;
            border-radius: 5px;
            color: white;
            z-index: 9999;
        }

        .notification.success {
            background-color: #28a745;
        }

        .notification.error {
            background-color: #dc3545;
        }
        
        .notification.info {
        	background-color: #0080ff;
        }
    </style>
<%
        session.removeAttribute("status");
        session.removeAttribute("message");
    }
%>
