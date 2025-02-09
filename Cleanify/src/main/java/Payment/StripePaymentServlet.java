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

        JSONObject jsonResponse = new JSONObject();

        try {
            // ✅ Retrieve and validate parameters
            String paymentMethodId = request.getParameter("paymentMethodId");
            String amountStr = request.getParameter("amount");
            String memberId = request.getParameter("memberId");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String postalCode = request.getParameter("postalCode");
            String specialRequest = request.getParameter("specialRequest");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");
            String billingAddress = request.getParameter("billingAddress");
            String billingPostalCode = request.getParameter("billingPostalCode");
            String bookingCartStr = request.getParameter("bookingCart");

            if (paymentMethodId == null || amountStr == null || memberId == null || bookingCartStr == null) {
                throw new IllegalArgumentException("Missing required parameters.");
            }

            long amount;
            try {
                amount = (long) (Double.parseDouble(amountStr));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format.");
            }

            // ✅ Convert bookingCartStr into List<Integer>
            List<Integer> serviceIds = extractServiceIds(bookingCartStr);
            if (serviceIds.isEmpty()) {
                throw new IllegalArgumentException("Invalid booking cart data.");
            }

            // ✅ Create a Payment Intent with Stripe
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
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Payment failed: " + paymentStatus);
                out.print(jsonResponse.toString());
                return;
            }

            // ✅ Create booking after successful payment
            boolean bookingCreated = createBooking(
            	request.getContextPath() + "/BookingServlet",
                memberId, phoneNumber, address, postalCode,
                specialRequest, appointmentDate, appointmentTime,
                paymentIntent.getId(), amount, billingAddress, billingPostalCode, bookingCartStr
            );

            if (!bookingCreated) {
                jsonResponse.put("success", false);
                jsonResponse.put("error", "Booking was not successful.");
                out.print(jsonResponse.toString());
                return;
            }
            cleanUpSession(request.getSession(false), serviceIds);
            // ✅ Payment & booking successful
            jsonResponse.put("success", true);
            jsonResponse.put("paymentIntentId", paymentIntent.getId());
            jsonResponse.put("message", "Payment processed successfully.");
            jsonResponse.put("redirect_url", request.getContextPath() + "/views/member/booking/index.jsp");
            out.print(jsonResponse.toString());

        } catch (StripeException e) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Stripe Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    // ✅ Extract Service IDs from bookingCartStr
    private List<Integer> extractServiceIds(String bookingCartStr) {
        List<Integer> serviceIds = new ArrayList<>();
        if (bookingCartStr != null && !bookingCartStr.isEmpty()) {
            try {
                for (String serviceId : bookingCartStr.split(",")) {
                    serviceIds.add(Integer.parseInt(serviceId.trim()));
                }
            } catch (Exception e) {
                System.out.println("ERROR: Failed to parse bookingCartStr - " + e.getMessage());
            }
        }
        return serviceIds;
    }
    
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

    // ✅ Create Booking with Debugging
    private boolean createBooking(String bookingUrl, String memberId, String phoneNumber, 
                                  String address, String postalCode, String specialRequest, 
                                  String appointmentDate, String appointmentTime, 
                                  String paymentIntentId, long amount, 
                                  String billingAddress, String billingPostalCode, String bookingCartStr) {
        try {
            String fullUrl = "http://localhost:8080" + bookingUrl;
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "memberId=" + URLEncoder.encode(memberId, StandardCharsets.UTF_8) +
                    "&phoneNumber=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8) +
                    "&address=" + URLEncoder.encode(address, StandardCharsets.UTF_8) +
                    "&postalCode=" + URLEncoder.encode(postalCode, StandardCharsets.UTF_8) +
                    "&specialRequest=" + URLEncoder.encode(specialRequest, StandardCharsets.UTF_8) +
                    "&appointmentDate=" + URLEncoder.encode(appointmentDate, StandardCharsets.UTF_8) +
                    "&appointmentTime=" + URLEncoder.encode(appointmentTime, StandardCharsets.UTF_8) +
                    "&paymentIntentId=" + URLEncoder.encode(paymentIntentId, StandardCharsets.UTF_8) +
                    "&amount=" + amount +
                    "&paymentMethodId=1" + 
                    "&billingAddress=" + URLEncoder.encode(billingAddress, StandardCharsets.UTF_8) +
                    "&billingPostalCode=" + URLEncoder.encode(billingPostalCode, StandardCharsets.UTF_8) +
                    "&bookingCart=" + URLEncoder.encode(bookingCartStr, StandardCharsets.UTF_8);

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("DEBUG: Booking Servlet Response Code = " + responseCode);

            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.out.println("ERROR: Booking request failed - " + e.getMessage());
            return false;
        }
    }
}
