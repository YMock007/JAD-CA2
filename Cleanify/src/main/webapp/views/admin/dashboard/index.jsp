<%@ include file="/views/Util/components/header/header.jsp" %>
<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/indexs.css">
</head>
<body>

    <div class="container">
        <!-- Managing Section -->
        <section>
            <h2>Management</h2>
            <div class="admin-links">
                <a href="managing/categories-services/index.jsp" class="btn btn-primary">Categories & Services</a>
                <a href="managing/members/index.jsp" class="btn btn-info">Members</a>
            </div>
        </section>

        <!-- Reporting Section -->
        <section>
            <h2>Reporting</h2>
            <div class="admin-links">
                <a href="reporting/categories-services/index.jsp" class="btn btn-primary">Categories & Services</a>
                <a href="reporting/members/index.jsp" class="btn btn-info">Members</a>
                <a href="reporting/bookings/index.jsp" class="btn btn-warning">Bookings</a>
            </div>
        </section>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
