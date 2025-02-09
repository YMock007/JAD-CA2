package controller;

import dao.WorkerDAO;
import handlingPassword.PasswordHashing;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BookingCategory;
import model.Worker;
import service.BookingServiceClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;



@WebServlet("/LogInServlet")
public class LogInServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogInServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // ✅ Validate inputs
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("views/registration/logIn.jsp").forward(request, response);
            return;
        }

        // ✅ Fetch worker from database
        WorkerDAO workerDAO = new WorkerDAO();
        Worker worker = null;
        try {
            worker = workerDAO.getWorkerByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error. Please try again.");
            request.getRequestDispatcher("views/registration/logIn.jsp").forward(request, response);
            return;
        }

        // ✅ Debugging logs
        if (worker == null) {
   
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("views/registration/logIn.jsp").forward(request, response);
            return;
        }

     
        String userPass = worker.getPassword();

        // ✅ Check password
        boolean isPasswordMatch = BCrypt.checkpw(password, userPass);

        System.out.println("🔍 Password Match: " + isPasswordMatch);

        if (!isPasswordMatch) {
          
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("views/registration/logIn.jsp").forward(request, response);
            return;
        }
        
        List<Integer> workerCategories = Arrays.asList(Arrays.stream(worker.getCategoryIds()).boxed().toArray(Integer[]::new));


        // ✅ Create session and store worker info
        HttpSession session = request.getSession();
        session.setAttribute("workerId", worker.getId());
        session.setAttribute("workerName", worker.getName());
        session.setAttribute("workerEmail", worker.getEmail());
        session.setAttribute("workerRole", worker.getRoleId());
        session.setAttribute("workerCategories", workerCategories);
        
        List<BookingCategory> bookings = BookingServiceClient.fetchAvailableBookings();
        session.setAttribute("availableBookings", bookings);

        System.out.println("✅ Login successful!");
        response.sendRedirect("views/home/home.jsp");
    }

    private String validateInputs(String email, String password) {
	    if (email == null || email.isEmpty()) {
	        return "Email is required.";
	    }
	    if (password == null || password.isEmpty()) {
	        return "Password is required.";
	    }
    return null;


}
}

