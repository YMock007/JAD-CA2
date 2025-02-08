package Inquiry;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class Inquiry
 */
@WebServlet("/Inquiry")
public class InquiryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InquiryServlet() {
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
	    try {
	        String name = request.getParameter("name");
	        String email = request.getParameter("email");
	        String title = request.getParameter("title");
	        String description = request.getParameter("description");

	        if (name == null || name.trim().isEmpty() || 
	            email == null || email.trim().isEmpty() || 
	            title == null || title.trim().isEmpty() || 
	            description == null || description.trim().isEmpty()) {

	            setSessionMessage(request, "All fields are required.", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        if (!name.matches("^[a-zA-Z\\s]+$")) {
	            setSessionMessage(request, "Invalid name. Only letters and spaces are allowed.", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
	            setSessionMessage(request, "Invalid email format.", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        if (!title.matches("^[a-zA-Z0-9\\s]{3,50}$")) {
	            setSessionMessage(request, "Invalid title. Only letters, numbers, and spaces are allowed (3-50 characters).", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        if (!description.matches("^[a-zA-Z0-9\\s.,!?'-]{5,500}$")) {
	            setSessionMessage(request, "Invalid description. Only letters, numbers, spaces, and common punctuation are allowed (10-500 characters).", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }
	        
	        Inquiry reqInquiry = new Inquiry(name, email, title, description);
	        boolean isCreated = InquiryDAO.createInquiry(reqInquiry);
	        
	        if(isCreated) {
		        setSessionMessage(request, "Submission successful!", "success");
		        response.sendRedirect(request.getHeader("Referer"));
	        }else {
		        setSessionMessage(request, "Submission failed, please try again!", "error");
		        response.sendRedirect(request.getHeader("Referer"));
	        }
	        


	    } catch (Exception e) {
	        e.printStackTrace();
	        setSessionMessage(request, "An error occurred while processing your request.", "error");
	        response.sendRedirect(request.getHeader("Referer"));
	    }
	}

	private void setSessionMessage(HttpServletRequest request, String message, String status) {
	    HttpSession session = request.getSession();
	    session.setAttribute("message", message);
	    session.setAttribute("status", status);
	}



}
