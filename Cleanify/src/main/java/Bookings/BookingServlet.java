package Bookings;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map.Entry;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve form parameters and validate them
            String memberIdStr = request.getParameter("memberId");
            int memberId = Integer.parseInt(memberIdStr);
            int cleanerId = Integer.parseInt(request.getParameter("cleanerId"));
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String postalCode = request.getParameter("postalCode");
            String specialRequest = request.getParameter("specialRequest");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");

            // Default time of 00:00:00 for date-only input if no time is provided
            if (appointmentDate != null && !appointmentDate.isEmpty() && (appointmentTime == null || appointmentTime.isEmpty())) {
                appointmentTime = "00:00:00"; // Use midnight time if no time is given
            }

            // Parse appointment date (as Date) and appointment time (as Time)
            java.sql.Date dateRequested = null;
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                dateRequested = java.sql.Date.valueOf(appointmentDate); 
            }

            java.sql.Time timeRequested = null;
            if (appointmentTime != null && !appointmentTime.isEmpty()) {
                // Convert appointmentTime to java.sql.Time (HH:MM:SS format)
                timeRequested = java.sql.Time.valueOf(appointmentTime + ":00");
            }

            // Default status ID
            int statusId = 1;

            // Get the booking HashMap from session
            HttpSession session = request.getSession(false); // Use false to avoid creating a new session
            if (session == null) {
                setSessionMessage(request, "Session expired. Please log in again.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/home.jsp");
                return;
            }

            @SuppressWarnings("unchecked")
            HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");

            // Ensure the booking HashMap is not empty
            if (booking != null && !booking.isEmpty()) {
                boolean success = true;

                // Create a list to store the keys of services to remove after processing
                List<Integer> keysToRemove = new ArrayList<>();

                // Loop through all services in the booking HashMap
                for (Entry<Integer, Integer> entry : booking.entrySet()) {
                    int serviceId = entry.getKey(); // Retrieve service ID

                    // Create booking entry for each service
                    boolean isCreated = BookingList.createBooking(
                            memberId, cleanerId, serviceId, statusId,
                            dateRequested, timeRequested, phoneNumber,
                            address, postalCode, specialRequest
                    );

                    // If any of the bookings failed, set success to false
                    if (!isCreated) {
                        success = false;
                        break;
                    }

                    // Remove the serviceId from the cart if it exists
                    HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
                    if (cart != null && cart.containsKey(serviceId)) {
                        cart.remove(serviceId);
                        session.setAttribute("cart", cart);
                    }

                    // Add the serviceId to the list of keys to remove
                    keysToRemove.add(serviceId);
                }

                // After the loop, remove the services from the booking map
                for (Integer key : keysToRemove) {
                    booking.remove(key);
                }

                // Respond based on success or failure
                if (success) {
                    setSessionMessage(request, "Booking successfully processed.", "success");
                    response.sendRedirect(request.getHeader("Referer"));
                } else {
                    setSessionMessage(request, "Error occurred while processing the booking.", "error");
                }
            } else {
                setSessionMessage(request, "No services selected for booking.", "error");
            }
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid number format: " + e.getMessage(), "error");
        } catch (Exception e) {
            e.printStackTrace();
            setSessionMessage(request, "Error processing booking: " + e.getMessage(), "error");
        }
    }

    // Set session message
    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}

