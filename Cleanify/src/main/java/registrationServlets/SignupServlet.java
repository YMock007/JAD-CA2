package registrationServlets;

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
 * Servlet implementation class SigninServlet
 */
@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String beforePassword = request.getParameter("password");
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phnumber");
        String address = request.getParameter("address");
        String postalCode = request.getParameter("postalcode");
        int role_id = 2; // Default role for normal users
        boolean is_google_user = "Google_OAuth".equals(beforePassword);
        
        	if (email == null || email.isEmpty() || beforePassword == null || beforePassword.isEmpty() || 
                name == null || name.isEmpty() || address == null || address.isEmpty() || postalCode == null || postalCode.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "All required fields must be filled.");
                response.sendRedirect("views/registration/signUp.jsp");
                return;
            }
            
           
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                request.getSession().setAttribute("errorMessage", "Invalid email format.");
                response.sendRedirect("views/registration/signUp.jsp");
                return;
            }

            if (beforePassword.length() < 8) {
                request.getSession().setAttribute("errorMessage", "Password must be at least 8 characters long.");
                response.sendRedirect("views/registration/signUp.jsp");
                return;
            }

        try {
            // Check if the email already exists
            if (personDataAccessObject.isEmailRegistered(email)) {
                Person existingPerson = personDataAccessObject.getPersonByEmail(email);

                // Prevent Google users from signing up as normal users
                if (existingPerson.getIsGoogleUser() && !is_google_user) {
                    request.getSession().setAttribute("errorMessage", "This email is already linked to a Google account. Please use Google Sign-In.");
                    response.sendRedirect("views/registration/signIn.jsp");
                    return;
                }

                // Prevent normal users from signing up as Google users
                if (!existingPerson.getIsGoogleUser() && is_google_user) {
                    request.getSession().setAttribute("errorMessage", "This email is already registered. Please use normal Sign-In.");
                    response.sendRedirect("views/registration/signIn.jsp");
                    return;
                }
            } else {
                // New registration
                String hashedPassword = is_google_user ? "Google_OAuth" : passwordHashing.hashPassword(beforePassword);

                // Create a new user
                Person newPerson = new Person(0, email, hashedPassword, name, phoneNumber, address, postalCode, role_id, is_google_user);
                personDataAccessObject.createPerson(newPerson);

                request.getSession().setAttribute("successMessage", "Registration successful! You can now sign in.");
                response.sendRedirect("views/registration/signIn.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "An error occurred during registration. Please try again.");
            response.sendRedirect("views/registration/signUp.jsp");
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
