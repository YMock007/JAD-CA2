package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UpdateBookingDTO;
import service.UpdateBookingClient;

import java.io.IOException;

@WebServlet("/AcceptBookingServlet")
public class AcceptBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AcceptBookingServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ✅ Get worker (provider) ID from session
        String providerIdB = request.getParameter("workerId");

        if (providerIdB == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Worker not logged in");
            return;
        }

        // ✅ Get booking ID and status ID from request
        String bookingIdB = request.getParameter("bookingId");
        String statusIdB = request.getParameter("statusId");

        if (bookingIdB == null || statusIdB == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Bad Request: Missing bookingId or statusId");
            return;
        }
        int providerId = Integer.parseInt(providerIdB);
        int bookingId = Integer.parseInt(bookingIdB);
        int statusId = Integer.parseInt(statusIdB);
       System.out.print("In servlet");
        System.out.println(providerId);
        System.out.println(bookingId);
        System.out.println(statusId);
        

        // ✅ Create DTO for API request
        UpdateBookingDTO updateBookingDTO = new UpdateBookingDTO();
        updateBookingDTO.setBookingId(bookingId);
        updateBookingDTO.setProviderId(providerId);
        updateBookingDTO.setStatusId(statusId);

        // ✅ Call Cleanify API
        boolean success = UpdateBookingClient.acceptBooking(updateBookingDTO);

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Booking accepted successfully");
            response.sendRedirect("views/home/home.jsp");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to accept booking");
            response.sendRedirect("views/home/home.jsp");
        }
    }
}
