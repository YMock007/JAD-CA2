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
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
            // ✅ Get payment details
        	String paymentMethodId = request.getParameter("paymentMethodId").trim(); // ✅ Remove spaces & newline
        	System.out.println("Received PaymentMethodId: " + paymentMethodId); // Debugging log
            String amountStr = request.getParameter("amount");
            String memberId = request.getParameter("memberId");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String postalCode = request.getParameter("postalCode");
            String specialRequest = request.getParameter("specialRequest");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");

            // ✅ Validate parameters
            if (paymentMethodId == null || paymentMethodId.isEmpty() || amountStr == null || amountStr.isEmpty() || memberId == null) {
                throw new IllegalArgumentException("Invalid payment request: Missing parameters.");
            }

            // ✅ Convert amount from dollars to cents
            long amount;
            try {
                amount = (long) (Double.parseDouble(amountStr) * 100); // Convert dollars to cents
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format. Amount must be a valid number.");
            }

            // ✅ Create PaymentIntent (For Card Payments)
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
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

            // ✅ If payment is successful, send `POST` request to `BookingServlet`
            if ("succeeded".equals(paymentIntent.getStatus())) {
                boolean bookingSuccess = createBooking(
                    request.getContextPath() + "/BookingServlet", memberId, phoneNumber, address, postalCode,
                    specialRequest, appointmentDate, appointmentTime
                );

                if (bookingSuccess) {
                    // ✅ Redirect user to bookings page
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("success", true);
                    jsonResponse.put("paymentIntentId", paymentIntent.getId());
                    jsonResponse.put("status", paymentIntent.getStatus());
                    jsonResponse.put("redirect_url", request.getContextPath() + "/views/member/booking/index.jsp");

                    out.print(jsonResponse.toString());
                } else {
                    throw new Exception("Booking creation failed.");
                }
            } else {
                throw new Exception("Payment was not successful.");
            }

        } catch (StripeException e) {
            sendErrorResponse(out, "Stripe Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            sendErrorResponse(out, "Invalid amount format.");
        } catch (Exception e) {
            sendErrorResponse(out, "Unexpected error: " + e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }

    // ✅ Function to Send `POST` Request to `BookingServlet`
    private boolean createBooking(String bookingUrl, String memberId, String phoneNumber, String address,
                                  String postalCode, String specialRequest, String appointmentDate, String appointmentTime) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL("http://localhost:8080" + bookingUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // ✅ Construct POST Data
            String postData = "memberId=" + memberId +
                    "&phoneNumber=" + phoneNumber +
                    "&address=" + address +
                    "&postalCode=" + postalCode +
                    "&specialRequest=" + specialRequest +
                    "&appointmentDate=" + appointmentDate +
                    "&appointmentTime=" + appointmentTime;

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

    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", errorMessage);
        out.print(errorResponse.toString());
    }
}
