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
<%@ page import="Persons.PersonList, Persons.Person" %>
<%@ page import="Roles.RoleList, Roles.Role" %>

<% for (Person member : PersonList.getPeople()) { %>
    <!-- Update Modal -->
    <div class="modal fade" id="updateMemberModal<%= member.getId() %>" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Member</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                </div>
                <div class="modal-body">
                    <form action="${pageContext.request.contextPath}/personServlet" method="POST">
                        <input type="hidden" name="action" value="update" />
                        <input type="hidden" name="id" value="<%= member.getId() %>" />

                        <div class="form-group">
                            <label>Name</label>
                            <input type="text" class="form-control" name="name" value="<%= member.getName() %>" required />
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" class="form-control" name="email" value="<%= member.getEmail() %>" required />
                        </div>
                        <div class="form-group">
                            <label>Phone Number</label>
                            <input type="text" class="form-control" name="phNumber" value="<%= member.getPhNumber() != null ? member.getPhNumber() : "" %>" placeholder="Optional" />
                        </div>
                        <div class="form-group">
                            <label>Address</label>
                            <input type="text" class="form-control" name="address" value="<%= member.getAddress() != null ? member.getAddress() : "" %>" placeholder="Optional" />
                        </div>
                        <div class="form-group">
                            <label>Role</label>
                            <select class="form-control" name="role_id" required>
                                <% for (Role roleUpdate : RoleList.getRoles()) { %>
                                    <option value="<%= roleUpdate.getId() %>" <%= roleUpdate.getId() == member.getRoleId() ? "selected" : "" %>><%= roleUpdate.getName() %></option>
                                <% } %>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Modal -->
    <div class="modal fade" id="deleteMemberModal<%= member.getId() %>" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Delete Member</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">&times;</button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete "<%= member.getName() %>"?</p>
                </div>
                <div class="modal-footer">
                    <form action="${pageContext.request.contextPath}/personServlet" method="POST">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="id" value="<%= member.getId() %>" />
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
<% } %>


