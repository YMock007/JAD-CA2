<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
     <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/index.css">
</head>
<body>
<%@ include file="/views/Util/components/header/header.jsp" %>

    <div class="container my-5">
        <h1 class="text-primary mb-4">Welcome to the Admin Dashboard</h1>
        <div class="admin-links row">
            <a href="categories-services/index.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Manage Categories & Services</a>
            <a href="members/index.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Manage Members</a>
            <a href="manageBookings.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Manage Bookings</a>
            <a href="manageApplicants.jsp" class="col-md-3 mb-3 btn btn-primary btn-lg">Manage Job Applicants</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
