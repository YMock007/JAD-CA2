<%@ page import="db.Seeder, Utils.ConfigLoader" %>
<html>
<head>
    <title>Seed Database</title>
</head>
<body>
    <%
        // Get the base path from configuration
        String basePath = ConfigLoader.get("BASE_PATH");
        if (basePath == null || basePath.isEmpty()) {
            out.println("Error: BASE_PATH not configured.");
        } else {
            // Construct the full path for the seed.sql script
            String scriptPath = basePath + "src/main/webapp/resources/database/seed.sql";
            boolean success = Seeder.executeSQLScript(scriptPath);

            // Display the outcome of the seeding process
            if (success) {
                out.println("Data seeded successfully!");
    %>
    		<form action="procedure.jsp" method="post">
                 <input type="submit" value="Proceed to Load Stored Procedure">
            </form>
    <%
            } else {
                out.println("Failed to seed data. Ensure the seed.sql file exists at: " + scriptPath);
            }
        }
    %>
</body>
</html>
