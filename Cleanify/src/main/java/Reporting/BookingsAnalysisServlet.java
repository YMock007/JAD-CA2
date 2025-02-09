package Reporting;

import Bookings.BookingReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import db.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/bookings-analysis")
public class BookingsAnalysisServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public BookingsAnalysisServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get parameters for year and month
            String fetchType = request.getParameter("fetchType");
            int year = Integer.parseInt(request.getParameter("year"));
            int month = Integer.parseInt(request.getParameter("month"));

            if ("all".equals(fetchType)) {
                // Fetch booking data for the specified year and month
                List<BookingReport> bookingsByDate = BookingsDataAnalysis.getBookingsByDate(year, month);
                List<BookingReport> topCustomers = BookingsDataAnalysis.getTopCustomers();
                List<BookingReport> serviceBookings = BookingsDataAnalysis.getServiceBookings();

                // Set attributes for navigation
                request.setAttribute("selectedYear", year);
                request.setAttribute("selectedMonth", month);

                // Attach JSON data to request attributes
                request.setAttribute("bookingsByDate", bookingsByDate);
                request.setAttribute("topCustomers", topCustomers);
                request.setAttribute("serviceBookings", serviceBookings);

                // Forward to JSP
                request.getRequestDispatcher("/views/admin/dashboard/reporting/bookings/index.jsp").forward(request, response);
            } else {
                request.setAttribute("status", "error");
                request.setAttribute("message", "Invalid request type.");
                request.getRequestDispatcher("/views/admin/dashboard/reporting/bookings/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("status", "error");
            request.setAttribute("message", "Internal server error.");
            request.getRequestDispatcher("/views/admin/dashboard/reporting/bookings/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
