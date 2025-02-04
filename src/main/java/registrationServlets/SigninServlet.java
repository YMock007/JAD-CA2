package registrationServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

import Persons.Person;
import Persons.personDataAccessObject;
import handlingPassword.passwordHashing;

/**
 * Servlet implementation class SigninServlet
 */
@WebServlet("/SigninServlet")
public class SigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SigninServlet() {
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
	    String email = request.getParameter("signinEmail");
	    String password = request.getParameter("signinPassword");

	    try {
	        HttpSession session = request.getSession();

	        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
	            session.setAttribute("errorMessage", "Invalid email format.");
	            response.sendRedirect("views/registration/signIn.jsp");
	            return;
	        }
	        if (password == null || password.isEmpty()) {
	            session.setAttribute("errorMessage", "Password cannot be empty.");
	            response.sendRedirect("views/registration/signIn.jsp");
	            return;
	        }

	        // Retrieve user from the database
	        Person person = personDataAccessObject.getPersonByEmail(email);

	        if (person == null) {
	            // User does not exist
	            session.setAttribute("errorMessage", "Invalid email or password.");
	            response.sendRedirect("views/registration/signIn.jsp");
	            return;
	        }
	        
	        // Ensure the user is not a Google user
	        if (person.getIsGoogleUser()) {
	            session.setAttribute("errorMessage", "This account is registered with Google. Please use Google Sign-In.");
	            response.sendRedirect("views/registration/signIn.jsp");
	            return;
	        }

	        // Verify the password
	        if (passwordHashing.checkPassword(password, person.getPassword())) {
	            // Successful normal login

	        	
	            session.setAttribute("person",person);

	            if (person.getRoleId() == 1) { 
	                response.sendRedirect("views/admin/dashboard/index.jsp");
	            } else if (person.getRoleId() == 2) {
	                response.sendRedirect("views/member/home.jsp");
	            } else {
	                response.sendRedirect("views/member/home.jsp");
	            }
	        } else {
	            // Invalid password
	            session.setAttribute("errorMessage", "Invalid email or password.");
	            response.sendRedirect("views/registration/signIn.jsp");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.getSession().setAttribute("errorMessage", "An error occurred. Please try again.");
	        response.sendRedirect("views/registration/signIn.jsp");
	    }
	}
}
