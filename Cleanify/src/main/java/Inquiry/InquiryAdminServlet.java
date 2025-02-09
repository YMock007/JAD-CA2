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

        List<Inquiry> inquiries = InquiryDAO.getInquiriesAdmin();
        
        request.setAttribute("inquiries", inquiries);

        request.getRequestDispatcher("/views/admin/dashboard/managing/inquiries/index.jsp").forward(request, response);
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int inquiryId = Integer.parseInt(request.getParameter("id"));
        String action = request.getParameter("action");

        if ("complete".equals(action)) {
            boolean success = InquiryDAO.updateInquiry(inquiryId);
        }


        // Redirect back to the inquiry list page after updating
        response.sendRedirect(request.getContextPath() + "/InquiryAdminServlet");
    }


}
