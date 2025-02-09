package Inquiry;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/Inquiry")
public class InquiryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public InquiryServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if ("complete".equals(action)) {
                int inquiryId = Integer.parseInt(request.getParameter("id"));
                boolean isUpdated = InquiryDAO.updateInquiryStatus(inquiryId, 2);
                if (isUpdated) {
                    setSessionMessage(request, "Inquiry marked as completed successfully.", "success");
                } else {
                    setSessionMessage(request, "Failed to update inquiry status.", "error");
                }
                redirectBack(request, response);
                return;
            }

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String title = request.getParameter("title");
            String description = request.getParameter("description");

            if (name == null || name.trim().isEmpty() || 
                email == null || email.trim().isEmpty() || 
                title == null || title.trim().isEmpty() || 
                description == null || description.trim().isEmpty()) {
                setSessionMessage(request, "All fields are required.", "error");
                redirectBack(request, response);
                return;
            }

            Inquiry reqInquiry = new Inquiry(name, email, title, description);
            boolean isCreated = InquiryDAO.createInquiry(reqInquiry);

            if(isCreated) {
                setSessionMessage(request, "Submission successful!", "success");
            } else {
                setSessionMessage(request, "Submission failed, please try again!", "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setSessionMessage(request, "An error occurred while processing your request.", "error");
        }
        redirectBack(request, response);
    }

    // Helper method to redirect back to the referring page
    private void redirectBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String referer = request.getHeader("referer"); // Get the previous page URL
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer); // Redirect back to the previous page
        } else {
            response.sendRedirect("defaultPage.jsp"); // Fallback page if referer is unavailable
        }
    }


    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
    
    
}
