package profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import Persons.Person;
import Persons.personDataAccessObject;
import handlingPassword.passwordHashing;

/**
 * Servlet implementation class UpdatePasswordServlet
 */
@WebServlet("/UpdatePasswordServlet")
public class UpdatePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePasswordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword").trim();
        String newPassword = request.getParameter("newPassword").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();

        Person currentPerson = (Person) request.getSession().getAttribute("person");

        if (currentPerson.getIsGoogleUser()) {
            request.getSession().setAttribute("errorMessage", "Google users cannot change their password.");
            response.sendRedirect("views/profile/profile.jsp");
            return;
        }

        try {
            // Validate old password
            if (!personDataAccessObject.checkPassword(currentPerson.getEmail(), oldPassword)) {
                request.getSession().setAttribute("errorMessage", "Old password is incorrect.");
                response.sendRedirect("views/profile/profile.jsp");
                return;
            }

            // Validate new password
            if (newPassword.length() < 8 || newPassword.length() > 20 ||
                !newPassword.matches(".*[A-Z].*") ||
                !newPassword.matches(".*[a-z].*") ||
                !newPassword.matches(".*[0-9].*") ||
                !newPassword.matches(".*[@#$%^&*(),.?\":{}|<>].*")) {
                request.getSession().setAttribute("errorMessage", "New password does not meet the required criteria.");
                response.sendRedirect("views/profile/profile.jsp");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.getSession().setAttribute("errorMessage", "New password and confirm password do not match.");
                response.sendRedirect("views/profile/profile.jsp");
                return;
            }

            // Update the password
            boolean isUpdated = personDataAccessObject.updatePassword(
                currentPerson.getEmail(), 
                passwordHashing.hashPassword(newPassword) 
            );

            if (isUpdated) {
                request.getSession().setAttribute("successMessage", "Password updated successfully.");
                request.getSession().setAttribute("person", currentPerson);
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update the password.");
            }

            response.sendRedirect("views/profile/profile.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "An error occurred while updating the password.");
            response.sendRedirect("views/profile/profile.jsp");
        }
    }

}
