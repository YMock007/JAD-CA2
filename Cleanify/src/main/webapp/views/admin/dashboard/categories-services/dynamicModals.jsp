<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Ensure this file is included only once in the context
    Boolean isDirectAccessForDynamic = (Boolean) request.getAttribute("isDirectAccess");
    if (isDirectAccessForDynamic == null) {
        isDirectAccessForDynamic = request.getAttribute("includedFromParent") == null;
        request.setAttribute("isDirectAccess", isDirectAccessForDynamic);
    }

    if (isDirectAccessForDynamic) {
%>
    <%@ include file="/views/Util/auth/adminAuth.jsp" %>
<%
    }
%>

<%@ page import="Categories.CategoryList, Categories.Category, Services.Service" %>

<!-- Dynamic Modals for Categories and Services -->
<% for (Category category : CategoryList.getCategories()) { %>
    <!-- Update Category Modal -->
    <div class="modal fade" id="updateCategoryModal<%= category.getId() %>" tabindex="-1" aria-labelledby="updateCategoryLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Category</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/categoryServlet" method="POST">
                        <input type="hidden" name="action" value="update" />
                        <input type="hidden" name="categoryId" value="<%= category.getId() %>" />
                        <div class="form-group">
                            <label for="categoryName<%= category.getId() %>">Category Name</label>
                            <input type="text" class="form-control" id="categoryName<%= category.getId() %>" name="name"
                                   value="<%= category.getName() %>" required pattern="^[A-Za-z0-9\s]+$"
                                   title="Category name can only contain letters, numbers, and spaces." />
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="return validateAndSubmit(this)">Save Changes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Category Modal -->
    <div class="modal fade" id="deleteCategoryModal<%= category.getId() %>" tabindex="-1" aria-labelledby="deleteCategoryLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Delete Category</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete the category "<strong><%= category.getName() %></strong>"?</p>
                </div>
                <div class="modal-footer">
                    <form action="${pageContext.request.contextPath}/categoryServlet" method="POST">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="categoryId" value="<%= category.getId() %>" />
                        <button type="submit" class="btn btn-danger" onclick="return validateAndSubmit(this)">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <% for (Service service : category.getServices()) { %>
        <!-- Update Service Modal -->
        <div class="modal fade" id="updateServiceModal<%= service.getId() %>" tabindex="-1" aria-labelledby="updateServiceLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Service</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/serviceServlet" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="update" />
                            <input type="hidden" name="serviceId" value="<%= service.getId() %>" />
                            <div class="form-group">
                                <label for="serviceCategory<%= service.getId() %>">Select Category</label>
                                <select class="form-control" id="serviceCategory<%= service.getId() %>" name="categoryId" required>
                                    <% for (Category serviceCategory : CategoryList.getCategories()) { %>
                                        <option value="<%= serviceCategory.getId() %>" <%= serviceCategory.getId() == service.getCategoryId() ? "selected" : "" %>>
                                            <%= serviceCategory.getName() %>
                                        </option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="serviceName<%= service.getId() %>">Service Name</label>
                                <input type="text" class="form-control" id="serviceName<%= service.getId() %>" name="name"
                                       value="<%= service.getName() %>" required pattern="^[A-Za-z0-9\s]+$"
                                       title="Service name can only contain letters, numbers, and spaces." />
                            </div>
                            <div class="form-group">
                                <label for="serviceDescription<%= service.getId() %>">Description</label>
                                <textarea class="form-control" id="serviceDescription<%= service.getId() %>" name="description"
                                          required maxlength="200" title="Description should not exceed 200 characters."><%= service.getDescription() %></textarea>
                            </div>
                            <div class="form-group">
                                <label for="servicePrice<%= service.getId() %>">Price</label>
                                <input type="number" class="form-control" id="servicePrice<%= service.getId() %>" name="price"
                                       step="0.01" min="0.01" value="<%= service.getPrice() %>" required
                                       title="Price must be a positive number." />
                            </div>
                            <div class="form-group">
                                <label for="serviceEstDuration<%= service.getEstDuration() %>">Est. Duration</label>
                                <input type="number" class="form-control" id="serviceEstDuration<%= service.getEstDuration() %>" name="estDuration"
                                       step="0.01" min="0.01" value="<%= service.getEstDuration() %>" required
                                       title="Duration must be a positive number." />
                            </div>
                            <div class="form-group">
                                <label for="serviceImage<%= service.getId() %>">Upload New Image</label>
                                <input type="file" class="form-control-file" id="serviceImage<%= service.getId() %>" name="image" />
                                <small class="form-text text-muted">Do not upload file to use the current image.</small>
                            </div>
                            <button type="submit" class="btn btn-primary" onclick="return validateAndSubmit(this)">Save Changes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Service Modal -->
        <div class="modal fade" id="deleteServiceModal<%= service.getId() %>" tabindex="-1" aria-labelledby="deleteServiceLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Delete Service</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete the service "<strong><%= service.getName() %></strong>"?</p>
                    </div>
                    <div class="modal-footer">
                        <form action="${pageContext.request.contextPath}/serviceServlet" method="POST">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="serviceId" value="<%= service.getId() %>" />
                            <button type="submit" class="btn btn-danger" onclick="return validateAndSubmit(this)">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    <% } %>
<% } %>
