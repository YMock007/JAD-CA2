<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Persons.Person" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        /* Custom Colors */
        :root {
            --bg-primary: #007bff;
            --bg-secondary: #17a2b8;
            --text-white: #ffffff;
            --text-blue: #007bff;
            --text-teal: #17a2b8;
        }

        /* Apply a global font size */
        * {
            font-size: 20px;
            padding: 0;
            margin: 0;
            font-weight : 500;
        }

        /* Sticky Header Styles - Scoped to 'header' */
        header {
            gap: 1.4rem;
            background-color: var(--bg-secondary);
            position: sticky;
            top: 0;
            z-index: 1000;
            width: 100%;
            
            /* ✅ Add Blur Effect at Bottom */
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15); /* Soft shadow */
        }

        /* ✅ Add Gradient Blur Effect */
        .header-blur {
            position: absolute;
            bottom: -10px;
            left: 0;
            width: 100%;
            height: 15px;
            background: linear-gradient(to bottom, rgba(23, 162, 184, 0.6), rgba(255, 255, 255, 0));
            filter: blur(5px);
            pointer-events: none;
        }

        /* Scoped Logo Style */
        header #logo {
            color: var(--text-white);
            font-size: 3rem;
            cursor: pointer;
        }

        /* Scoped Navbar Styles */
        header .navbar-nav {
            gap: 1.4rem;
        }

        header .nav-link {
            color: var(--text-white);
            text-decoration: none;
            transition: color 0.3s ease;
        }

        header .nav-link:hover {
            color: var(--bg-primary);
        }
    </style>
</head>
<body>
    <%
        Person person = (Person) request.getAttribute("person");
        if (person == null) {
            person = (Person) session.getAttribute("person");
            request.setAttribute("person", person);
        }
    %>

    <header>
        <div class="container d-flex justify-content-between align-items-center p-2">
            <!-- Logo -->
            <div id="logo">
                Cleanify
            </div>
            
            <!-- Navbar -->
            <nav> 
                <ul class="navbar-nav d-flex align-items-center flex-row">
                    <% if (person != null) { %>
                        <% if (person.getRoleId() == 1) { %> 
                            <!-- Admin Navigation -->
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/admin/dashboard/index.jsp">Admin Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/profile/profile.jsp">User Profile</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/LogoutServlet">Log out</a>
                            </li>
                        <% } else if (person.getRoleId() == 2) { %> 
                            <!-- Member Navigation -->
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/member/home.jsp">Home</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/member/services/index.jsp">Services</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/member/saved/index.jsp">Saved</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/member/cart/index.jsp">Cart</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/member/booking/index.jsp">Bookings</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/views/profile/profile.jsp">User Profile</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/LogoutServlet">Log out</a>
                            </li>
                        <% } %>
                    <% } else { %>
                        <!-- Guest Navigation -->
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/views/member/home.jsp">Home</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/views/member/services/index.jsp">Services</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/views/registration/signIn.jsp">Sign Up/Login</a>
                        </li>
                    <% } %>
                </ul>
            </nav>
        </div>

        <!-- ✅ Blur Effect Layer -->
        <div class="header-blur"></div>

    </header>
</body>
</html>
