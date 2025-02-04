package registrationServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import Persons.Person;
import Persons.personDataAccessObject;

/**
 * Servlet implementation class GoogleSignInServlet
 */
@WebServlet("/GoogleSignInServlet")
public class GoogleSigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleSigninServlet() {
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

        try {
            Person person = personDataAccessObject.getPersonByEmail(email);

            if (person == null || !person.getIsGoogleUser()) {
                request.getSession().setAttribute("errorMessage", "No Google account linked with this email. Please register first.");
                response.sendRedirect("views/registration/signIn.jsp");
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("person", person);

            if (person.getRoleId() == 1) {
                response.sendRedirect("adminDashboard.jsp");
            } else {
                response.sendRedirect("views/profile/profile.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "An error occurred. Please try again.");
            response.sendRedirect("views/registration/signIn.jsp");
        }
    }

}
