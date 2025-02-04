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

/**
 * Servlet implementation class UpdateProfileServlet
 */
@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.getWriter().print("Test Servlet is working!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        String postalCode = request.getParameter("postalCode").trim();

        response.getWriter().print("hello");
        response.getWriter().print("Test Servlet is working!");

        // Backend Validation
        if (name.length() < 2 || name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Name must be between 2 and 50 characters.");
            response.sendRedirect("views/profile/profile.jsp");
            return;
        }

        if (phone != null && !phone.matches("^[0-9]{8,15}$")) {
            request.setAttribute("errorMessage", "Phone Number must be numeric and between 8 to 15 digits.");
            response.sendRedirect("views/profile/profile.jsp");
            return;
        }

        if (address.length() < 5) {
            request.setAttribute("errorMessage", "Address must be at least 5 characters long.");
            response.sendRedirect("views/profile/profile.jsp");
            return;
        }

        if (!postalCode.matches("^[0-9]{4,6}$")) {
            request.setAttribute("errorMessage", "Postal Code must be numeric and between 4 to 6 digits.");
            response.sendRedirect("views/profile/profile.jsp");
            return;
        }

        try {
            // Fetch the user to preserve password and status ID
            Person currentPerson = personDataAccessObject.getPersonByEmail(email);

            if (currentPerson != null) {
                // Update person object
                currentPerson.setName(name);
                currentPerson.setPhNumber(phone);
                currentPerson.setAddress(address);
                currentPerson.setPostalCode(postalCode);

                personDataAccessObject.updatePerson(currentPerson);
                
                request.getSession().setAttribute("person", currentPerson);

                request.setAttribute("successMessage", "Profile updated successfully.");
                response.sendRedirect("views/profile/profile.jsp");
            } else {
                request.setAttribute("errorMessage", "Error: User not found.");
                response.sendRedirect("views/profile/profile.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while updating the profile.");
            response.sendRedirect("views/profile/profile.jsp");
        }
    }

}
