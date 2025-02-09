<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Worker Signup | CleanBot Solutions</title>
    <style>
        /* General Styling */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            background: linear-gradient(135deg, #0f172a, #1e293b);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        /* Form Container */
        .form-container {
            background: rgba(255, 255, 255, 0.1);
            padding: 40px;
            border-radius: 12px;
            backdrop-filter: blur(10px);
            box-shadow: 0px 8px 24px rgba(0, 0, 0, 0.2);
            width: 400px;
            text-align: center;
        }

        h2 {
            margin-bottom: 20px;
            font-size: 24px;
        }

        label {
            display: block;
            text-align: left;
            font-size: 14px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        input[type="text"], 
        input[type="email"], 
        input[type="password"] {
            width: 100%;
            padding: 12px;
            background: #e2e8f0;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            color: black;
            margin-bottom: 15px;
        }

        /* Checkbox Group */
        .category-label {
            font-size: 14px;
            font-weight: bold;
            margin-top: 10px;
            text-align: left;
        }

        .checkbox-group {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
            margin-bottom: 20px;
        }

        .checkbox-group label {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 16px;
            font-weight: normal;
        }

        .checkbox-group input[type="checkbox"] {
            width: 16px;
            height: 16px;
            accent-color: #facc15;
        }

        /* Buttons */
        button {
            background: #facc15;
            color: black;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            transition: 0.3s;
            font-weight: bold;
            margin-top: 10px;
        }

        button:hover {
            background: #fcd34d;
        }

        /* Links */
        p {
            margin-top: 15px;
        }

        p a {
            color: #facc15;
            text-decoration: none;
            font-weight: bold;
        }

        p a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>Signup</h2>
        
	    <%-- Show error message if exists --%>
	    <% String errorMessage = (String) request.getAttribute("error"); %>
	    <% if (errorMessage != null) { %>
	        <div class="error"><%= errorMessage %></div>
	    <% } %>
	        
        <form action="<%= request.getContextPath() %>/SignUpServlet" method="POST" onsubmit="return validateForm()">
            
           <label for="name">Name:</label>
        	<input type="text" name="name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>" required><br>

        	<label for="email">Email:</label>
        	<input type="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required><br>

        	<label for="phone">Phone:</label>
        	<input type="text" name="phone" value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>" required><br>
        	
        	 <label for="password">Password:</label>
            <input type="password" name="password" required>

            <label class="category-label">Select Job Categories (At least one required)</label>
            <div class="checkbox-group multiple required" >
                <label><input type="checkbox" name="categories" value="1"> Cleaning</label>
                <label><input type="checkbox" name="categories" value="2"> Repair</label>
                <label><input type="checkbox" name="categories" value="3"> Maintenance</label>
                <label><input type="checkbox" name="categories" value="4"> Pest Control</label>
                <label><input type="checkbox" name="categories" value="5"> Installation</label>
            </div>
            
            <button type="submit">Sign Up</button>
        </form>
        <p>Already have an account? <a href="${pageContext.request.contextPath}/views/registration/logIn.jsp">Login</a></p>
    </div>

    <script>
        function validateForm() {
            var checkboxes = document.querySelectorAll('input[name="categories"]:checked');
            if (checkboxes.length < 1) {
                alert("Please select at least one job category.");
                return false;
            }
            return true;
        }
    </script>

</body>
</html>
