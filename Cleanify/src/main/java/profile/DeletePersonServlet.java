package profile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DatabaseConnection;

/**
 * Servlet implementation class DeletePersonServlet
 */
@WebServlet("/DeletePersonServlet")
public class DeletePersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePersonServlet() {
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
        int userId = Integer.parseInt(request.getParameter("userId"));
        HttpSession session = request.getSession();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String deleteQuery = "DELETE FROM person WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, userId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    session.invalidate(); // Log out the user
                    response.sendRedirect("views/registration/signIn.jsp");
                } else {
                    session.setAttribute("errorMessage", "Failed to delete account. Please try again.");
                    response.sendRedirect("views/profile/profile.jsp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while deleting the account.");
            response.sendRedirect("views/profile/profile.jsp");
        }
    }

}
