package Bookings;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Date;
import java.util.*;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // ✅ Retrieve & Validate Payment Details
            String paymentIntentId = request.getParameter("paymentIntentId");
            if (paymentIntentId == null || paymentIntentId.isEmpty()) {
                setSessionMessage(request, "Payment verification failed. Try again.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/booking/booking.jsp");
                return;
            }
            byte[] paymentDetails = paymentIntentId.getBytes(StandardCharsets.UTF_8);

            // ✅ Retrieve & Validate Booking Details
            int memberId = Integer.parseInt(request.getParameter("memberId").trim());
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String address = request.getParameter("address").trim();
            String postalCode = request.getParameter("postalCode").trim();
            String specialRequest = request.getParameter("specialRequest") != null ? request.getParameter("specialRequest").trim() : "";
            String appointmentDateStr = request.getParameter("appointmentDate").trim();
            String appointmentTimeStr = request.getParameter("appointmentTime").trim();
            String billingAddress = request.getParameter("billingAddress");
            String billingPostalCode = request.getParameter("billingPostalCode");

            billingAddress = (billingAddress != null) ? billingAddress.trim() : "";
            billingPostalCode = (billingPostalCode != null) ? billingPostalCode.trim() : "";

            String bookingCartStr = request.getParameter("bookingCart");

            // ✅ Parse Date
            Date dateRequested = Date.valueOf(appointmentDateStr);

            // ✅ Parse Time
            Time timeRequested = (appointmentTimeStr != null && !appointmentTimeStr.isEmpty()) ?
                Time.valueOf(appointmentTimeStr + ":00") : Time.valueOf("00:00:00");

            // ✅ Retrieve & Validate Payment Information
            double amount = Double.parseDouble(request.getParameter("amount"));
            int paymentMethodId = Integer.parseInt(request.getParameter("paymentMethodId"));

            // ✅ Booking Status
            int statusId = 1;

            // ✅ Extract Service IDs from bookingCartStr
            List<Integer> serviceIds = extractServiceIds(bookingCartStr);

            // ✅ If no services found, return error
            if (serviceIds.isEmpty()) {
                setSessionMessage(request, "No services selected for booking.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
                return;
            }

            // ✅ Call Function to Process Booking
            boolean success = BookingList.createBookingWithPayment(
                 memberId, serviceIds, statusId, dateRequested, timeRequested, phoneNumber,
                 address, postalCode, specialRequest, amount, paymentMethodId, paymentDetails,
                 (paymentMethodId == 1) ? billingAddress : null,
                 (paymentMethodId == 1) ? billingPostalCode : null
            );

            // ✅ Handle Booking Success or Failure
            if (success) {
                setSessionMessage(request, "Booking successfully created.", "success");
                response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
            } else {
                setSessionMessage(request, "Booking creation failed. Try again later.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
            }

        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid number format: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        } catch (IllegalArgumentException e) {
            setSessionMessage(request, "Invalid date format. Please use YYYY-MM-DD.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/booking.jsp");
        } catch (Exception e) {
            setSessionMessage(request, "Error processing booking: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        }
    }

    // ✅ Extracts Service IDs from bookingCartStr
    private List<Integer> extractServiceIds(String bookingCartStr) {
        List<Integer> serviceIds = new ArrayList<>();
        if (bookingCartStr != null && !bookingCartStr.isEmpty()) {
            for (String serviceId : bookingCartStr.split(",")) {
                serviceIds.add(Integer.parseInt(serviceId.trim()));
            }
        }
        return serviceIds;
    }

    // ✅ Helper Function to Set Session Messages
    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
