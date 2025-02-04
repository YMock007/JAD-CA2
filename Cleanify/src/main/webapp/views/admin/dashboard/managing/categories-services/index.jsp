<%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="Services.*, Categories.*"%>
<%
request.setAttribute("includedFromParent", true);
%>
<%@ include file="/views/Util/components/header/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Manage Categories & Services</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/views/admin/dashboard/managing/categories-services/index.css">
</head>

<body>
    <div class="container my-5">
        <h2 class="text-primary mb-4">Manage Categories & Services</h2>
		
       	<!-- Buttons for adding category and service -->
        <div class="mb-3">
        	<!-- Search Bar -->
		    <div class="search-container">
		        <input type="text" id="searchBar" class="form-control" placeholder="Search..." />
		        <div class="checkbox-container">
		            <label>
		                <input type="checkbox" id="searchCategory" checked /> Categories
		            </label>
		            <label>
		                <input type="checkbox" id="searchService" checked /> Services
		            </label>
		        </div>
		    </div>
            <button type="button" class="btn btn-info font-weight-bold" data-toggle="modal" data-target="#addCategoryModal">
                <i class="fas fa-plus-circle"></i> Add New Category
            </button>
            <button type="button" class="btn btn-info font-weight-bold ml-2" data-toggle="modal" data-target="#addServiceModal">
                <i class="fas fa-plus-circle"></i> Add New Service
            </button>
        </div>
		
		<!-- Sort options with icons -->
	    <div class="mb-4">
	        <label class="mr-3 font-weight-bold">Sort By:</label>
		    <div class="btn-group" role="group" aria-label="Sort Options">
		        <button class="btn btn-custom-sort" id="sortAlphabeticalAsc">
		            <i class="fas fa-sort-alpha-down"></i> A to Z
		        </button>
		        <button class="btn btn-custom-sort" id="sortAlphabeticalDesc">
		            <i class="fas fa-sort-alpha-up"></i> Z to A
		        </button>
		        <button class="btn btn-custom-sort" id="sortServicesAsc">
		            <i class="fas fa-sort-numeric-down"></i> Fewest Services
		        </button>
		        <button class="btn btn-custom-sort" id="sortServicesDesc">
		            <i class="fas fa-sort-numeric-up"></i> Most Services
		        </button>
		    </div>
	    </div>

        <div id="categoriesContainer">
            <% for (Category category : CategoryList.getCategories()) { %>
                <div class="card mb-3 category-card" data-name="<%= category.getName() %>"
                    data-services="<%= category.getServices().size() %>">
                    <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center collapsed"
                        data-toggle="collapse" data-target="#collapseCategory<%= category.getId() %>">
                        <h5 class="mb-0">
                            <i class="indicator-icon fas fa-chevron-right mr-2"></i>
                            <%= category.getName() %>
                        </h5>
                        <div class="icon-container">
                            <button class="btn btn-secondary btn-sm edit-icon" data-toggle="modal"
                                data-target="#updateCategoryModal<%= category.getId() %>">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn btn-danger btn-sm delete-icon" data-toggle="modal"
                                data-target="#deleteCategoryModal<%= category.getId() %>">
                                <i class="fas fa-trash-alt"></i>
                            </button>
                        </div>
                    </div>
        
                    <div id="collapseCategory<%= category.getId() %>" class="collapse">
                        <div class="card-body">
                            <% if (category.getServices().isEmpty()) { %>
                                <div class="text-center p-3 text-muted">
                                    <em>No services available in this category.</em>
                                </div>
                                <% } else { %>
                                    <table class="table table-bordered table-hover">
                                        <thead class="thead-light">
                                            <tr>
                                                <th>ID</th>
                                                <th>Photo</th>
                                                <th>Service Name</th>
                                                <th>Description</th>
                                                <th>Price</th>
                                                <th>Est. Duration</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% int i=1; for (Service service : category.getServices()) { %>
                                                <tr>
                                                    <td>
                                                        <%= i++ %>
                                                    </td>
                                                    <td>
                                                        <img src="<%= service.getImageUrl() != null && !service.getImageUrl().isEmpty()
                                                        ? service.getImageUrl()
                                                        : "https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/DefaultPicture.png" %>"
                                                        style="width: 100px; height: 100px; object-fit: cover; border-radius:
                                                        8px; border: 2px solid #dee2e6;"
                                                        alt="<%= service.getName() %>" />
                                                    </td>
                                                    <td>
                                                        <%= service.getName() %>
                                                    </td>
                                                    <td>
                                                        <%= service.getDescription() %>
                                                    </td>
                                                    <td>$<%= service.getPrice() %>
                                                    </td>
                                                    <td><%= service.getEstDuration() %>
                                                    </td>
                                                    <td>
                                                        <div class="action-icon-container">
                                                            <button class="btn btn-secondary btn-sm edit-icon"
                                                                data-toggle="modal"
                                                                data-target="#updateServiceModal<%= service.getId() %>">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button class="btn btn-danger btn-sm delete-icon"
                                                                data-toggle="modal"
                                                                data-target="#deleteServiceModal<%= service.getId() %>">
                                                                <i class="fas fa-trash-alt"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <% } %>
                                        </tbody>
                                    </table>
                                    <% } %>
                        </div>
                    </div>
                </div>
                <% } %>
        </div>
    </div>
	<%@ include file="/views/admin/dashboard/managing/categories-services/modals.jsp" %>
	<%@ include file="/views/admin/dashboard/managing/categories-services/dynamicModals.jsp" %>
	<%@ include file="/views/Util/notification.jsp" %>
	<script src="<%= request.getContextPath() %>/views/admin/dashboard/managing/categories-services/index.js" type="text/javascript"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
