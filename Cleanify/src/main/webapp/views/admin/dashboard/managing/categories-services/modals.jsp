<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Ensure this file is included only once in the context
    Boolean isDirectAccess = (Boolean) request.getAttribute("isDirectAccess");
    if (isDirectAccess == null) {
        isDirectAccess = request.getAttribute("includedFromParent") == null;
        request.setAttribute("isDirectAccess", isDirectAccess);
    }

    if (isDirectAccess) {
%>
    <%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%
    }
%>

<%@ page import="Categories.CategoryList, Categories.Category, Services.Service" %>

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Category</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/categoryServlet" method="POST">
                    <input type="hidden" name="action" value="add" />
                    <div class="form-group">
                        <label for="newCategoryName">Category Name</label>
                        <input type="text" class="form-control" id="newCategoryName" name="name" required
                               pattern="^[A-Za-z0-9\s]+$"
                               title="Category name can only contain letters, numbers, and spaces." />
                    </div>
                    <button type="submit" class="btn btn-success">
                        Add Category
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- Add Service Modal -->
<div class="modal fade" id="addServiceModal" tabindex="-1" aria-labelledby="addServiceLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Add New Service</h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                    <span>&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="addServiceForm" action="${pageContext.request.contextPath}/serviceServlet" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add" />
                    
                    <!-- Category Selection -->
                    <div class="form-group">
                        <label for="categoryId"><strong>Select Category</strong></label>
                        <select class="form-control" id="categoryId" name="categoryId" required>
                            <% for (Category category : CategoryList.getCategories()) { %>
                                <option value="<%= category.getId() %>">
                                    <%= category.getName() %>
                                </option>
                            <% } %>
                        </select>
                    </div>

                    <!-- Service Name -->
                    <div class="form-group">
                        <label for="serviceName"><strong>Service Name</strong></label>
                        <input type="text" class="form-control" id="serviceName" name="name" placeholder="Enter service name" 
                               required pattern="^[A-Za-z0-9\s]+$" title="Service name can only contain letters, numbers, and spaces." />
                    </div>

                    <!-- Description -->
                    <div class="form-group">
                        <label for="serviceDescription"><strong>Description</strong></label>
                        <textarea class="form-control" id="serviceDescription" name="description" placeholder="Enter a brief description" 
                                  rows="3" required maxlength="200" title="Description should not exceed 200 characters."></textarea>
                    </div>

                    <!-- Price -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="servicePrice"><strong>Price (SGD)</strong></label>
                            <input type="number" class="form-control" id="servicePrice" name="price" placeholder="Enter price" 
                                   step="0.01" min="0.01" required title="Price must be a positive number." />
                        </div>

                        <!-- Estimated Duration -->
                        <div class="form-group col-md-6">
                            <label for="serviceEstDuration"><strong>Estimated Duration (hrs)</strong></label>
                            <input type="number" class="form-control" id="serviceEstDuration" name="estDuration" 
                                   placeholder="Enter estimated duration" step="0.1" min="0.1" 
                                   required title="Duration must be a positive number." />
                        </div>
                    </div>

                    <!-- Image Upload -->
                    <div class="form-group">
                        <label for="serviceImage"><strong>Upload Image</strong></label>
                        <input type="file" class="form-control-file" id="serviceImage" name="image" />
                        <small class="form-text text-muted">Leave empty to use the default image.</small>
                    </div>

                    <!-- Submit Button -->
                    <div class="text-right">
                        <button type="submit" class="btn btn-success font-weight-bold">
                            <i class="fas fa-plus-circle"></i> Add Service
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
