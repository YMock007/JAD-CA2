package Bookings;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String paymentIntentId = request.getParameter("paymentIntentId");
            if (paymentIntentId == null || paymentIntentId.isEmpty()) {
                setSessionMessage(request, "Payment verification failed. Try again.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/booking/booking.jsp");
                return;
            }
            byte[] paymentDetails = paymentIntentId.getBytes(StandardCharsets.UTF_8);
            int memberId = Integer.parseInt(request.getParameter("memberId").trim());
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String address = request.getParameter("address").trim();
            String postalCode = request.getParameter("postalCode").trim();
            String specialRequest = request.getParameter("specialRequest") != null ? request.getParameter("specialRequest").trim() : "";
            String appointmentDateStr = request.getParameter("appointmentDate").trim();
            String appointmentTimeStr = request.getParameter("appointmentTime").trim();
            String billingAddress = request.getParameter("billingAddress");
            String billingPostalCode = request.getParameter("billingPostalCode");

            billingAddress = (billingAddress != null && !billingAddress.equals("")) ? billingAddress.trim() : "";
            billingPostalCode = (billingPostalCode != null && !billingPostalCode.equals("")) ? billingPostalCode.trim() : "";
            String bookingCartStr = request.getParameter("bookingCart");
            Date dateRequested = Date.valueOf(appointmentDateStr);
            Time timeRequested = (appointmentTimeStr != null && !appointmentTimeStr.isEmpty()) ?
                    Time.valueOf(appointmentTimeStr + ":00") : Time.valueOf("00:00:00");
            double amount = Double.parseDouble(request.getParameter("amount"));
            int paymentMethodId = Integer.parseInt(request.getParameter("paymentMethodId"));
            int statusId = 1;
            List<Integer> serviceIds = extractServiceIds(bookingCartStr);

            if (serviceIds.isEmpty()) {
                setSessionMessage(request, "No services selected for booking.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
                return;
            }

            boolean success = BookingList.createBookingWithPayment(
                    memberId, serviceIds, statusId, dateRequested, timeRequested, phoneNumber,
                    address, postalCode, specialRequest, amount, paymentMethodId, paymentDetails,
                    (paymentMethodId == 1) ? billingAddress : null,
                    (paymentMethodId == 1) ? billingPostalCode : null
            );

            if (success) {
                setSessionMessage(request, "Booking successfully created.", "success");
                response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
            } else {
                setSessionMessage(request, "Booking creation failed. Try again later.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
            }

        } catch (NumberFormatException e) {
            System.out.println("ERROR: NumberFormatException - " + e.getMessage());
            setSessionMessage(request, "Invalid number format: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: IllegalArgumentException - " + e.getMessage());
            setSessionMessage(request, "Invalid date format. Please use YYYY-MM-DD.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/booking.jsp");
        } catch (Exception e) {
            System.out.println("ERROR: General Exception - " + e.getMessage());
            e.printStackTrace();
            setSessionMessage(request, "Error processing booking: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
        }
    }


    private List<Integer> extractServiceIds(String bookingCartStr) {
        List<Integer> serviceIds = new ArrayList<>();
        
        if (bookingCartStr != null && !bookingCartStr.trim().isEmpty()) {
            try {
                if (bookingCartStr.startsWith("[") && bookingCartStr.endsWith("]")) {
                    Type listType = new TypeToken<List<Integer>>() {}.getType();
                    serviceIds = new Gson().fromJson(bookingCartStr, listType);
                } else {
                    serviceIds = Arrays.stream(bookingCartStr.split(","))
                                       .map(Integer::parseInt)
                                       .collect(Collectors.toList());
                }
            } catch (Exception e) {
                System.out.println("ERROR: Failed to parse bookingCartStr - " + e.getMessage());
            }
        }
        return serviceIds;
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
