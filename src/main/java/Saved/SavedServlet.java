package Saved;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import Persons.Person;

/**
 * Servlet implementation class SavedServlet
 */
@WebServlet("/SavedServlet")
public class SavedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SavedServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String action = request.getParameter("action");

	        if (action == null) {
	            setSessionMessage(request, "No action specified.", "error");
	            response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        try {
	            switch (action) {
	                case "add":
	                    addToSavedItems(request, response);
	                    break;
	                case "remove":
	                	removeFromSavedItems(request, response);
	                    break;
	                default:
	                    setSessionMessage(request, "Unknown action.", "error");
	                    response.sendRedirect(request.getHeader("Referer"));
	            }
	        } catch (Exception e) {
	            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
	            response.sendRedirect(request.getHeader("Referer"));
	        }
	    }
	    
	    private void addToSavedItems(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
	        Integer serviceId = Integer.parseInt(request.getParameter("service_id"));
	        Person member = (Person) request.getSession().getAttribute("person");
	        
	        if (member == null || serviceId == null) {
	            setSessionMessage(request, "Invalid service data.", "error");
		        response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        try {
	            boolean isInCart = SavedList.checkSavedItems(member, serviceId);
	            
	            if (!isInCart) {
	                setSessionMessage(request, "Service is already in your saved items.", "info");
	            } else {
	                SavedList.addToSavedItems(member, serviceId);
	                setSessionMessage(request, "Service has been successfully added to your saved items.", "success");
	                
	            }

	        } catch (NumberFormatException e) {
	            setSessionMessage(request, "Invalid service ID or member ID.", "error");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            setSessionMessage(request, "Database error occurred while adding service to your saved items.", "error");
	            e.printStackTrace();
	        }

	        response.sendRedirect(request.getHeader("Referer"));
	    }

	    
	    private void removeFromSavedItems(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
	        Integer savedId = Integer.parseInt(request.getParameter("saved_id"));
	        Person member = (Person) request.getSession().getAttribute("person");

	        
	        if (member == null || savedId == null) {
	            setSessionMessage(request, "Invalid service data.", "error");
		        response.sendRedirect(request.getHeader("Referer"));
	            return;
	        }

	        try {
	             SavedList.removeFromSavedItems(savedId, member);
	             setSessionMessage(request, "Service has been removed successfully from your saved Items.", "success");


	        } catch (NumberFormatException e) {
	            setSessionMessage(request, "Invalid service ID or member ID.", "error");
	            e.printStackTrace();
	        } catch (Exception e) {
	            setSessionMessage(request, "error occurred while adding service to saved Items.", "error");
	            e.printStackTrace();
	        }
	        
	        response.sendRedirect(request.getHeader("Referer"));
	    }
	    
	    private void setSessionMessage(HttpServletRequest request, String message, String status) {
	        HttpSession session = request.getSession();
	        session.setAttribute("message", message);
	        session.setAttribute("status", status);
	    }

}
