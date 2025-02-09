<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <div class="navbar">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/dashboard.jsp">🚀 Worker Dashboard</a>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/views/home/home.jsp">📅 Bookings</a></li>
                <li><a href="${pageContext.request.contextPath}/views/accepted-jobs/acceptedJobs.jsp">✅ Accepted</a></li>
                <li><a href="${pageContext.request.contextPath}/views/completed-jobs/completedJobs.jsp">🏆 Completed</a></li>
                </li>
            </ul>
        </nav>
    </div>
</header>

<style>
    /* Header Styles */
    header {
        background: #111827;
        color: white;
        padding: 15px 0;
        width: 100%;
        position: fixed;
        top: 0;
        left: 0;
        z-index: 1000;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
    }

    .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .logo a {
        color: #facc15;
        font-size: 24px;
        font-weight: bold;
        text-decoration: none;
    }

    nav ul {
        display: flex;
        list-style: none;
        margin: 0;
        padding: 0;
    }

    nav ul li {
        margin-left: 20px;
        position: relative;
    }

    nav ul li a {
        color: white;
        text-decoration: none;
        font-size: 16px;
        padding: 10px 15px;
        transition: 0.3s;
        border-radius: 5px;
    }

    nav ul li a:hover {
        background: #facc15;
        color: black;
    }

    /* Dropdown Styles */
    .dropdown .dropbtn {
        cursor: pointer;
    }

    .dropdown-content {
        display: none;
        position: absolute;
        background: #1f2937;
        min-width: 160px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
        border-radius: 5px;
        top: 40px;
    }

    .dropdown-content a {
        color: white;
        padding: 10px 15px;
        display: block;
        text-decoration: none;
        font-size: 14px;
    }

    .dropdown-content a:hover {
        background: #facc15;
        color: black;
    }

    .dropdown:hover .dropdown-content {
        display: block;
    }
</style>
