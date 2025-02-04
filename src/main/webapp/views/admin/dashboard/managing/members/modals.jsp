<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<%@ page import="Persons.PersonList, Persons.Person" %>
<%@ page import="Roles.RoleList, Roles.Role" %>

<div class="modal fade" id="addPersonModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Member</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/personServlet" method="POST">
                    <input type="hidden" name="action" value="add" />

                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" class="form-control" name="name" required />
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" class="form-control" name="password" required />
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" class="form-control" name="email" required />
                    </div>
                    <div class="form-group">
                        <label>Phone Number</label>
                        <input type="text" class="form-control" name="phNumber" placeholder="Optional" />
                    </div>
                    <div class="form-group">
                        <label>Address</label>
                        <input type="text" class="form-control" name="address" placeholder="Optional" />
                    </div>
                    <div class="form-group">
                        <label>Role</label>
                        <select class="form-control" name="role_id" required>
                            <% for (Role roleAdd : RoleList.getRoles()) { %>
                                <option value="<%= roleAdd.getId() %>"><%= roleAdd.getName() %></option>
                            <% } %>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-success">Add</button>
                </form>
            </div>
        </div>
    </div>
</div>

