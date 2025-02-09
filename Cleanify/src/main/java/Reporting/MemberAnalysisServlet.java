package Reporting;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import Persons.MemberReport;

@WebServlet("/members-analysis")
public class MemberAnalysisServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MemberAnalysisServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String getAll = request.getParameter("getALL");

            if ("all".equals(getAll)) {
                // Fetch data for members analysis
                List<MemberReport> membersByPostal = MemberDataAnalysis.getMembersByPostalCode();
                List<MemberReport> mostActiveMembers = MemberDataAnalysis.getMostActiveMembers();

                // **Debugging Statements**
                System.out.println("✅ MembersByPostal: " + membersByPostal);
                System.out.println("✅ MostActiveMembers: " + mostActiveMembers);

                // **Check if Data is Empty**
                if (membersByPostal == null || membersByPostal.isEmpty()) {
                    System.out.println("⚠ No Members Found by Postal Code");
                }
                if (mostActiveMembers == null || mostActiveMembers.isEmpty()) {
                    System.out.println("⚠ No Most Active Members Found");
                }

                // Set attributes for JSP
                request.setAttribute("membersByPostal", membersByPostal);
                request.setAttribute("mostActiveMembers", mostActiveMembers);

                // Forward to JSP
                request.getRequestDispatcher("/views/admin/dashboard/reporting/members/index.jsp").forward(request, response);
            } else {
                request.setAttribute("status", "error");
                request.setAttribute("message", "Invalid request for data analysis");
                request.getRequestDispatcher("/views/admin/dashboard/reporting/members/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            request.setAttribute("message", "Internal server error");
            request.getRequestDispatcher("/views/admin/dashboard/reporting/members/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
