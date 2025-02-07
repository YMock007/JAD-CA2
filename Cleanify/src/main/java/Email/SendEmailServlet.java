package Email;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


@WebServlet("/SendEmailServlet")
public class SendEmailServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SENDER_EMAIL = "cleanifymakeyoushine@gmail.com"; 
    private static final String SENDER_PASSWORD = "fgmx nyzu goip asam"; 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String email = request.getParameter("email");
            String amountStr = request.getParameter("amount");
            String memberId = request.getParameter("memberId").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String address = request.getParameter("address").trim();
            String postalCode = request.getParameter("postalCode").trim();
            String specialRequest = request.getParameter("specialRequest");
            specialRequest = (specialRequest != null) ? specialRequest.trim() : "";
            String appointmentDate = request.getParameter("appointmentDate").trim();
            String appointmentTime = request.getParameter("appointmentTime").trim();
            String billingAddress = null;
            String billingPostalCode = null;
            String bookingCartStr = request.getParameter("bookingCart");
            
            List<Integer> serviceIds = extractServiceIds(bookingCartStr);
            
            long amount;
            try {
                amount = (long) (Double.parseDouble(amountStr)); 
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid amount format. Amount must be a valid number.");
            }
            if (email == null || email.isEmpty()) {
                response.getWriter().print("{\"success\": false, \"message\": \"Email address is required.\"}");
                return;
            }

            String qrData = "🔹 Cleanify - Payment Confirmation 🔹\n"
                          + "---------------------------------\n"
                          + "✅ Payment Received Successfully!\n"
                          + "💳 Amount Paid: S$" + (amount * 0.01) + "\n"
                          + "📌 Payment Method: QR Code Payment\n\n"
                          + "📅 Service Date: " + appointmentDate + "\n"
                          + "⏰ Service Time: " + appointmentTime + "\n\n"
                          + "🧹 Your cleaning appointment is now confirmed.\n"
                          + "🚀 We are assigning a professional cleaner to your booking.\n"
                          + "📢 You will receive an update with cleaner details soon!\n\n"
                          + "🙏 Thank you for choosing **Cleanify**!\n"
                          + "✨ We look forward to making your space shine.\n\n"
                          + "📞 Need help? Contact us: +65 1234 5678\n";

            BufferedImage qrImage = generateQRCodeImage(qrData);
            
            String messageBody = "Dear Customer,\n\n"
                    + "Thank you for choosing **Cleanify**! Your cleaning service payment request has been received.\n\n"
                    + "💳 **Payment Details:**\n"
                    + "✔ Amount: S$" + (amount * 0.01) + "\n"
                    + "✔ Method: QR Code Payment\n\n"
                    + "📅 **Appointment:**\n"
                    + "✔ Date: " + appointmentDate + "\n"
                    + "✔ Time: " + appointmentTime + "\n\n"
                    + "🔗 **To Complete Payment:**\n"
                    + "1️⃣ Open PayNow App\n"
                    + "2️⃣ Scan the attached QR code\n"
                    + "3️⃣ Confirm to secure your booking\n\n"
                    + "✅ **Next Steps:**\n"
                    + "Once paid, we’ll assign a cleaner and send you the details.\n\n"
                    + "📢 **Reminders:**\n"
                    + "🔹 Pay **at least 24 hours before** to confirm your slot.\n"
                    + "🔹 Need changes? Contact us **24 hours in advance**.\n\n"
                    + "📞 Support: +65 1234 5678 | ✉ cleanifymakeyoushine@gmail.com\n"
                    + "✨ **Cleanify – Making Your Space Shine!**";

            boolean emailSent = sendEmailWithQR(email, "Your Cleanify Payment Request – Secure Your Appointment!", messageBody, qrImage);
            if (emailSent) {
                // **Debugging: Print values before processing**
                System.out.println("DEBUG: Initial billingAddress = " + billingAddress);
                System.out.println("DEBUG: Initial billingPostalCode = " + billingPostalCode);

                // **Ensure billingAddress and billingPostalCode are not null**
                billingAddress = (billingAddress != null) ? billingAddress : "";
                billingPostalCode = (billingPostalCode != null) ? billingPostalCode : "";

                // **Debugging: Print values after null-checking**
                System.out.println("DEBUG: Final billingAddress = " + billingAddress);
                System.out.println("DEBUG: Final billingPostalCode = " + billingPostalCode);

                boolean bookingCreated = createBooking(
                        request.getContextPath() + "/BookingServlet", 
                        memberId, phoneNumber, address, postalCode, 
                        specialRequest, appointmentDate, appointmentTime, 
                        (UUID.randomUUID()).toString(), amount, billingAddress, billingPostalCode, bookingCartStr
                );

                if (bookingCreated) {
                    cleanUpSession(request.getSession(false), serviceIds);
                    
                    setSessionMessage(request, "Booking Confirmed", "success");

                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("success", true);
                    jsonResponse.put("status", bookingCreated);
                    jsonResponse.put("message", "Payment processed. Redirecting to booking...");
                    jsonResponse.put("redirect_url", request.getContextPath() + "/views/member/booking/index.jsp");

                    out.print(jsonResponse.toString());
                } else {
                    setSessionMessage(request, "Booking was not successful.", "error");
                    throw new Exception("Booking was not successful.");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage generateQRCodeImage(String data) throws Exception {
        int width = 500, height = 500;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
    
    @SuppressWarnings("unused")
	private void cleanUpSession(HttpSession session, List<Integer> serviceIds) {
        if (session != null) {
            @SuppressWarnings("unchecked")
			HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
            @SuppressWarnings("unchecked")
			HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");

            if (cart != null && booking != null) {
                serviceIds.forEach(serviceId -> {
                    cart.remove(serviceId);
                    booking.remove(serviceId);
                });
            }
        }
    }

    private boolean sendEmailWithQR(String toEmail, String subject, String messageBody, BufferedImage qrImage) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Convert BufferedImage to ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", baos);
            byte[] qrBytes = baos.toByteArray();

            // Email Body Part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageBody);

            // Fix: Use ByteArrayDataSource to properly attach image
            DataSource dataSource = new ByteArrayDataSource(qrBytes, "image/png");
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName("QRCode.png");

            // Combine Message & Attachment
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (MessagingException | java.io.IOException e) {
            e.printStackTrace();
            return false;
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

            // **Ensure no parameter is null**
            memberId = (memberId != null) ? memberId : "";
            phoneNumber = (phoneNumber != null) ? phoneNumber : "";
            address = (address != null) ? address : "";
            postalCode = (postalCode != null) ? postalCode : "";
            specialRequest = (specialRequest != null) ? specialRequest : "";
            appointmentDate = (appointmentDate != null) ? appointmentDate : "";
            appointmentTime = (appointmentTime != null) ? appointmentTime : "";
            paymentIntentId = (paymentIntentId != null) ? paymentIntentId : "";
            billingAddress = (billingAddress != null) ? billingAddress : "";
            billingPostalCode = (billingPostalCode != null) ? billingPostalCode : "";
            bookingCartStr = (bookingCartStr != null) ? bookingCartStr : "";

            // **Add Debugging Statements**
            System.out.println("DEBUG: memberId = " + memberId);
            System.out.println("DEBUG: phoneNumber = " + phoneNumber);
            System.out.println("DEBUG: address = " + address);
            System.out.println("DEBUG: postalCode = " + postalCode);
            System.out.println("DEBUG: specialRequest = " + specialRequest);
            System.out.println("DEBUG: appointmentDate = " + appointmentDate);
            System.out.println("DEBUG: appointmentTime = " + appointmentTime);
            System.out.println("DEBUG: paymentIntentId = " + paymentIntentId);
            System.out.println("DEBUG: amount = " + amount);
            System.out.println("DEBUG: billingAddress = " + billingAddress);
            System.out.println("DEBUG: billingPostalCode = " + billingPostalCode);
            System.out.println("DEBUG: bookingCartStr = " + bookingCartStr);

            // **Ensure encoding will not fail**
            String postData = "memberId=" + URLEncoder.encode(memberId, StandardCharsets.UTF_8.name()) +
                    "&phoneNumber=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8.name()) +
                    "&address=" + URLEncoder.encode(address, StandardCharsets.UTF_8.name()) +
                    "&postalCode=" + URLEncoder.encode(postalCode, StandardCharsets.UTF_8.name()) +
                    "&specialRequest=" + URLEncoder.encode(specialRequest, StandardCharsets.UTF_8.name()) +
                    "&appointmentDate=" + URLEncoder.encode(appointmentDate, StandardCharsets.UTF_8.name()) +
                    "&appointmentTime=" + URLEncoder.encode(appointmentTime, StandardCharsets.UTF_8.name()) +
                    "&paymentIntentId=" + URLEncoder.encode(paymentIntentId, StandardCharsets.UTF_8.name()) +
                    "&amount=" + amount +
                    "&paymentMethodId=2" + 
                    "&billingAddress=" + URLEncoder.encode(billingAddress, StandardCharsets.UTF_8.name()) +
                    "&billingPostalCode=" + URLEncoder.encode(billingPostalCode, StandardCharsets.UTF_8.name()) + 
                    "&bookingCart=" + URLEncoder.encode(bookingCartStr, StandardCharsets.UTF_8.name());

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/png");

        String qrData = "Your QR Code Data Here...";
        try {
            BufferedImage qrImage = generateQRCodeImage(qrData);
            ImageIO.write(qrImage, "PNG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}