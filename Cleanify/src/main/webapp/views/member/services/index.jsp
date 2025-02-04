<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Categories.CategoryList"%>
<%@ page import="Categories.Category" %>
<%@ page import="Services.Service" %>
<%@ page import="Services.ServiceList" %>
<%@ page import="Services.ServiceServlet" %>
<%@ page import="java.util.*" %>
<%@ page import="Persons.Person" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cleanify</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/scrollreveal/4.0.5/scrollreveal.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/crypto-js@3.1.9-1/crypto-js.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=arrow_forward" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
</head>
<style>
body{
        background: linear-gradient(#eceffe, #ced6fb);}
</style>

<body>
        <%@ include file="/views/Util/components/header/header.jsp" %>


<%
    List<Category> categoryList = CategoryList.getCategories();
%>

<div class="services-section container my-5">
    <div class="text-center mb-4">
        <h3 class="fw-bold" style="color: #17a2b8;">Our Services</h3>
        <p class="text-muted" style="font-size: 1.2rem; line-height: 1.7;">
            Explore our wide range of <span class="fw-bold text-primary">home cleaning services</span>, 
            categorized for your convenience. Whether it’s a quick tidy-up or a deep clean, we have 
            everything you need to make your home shine!
        </p>
        <div class="mb-3">
            <label for="categoryFilter" class="form-label">Filter Categories</label>
            <select id="categoryFilter" class="form-select form-select-sm" style="width: 200px; display: inline;">
                <option value="all">All Categories</option>
                <% for (Category category : categoryList) { %>
                    <option value="<%= category.getId() %>"><%= category.getName() %></option>
                <% } %>
            </select>
        </div>
    </div>

    <div class="accordion" id="serviceListInCategories">
        <% 
        for (Category category : categoryList) { 
            int serviceCount = category.getServices().size();
        %>
            <div class="accordion-item shadow-sm mb-4 category-item" data-category-id="<%= category.getId() %>">
                <h2 class="accordion-header" id="heading-<%= category.getId() %>">
                    <button class="accordion-button collapsed py-3 px-4" 
                            type="button" 
                            data-bs-toggle="collapse" 
                            data-bs-target="#collapse-<%= category.getId() %>" 
                            aria-expanded="false" 
                            aria-controls="collapse-<%= category.getId() %>"
                            style="color: #17a2b8;">
                        <%= category.getName() %> 
                        <span class="badge bg-primary ms-2"><%= serviceCount %></span>
                    </button>
                </h2>
                <div id="collapse-<%= category.getId() %>" class="accordion-collapse collapse" aria-labelledby="heading-<%= category.getId() %>" data-bs-parent="#serviceListInCategories">
                    <div class="accordion-body">
                        <% if (serviceCount == 0) { %>
                            <div class="text-muted">
                                No services available for this category.
                            </div>
                        <% } else { %>
                            <ul class="list-group service-list">
                                <% for (Service service : category.getServices()) { %>
                                    <li class="list-group-item d-flex justify-content-between align-items-center service-item">
                                        <span class="service-name">
                                            <strong><%= service.getName() %></strong>
                                        </span>
                                        <form action="${pageContext.request.contextPath}/serviceServlet" method="GET">
										    <input type="hidden" name="service_id" value="<%= service.getId() %>" />
										    <button type="submit" class="btn btn-primary btn-sm">
										        View Details <i class="fa-solid fa-arrow-right ms-1"></i>
										    </button>
										</form>
                                    </li>
                                <% } %>
                            </ul>

                        <% } %>
                    </div>
                </div>
            </div>
        <% } %>
    </div>
</div>
<%@ include file="/views/Util/components/footer/footer.jsp" %>
<script>
    document.getElementById('categoryFilter').addEventListener('change', function() {
        const selectedCategoryId = this.value;
        const categoryItems = document.querySelectorAll('.category-item');

        categoryItems.forEach(item => {
            if (selectedCategoryId === 'all' || item.getAttribute('data-category-id') === selectedCategoryId) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });
</script>


    <!-- Include the JavaScript File -->
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
</body>
</html>
