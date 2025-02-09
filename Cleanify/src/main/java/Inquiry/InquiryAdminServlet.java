package Inquiry;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class InquiryAdminServlet
 */

@WebServlet("/InquiryAdminServlet")
public class InquiryAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InquiryAdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("🔍 [DEBUG] doGet() - Entering InquiryAdminServlet");

        // Fetch inquiries from database
        List<Inquiry> inquiries = InquiryDAO.getInquiriesAdmin();
        
        // Debugging: Print size of the list
        System.out.println("✅ [DEBUG] Total Inquiries Retrieved: " + (inquiries != null ? inquiries.size() : "null"));

        // Debugging: Print all inquiries retrieved
        if (inquiries != null) {
            for (Inquiry inquiry : inquiries) {
                System.out.println("📝 [DEBUG] Inquiry - ID: " + inquiry.getId() +
                                   ", Name: " + inquiry.getName() +
                                   ", Email: " + inquiry.getEmail() +
                                   ", Title: " + inquiry.getTitle() +
                                   ", Description: " + inquiry.getDescription());
            }
        } else {
            System.err.println("❌ [ERROR] Inquiry list is NULL!");
        }

        // Set attributes for JSP
        request.setAttribute("inquiries", inquiries);

        // Debugging: Confirming forward path
        System.out.println("➡️ [DEBUG] Forwarding request to: /views/admin/dashboard/managing/inquiries/index.jsp");
        
        request.getRequestDispatcher("/views/admin/dashboard/managing/inquiries/index.jsp").forward(request, response);
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("🔍 [DEBUG] InquiryAdminServlet doPost() - Handling form submission");

        // Get the ID and action from the request
        int inquiryId = Integer.parseInt(request.getParameter("id"));
        String action = request.getParameter("action");

        System.out.println("✅ [DEBUG] Received Form Data - ID: " + inquiryId + ", Action: " + action);

        if ("complete".equals(action)) {
            boolean success = InquiryDAO.updateInquiry(inquiryId);

            if (success) {
                System.out.println("✅ [DEBUG] Inquiry updated successfully!");
            } else {
                System.out.println("❌ [ERROR] Failed to update inquiry!");
            }
        } else {
            System.out.println("⚠️ [WARNING] Unknown action: " + action);
        }

        // Redirect back to the inquiry list page after updating
        response.sendRedirect(request.getContextPath() + "/InquiryAdminServlet");
    }


}
