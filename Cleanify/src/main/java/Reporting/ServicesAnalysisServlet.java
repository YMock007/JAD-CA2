package Reporting;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/services-analysis")
public class ServicesAnalysisServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get parameters from the request
            String getALL = request.getParameter("getALL");
            String selectedYearParam = request.getParameter("year");
            String selectedMonthParam = request.getParameter("month");

            // Set default year and month if not provided
            int year = (selectedYearParam != null) ? Integer.parseInt(selectedYearParam) : LocalDate.now().getYear();
            int month = (selectedMonthParam != null) ? Integer.parseInt(selectedMonthParam) : LocalDate.now().getMonthValue();

            if ("all".equals(getALL)) {
                // Fetch the data
                request.setAttribute("bestRatedServices", safeGetList(ServicesDataAnalysis.getBestRatedServices()));
                request.setAttribute("lowestRatedServices", safeGetList(ServicesDataAnalysis.getLowestRatedServices()));
                request.setAttribute("highDemandServiceNames", safeGetList(ServicesDataAnalysis.getHighDemandServiceNames()));
                request.setAttribute("highDemandBookings", safeGetList(ServicesDataAnalysis.getHighDemandBookings()));
                request.setAttribute("mostBookedServiceNames", safeGetList(ServicesDataAnalysis.getMostBookedServiceNames()));
                request.setAttribute("mostBookedBookings", safeGetList(ServicesDataAnalysis.getMostBookedBookings()));
                request.setAttribute("categoryNames", safeGetList(ServicesDataAnalysis.getCategoryNames()));
                request.setAttribute("categoryRevenues", safeGetList(ServicesDataAnalysis.getCategoryRevenues()));

                List<DailyRevenueService> dailyRevenueTrends = ServicesDataAnalysis.getDailyRevenueTrends(year, month);
                request.setAttribute("dailyRevenueTrends", dailyRevenueTrends);


                // Always set the year and month attributes
                request.setAttribute("selectedYear", year);
                request.setAttribute("selectedMonth", month);

                // Forward to JSP
                request.getRequestDispatcher("/views/admin/dashboard/reporting/categories-services/index.jsp").forward(request, response);
            } else {
                request.setAttribute("status", "error");
                request.setAttribute("message", "Invalid request for data analysis");
                request.getRequestDispatcher("/views/admin/dashboard/reporting/categories-services/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            request.setAttribute("message", "Internal server error");
            request.getRequestDispatcher("/views/admin/dashboard/reporting/categories-services/index.jsp").forward(request, response);
        }
    }

    private <T> List<T> safeGetList(List<T> data) {
        return (data != null) ? data : Collections.emptyList();
    }
}
