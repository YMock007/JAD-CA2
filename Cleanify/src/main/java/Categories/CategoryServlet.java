package Categories;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/categoryServlet")
public class CategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 2L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            setSessionMessage(request, "No action specified.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    setSessionMessage(request, "Unknown action.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            }
        } catch (Exception e) {
            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name").trim(); // Trim whitespace
        if (name.isEmpty()) {
            setSessionMessage(request, "Category name cannot be empty.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            return;
        }

        if (CategoryList.isCategoryNameExists(name)) {
            setSessionMessage(request, "Category with name '" + name + "' already exists.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            return;
        }

        try {
            Category category = new Category(0, name, null);
            CategoryList.addCategory(category);
            setSessionMessage(request, "Category '" + name + "' added successfully.", "success");
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                setSessionMessage(request, "Category with name '" + name + "' already exists.", "error");
            } else {
                setSessionMessage(request, "Failed to add category: " + e.getMessage(), "error");
            }
        }
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("categoryId");
        String name = request.getParameter("name");

        if (idStr == null || name == null || name.isEmpty()) {
            setSessionMessage(request, "Invalid category data.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            if (CategoryList.isCategoryNameExistsExcludingId(name, id)) {
                setSessionMessage(request, "Category with name '" + name + "' already exists.", "error");
                response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
                return;
            }

            Category category = new Category(id, name, null);
            CategoryList.updateCategory(category);

            setSessionMessage(request, "Category '" + name + "' updated successfully.", "success");
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid category ID.", "error");
        }
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("categoryId");

        if (idStr == null) {
            setSessionMessage(request, "Category ID is required for deletion.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            CategoryList.deleteCategory(id);

            setSessionMessage(request, "Category deleted successfully.", "success");
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid category ID.", "error");
        }
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
