package Payment;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

@WebServlet("/StripePaymentServlet")
public class StripePaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String STRIPE_SECRET_KEY = "sk_test_51QopKmLLgHtQcF9Hm28gVOWOzeVhXW1LY5VqgFtP9kN7rOIenqCw23jOMQEIZpYClTiDUdWOiaMb0sZmHGDgq5E500GrPu6RPg";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Stripe.apiKey = STRIPE_SECRET_KEY;

        try {
            String paymentMethodId = request.getParameter("paymentMethodId").trim();
            String amountStr = request.getParameter("amount");
            String memberId = request.getParameter("memberId").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String address = request.getParameter("address").trim();
            String postalCode = request.getParameter("postalCode").trim();
            String specialRequest = request.getParameter("specialRequest");
            specialRequest = (specialRequest != null) ? specialRequest.trim() : "";
            String appointmentDate = request.getParameter("appointmentDate").trim();
            String appointmentTime = request.getParameter("appointmentTime").trim();
            String billingAddress = request.getParameter("billingAddress").trim();
            String billingPostalCode = request.getParameter("billingPostalCode").trim();
            String bookingCartStr = request.getParameter("bookingCart");

            List<Integer> serviceIds = extractServiceIds(bookingCartStr);
            System.out.println(serviceIds);

            if (paymentMethodId.isEmpty() || amountStr.isEmpty() || memberId.isEmpty()) {
                throw new IllegalArgumentException("Invalid payment request: Missing parameters.");
            }

            long amount;
            try {
                amount = (long) (Double.parseDouble(amountStr)); 
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format. Amount must be a valid number.");
            }

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("sgd")
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                        .build()
                )
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String paymentStatus = paymentIntent.getStatus();
            if (!"succeeded".equals(paymentStatus)) {
                setSessionMessage(request, "Payment failed: " + paymentStatus, "error");
                response.sendRedirect(request.getContextPath() + "/views/member/cart/cart.jsp");
                return;
            } 
            setSessionMessage(request, "Payment Successful: " + paymentStatus, "success");
            boolean bookingCreated = createBooking(
                request.getContextPath() + "/BookingServlet", 
                memberId, phoneNumber, address, postalCode, 
                specialRequest, appointmentDate, appointmentTime, 
                paymentIntent.getId(), amount, billingAddress, billingPostalCode, bookingCartStr
            );

            if (bookingCreated) {
                cleanUpSession(request.getSession(false), serviceIds);
                
                setSessionMessage(request, "Booking Confirmed", "success");

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", true);
                jsonResponse.put("paymentIntentId", paymentIntent.getId());
                jsonResponse.put("status", paymentIntent.getStatus());
                jsonResponse.put("message", "Payment processed. Redirecting to booking...");
                jsonResponse.put("redirect_url", request.getContextPath() + "/views/member/booking/index.jsp");

                out.print(jsonResponse.toString());
            } else {
                setSessionMessage(request, "Booking was not successful.", "error");
                throw new Exception("Booking was not successful.");
            }

        } catch (StripeException e) {
            setSessionMessage(request, "Stripe Error: " + e.getMessage(), "error");
        } catch (Exception e) {
            setSessionMessage(request, "Unexpected error: " + e.getMessage(), "error");
        } finally {
            out.flush();
            out.close();
        }
    }

    @SuppressWarnings("deprecation")
    private boolean createBooking(String bookingUrl, String memberId, String phoneNumber, String address,
                                  String postalCode, String specialRequest, String appointmentDate, 
                                  String appointmentTime, String paymentIntentId, long amount, 
                                  String billingAddress, String billingPostalCode, String bookingCartStr) {
        try {
            String fullUrl = "http://localhost:8080" + bookingUrl;  
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "memberId=" + URLEncoder.encode(memberId, "UTF-8") +
                    "&phoneNumber=" + URLEncoder.encode(phoneNumber, "UTF-8") +
                    "&address=" + URLEncoder.encode(address, "UTF-8") +
                    "&postalCode=" + URLEncoder.encode(postalCode, "UTF-8") +
                    "&specialRequest=" + URLEncoder.encode(specialRequest, "UTF-8") +
                    "&appointmentDate=" + URLEncoder.encode(appointmentDate, "UTF-8") +
                    "&appointmentTime=" + URLEncoder.encode(appointmentTime, "UTF-8") +
                    "&paymentIntentId=" + URLEncoder.encode(paymentIntentId, "UTF-8") +
                    "&amount=" + amount +
                    "&paymentMethodId=1" + 
                    "&billingAddress=" + URLEncoder.encode(billingAddress, "UTF-8") +
                    "&billingPostalCode=" + URLEncoder.encode(billingPostalCode, "UTF-8") + 
                    "&bookingCart=" + URLEncoder.encode(bookingCartStr, "UTF-8");

            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            OutputStream os = conn.getOutputStream();
            os.write(postDataBytes);
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @SuppressWarnings("unused")
	private void cleanUpSession(HttpSession session, List<Integer> serviceIds) {
        if (session != null) {
            HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
            HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");

            if (cart != null && booking != null) {
                serviceIds.forEach(serviceId -> {
                    cart.remove(serviceId);
                    booking.remove(serviceId);
                });
            }
        }
    }


    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", errorMessage);
        out.print(errorResponse.toString());
    }
    
    
    
    private List<Integer> extractServiceIds(String bookingCartStr) {
        List<Integer> serviceIds = new ArrayList<>();
        if (bookingCartStr != null && !bookingCartStr.isEmpty()) {
            for (String serviceId : bookingCartStr.split(",")) {
                serviceIds.add(Integer.parseInt(serviceId.trim()));
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
