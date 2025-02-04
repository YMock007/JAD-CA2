package Persons;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/personServlet")
public class PersonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            setSessionMessage(request, "No action specified.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addPerson(request, response);
                    break;
                case "update":
                    updatePerson(request, response);
                    break;
                case "delete":
                    deletePerson(request, response);
                    break;
                default:
                    setSessionMessage(request, "Unknown action.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
            }
        } catch (Exception e) {
            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
        }
    }

    private void addPerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phNumber = request.getParameter("phNumber");
        String address = request.getParameter("address");
        String postalCode = request.getParameter("postalCode");
        int roleId = Integer.parseInt(request.getParameter("role_id"));
        Boolean isGoogleUser = false;

        // Check if email already exists
        if (PersonList.isEmailExists(email)) {
            setSessionMessage(request, "Email already exists. Please use a different email.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
            return;
        }

        // Add person
        Person person = new Person(name, password, email, phNumber, address, postalCode, roleId, isGoogleUser);
        PersonList.addPerson(person);
        setSessionMessage(request, "Person added successfully.", "success");
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
    }

    private void updatePerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phNumber = request.getParameter("phNumber");
        String address = request.getParameter("address");
        String postalCode = request.getParameter("postalCode");
        int roleId = Integer.parseInt(request.getParameter("role_id"));
        Boolean isGoogleUser = false;

        // Fetch the existing person details
        Person existingPerson = PersonList.getPersonById(id);

        // Validate if email is being changed and already exists
        if (!email.equals(existingPerson.getEmail()) && PersonList.isEmailExists(email)) {
            setSessionMessage(request, "Email already exists. Please use a different email.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
            return;
        }

        // Validate if all details are the same
        if (existingPerson.equals(new Person(id, name, null, email, phNumber, address, postalCode, roleId, isGoogleUser))) {
            setSessionMessage(request, "No changes detected. Please update at least one field.", "error");
            response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
            return;
        }

        // Update person
        Person person = new Person(id, name, null, email, phNumber, address, postalCode, roleId, isGoogleUser);
        PersonList.updatePerson(person);
        setSessionMessage(request, "Person updated successfully.", "success");
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
    }

    private void deletePerson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        PersonList.deletePerson(id);
        setSessionMessage(request, "Person deleted successfully.", "success");
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/members/index.jsp");
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
