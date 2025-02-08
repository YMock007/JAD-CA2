<%@ page import="db.InitTables, utils.ConfigLoader" %>
<html>
<head>
    <title>Initialize Tables</title>
</head>
<body>
    <%
        String basePath = ConfigLoader.get("BASE_PATH");
        if (basePath == null || basePath.isEmpty()) {
            out.println("Error: BASE_PATH not configured.");
        } else {
            String scriptPath = basePath + "/CleanBot Solutions/src/main/webapp/resources/database/initTables.sql";
            out.println("Resolved script path: " + scriptPath);
            boolean success = InitTables.executeSQLScript(scriptPath);

            if (success) {
                out.println("Tables initialized successfully!");
    %>
                <form action="seeder.jsp" method="post">
                    <input type="submit" value="Seed Database with Sample Data">
                </form>
    <%
            } else {
                out.println("Failed to initialize tables. Ensure the initTables.sql file exists at: " + scriptPath);
            }
        }
    %>
</body>
</html>
