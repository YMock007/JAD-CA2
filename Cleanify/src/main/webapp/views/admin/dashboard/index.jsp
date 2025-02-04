<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/managing/index.css">
</head>
<body>
<%@ include file="/views/Util/components/header/header.jsp" %>

    <div class="container my-5">        
        <!-- Managing Section -->
        <div class="mb-5">
            <h2 class="text-secondary mb-3">Management</h2>
            <div class="admin-links row">
                <a href="categories-services/index.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Categories & Services</a>
                <a href="members/index.jsp" class="col-md-3 mb-3 btn btn-info btn-lg">Customers</a>
                <a href="manageBookings.jsp" class="col-md-3 mb-3 btn btn-warning btn-lg">Bookings</a>
                <a href="manageApplicants.jsp" class="col-md-3 mb-3 btn btn-success btn-lg">Workers</a>
            </div>
        </div>

        <!-- Reporting Section -->
        <div>
            <h2 class="text-secondary mb-3">Reporting</h2>
            <div class="admin-links row">
                <a href="reportCategoriesServices.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Categories & Services</a>
                <a href="reportCustomers.jsp" class="col-md-3 mb-3 btn btn-info btn-lg">Customers</a>
                <a href="reportBookings.jsp" class="col-md-3 mb-3 btn btn-warning btn-lg">Bookings</a>
                <a href="reportWorkers.jsp" class="col-md-3 mb-3 btn btn-success btn-lg">Workers</a>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
