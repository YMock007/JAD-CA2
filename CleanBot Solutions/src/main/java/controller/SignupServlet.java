package controller;

import dao.WorkerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Worker;
import model.WorkerDTO;
import handlingPassword.PasswordHashing;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/SignUpServlet")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SignupServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        // ✅ Get worker details from the form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        int role_id = 2;
        String[] selectedCategories = request.getParameterValues("categories");

        // ✅ Validate inputs
        String errorMessage = validateInputs(name, email, phone, password, selectedCategories);
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("views/registration/signUp.jsp").forward(request, response);
            return;
        }

        // ✅ Hash password
        

        // ✅ Convert category list to int array
        int[] categoryIds = new int[selectedCategories.length];
        for (int i = 0; i < selectedCategories.length; i++) {
            categoryIds[i] = Integer.parseInt(selectedCategories[i]);
        }

        // ✅ Create Worker Object
        Worker worker = new Worker(name, email, phone, password, role_id, categoryIds);

        // ✅ Insert into our local database
        WorkerDAO WorkerDAO = new WorkerDAO();
        
        boolean isAlreadyRegistered;
        try {
            isAlreadyRegistered = WorkerDAO.isAlreadyRegistered(worker.getEmail(), worker.getPhone());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: Unable to verify existing worker.");
            request.getRequestDispatcher("views/registration/signUp.jsp").forward(request, response);
            return;
        }

        if (isAlreadyRegistered) {
            request.setAttribute("error", "Registration failed. Email or phone is already in use.");
            request.getRequestDispatcher("views/registration/signUp.jsp").forward(request, response);
            return;
        }

        // 🔹 Insert worker if not already registered
        boolean isRegistered = false;
        int provider_Id = -1;

        try {
            provider_Id = WorkerDAO.insertWorker(worker, categoryIds);
            
            isRegistered = provider_Id != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isRegistered) {
            // ✅ Send only provider_id and name to Cleanify
            boolean isSyncedWithCleanify = sendWorkerToCleanify(provider_Id, worker.getName());

            if (!isSyncedWithCleanify) {
                request.setAttribute("error", "Worker registered, but failed to sync with Cleanify.");
                request.getRequestDispatcher("views/registration/logIn.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("views/registration/logIn.jsp?success=Signup+Successful");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("views/registration/signUp.jsp").forward(request, response);
        }
    }
    
    
    
    // Connecting to web service
    private boolean sendWorkerToCleanify(int provider_Id, String workerName) {
        Client client = ClientBuilder.newClient();
        String restUrl = "http://localhost:8081/cleanify-ws/registerWorker";

        WebTarget target = client.target(restUrl);

        WorkerDTO workerDTO = new WorkerDTO(provider_Id, workerName);
        System.out.println("📤 Sending JSON to Cleanify: " + workerDTO);

        Entity<WorkerDTO> entity = Entity.entity(workerDTO, MediaType.APPLICATION_JSON);

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response resp = invocationBuilder.post(entity);

        System.out.println("🔵 Response Status: " + resp.getStatus());

        if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
            System.out.println("❌ Error Response: " + resp.readEntity(String.class));
        }

        return resp.getStatus() == Response.Status.OK.getStatusCode();
    }


    /**
     * Validates user input and returns an error message if invalid.
     */
    private String validateInputs(String name, String email, String phone, String password, String[] categories) {
        if (name == null || name.trim().length() < 3) {
            return "Name must be at least 3 characters long.";
        }
        if (email == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            return "Invalid email format.";
        }
        if (phone == null || !Pattern.matches("^[0-9]{8,15}$", phone)) {
            return "Phone number must be between 8 and 15 digits.";
        }
        if (password == null || password.length() < 6 || !Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", password)) {
            return "Password must be at least 6 characters long and contain both letters and numbers.";
        }
        if (categories == null || categories.length == 0) {
            return "You must select at least one service category.";
        }
        return null; // No errors
    }
}
