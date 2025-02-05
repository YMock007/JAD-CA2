package Bookings;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // ✅ Retrieve payment details
            String paymentIntentId = request.getParameter("paymentIntentId");
            if (paymentIntentId == null || paymentIntentId.isEmpty()) {
                setSessionMessage(request, "Payment verification failed. Try again.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/payment_failed.jsp");
                return;
            }

            // ✅ Retrieve booking details
            String memberIdStr = request.getParameter("memberId");
            int memberId = Integer.parseInt(memberIdStr);
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String postalCode = request.getParameter("postalCode");
            String specialRequest = request.getParameter("specialRequest");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");

            if (appointmentDate != null && !appointmentDate.isEmpty() && (appointmentTime == null || appointmentTime.isEmpty())) {
                appointmentTime = "00:00:00"; // Default midnight time
            }

            Date dateRequested = null;
            if (appointmentDate != null && !appointmentDate.isEmpty()) {
                dateRequested = Date.valueOf(appointmentDate);
            }

            Time timeRequested = null;
            if (appointmentTime != null && !appointmentTime.isEmpty()) {
                timeRequested = Time.valueOf(appointmentTime + ":00");
            }

            // ✅ Default booking status
            int statusId = 1;

            // ✅ Get booking cart from session
            HttpSession session = request.getSession(false);
            if (session == null) {
                setSessionMessage(request, "Session expired. Please log in again.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/home.jsp");
                return;
            }

            @SuppressWarnings("unchecked")
            HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");

            if (booking != null && !booking.isEmpty()) {
                boolean success = true;
                List<Integer> keysToRemove = new ArrayList<>();

                // ✅ Process each service in booking cart
                for (Map.Entry<Integer, Integer> entry : booking.entrySet()) {
                    int serviceId = entry.getKey();

                    boolean isCreated = BookingList.createBooking(
                        memberId, serviceId, statusId,
                        dateRequested, timeRequested, phoneNumber,
                        address, postalCode, specialRequest
                    );

                    if (!isCreated) {
                        success = false;
                        break;
                    }

                    // ✅ Remove service from session cart
                    HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
                    if (cart != null) {
                        cart.remove(serviceId);
                        session.setAttribute("cart", cart);
                    }

                    keysToRemove.add(serviceId);
                }

                // ✅ Remove booked services from session
                for (Integer key : keysToRemove) {
                    booking.remove(key);
                }

                // ✅ Redirect after successful booking
                if (success) {
                    setSessionMessage(request, "Booking successfully created.", "success");
                    response.sendRedirect(request.getContextPath() + "/views/member/booking_success.jsp");
                } else {
                    setSessionMessage(request, "Error while creating booking.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/member/booking_failed.jsp");
                }
            } else {
                setSessionMessage(request, "No services selected for booking.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
            }
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid number format: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            setSessionMessage(request, "Error processing booking: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        }
    }

    // ✅ Helper function to set session messages
    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
