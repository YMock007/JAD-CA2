<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin: 50px;
        }
        h1 {
            color: red;
        }
    </style>
</head>
<body>
    <h1>An Error Occurred</h1>
    <p>Something went wrong. Please try again later.</p>
    <a href="${pageContext.request.contextPath}/views/member/home.jsp">Go Back to Home</a>
</body>
</html>
