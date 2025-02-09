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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        	setSessionMessage(request, "Processing...Please wait for a while...", "success");
            String email = request.getParameter("email");
            String amountStr = request.getParameter("totalPrice");
            String memberId = request.getParameter("memberId").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String address = request.getParameter("address").trim();
            String postalCode = request.getParameter("postalCode").trim();
            String specialRequest = request.getParameter("specialRequest");
            specialRequest = (specialRequest != null) ? specialRequest.trim() : "";
            String appointmentDate = request.getParameter("appointmentDate").trim();
            String appointmentTime = request.getParameter("appointmentTime").trim();
            String billingAddress = "";
            String billingPostalCode = "";
            String bookingCartStr = request.getParameter("bookingCart");

            List<Integer> serviceIds = new ArrayList<>();
            if (bookingCartStr != null && !bookingCartStr.trim().isEmpty()) {
                try {
                    Type listType = new TypeToken<List<Integer>>() {}.getType();
                    serviceIds = new Gson().fromJson(bookingCartStr, listType);
                } catch (Exception e) {
                    System.out.println("ERROR: Failed to parse serviceIdsJson - " + e.getMessage());
                }
            }

            long amount = 0;
            try {
                amount = (long) (Double.parseDouble(amountStr)); 
            } catch (NumberFormatException e) {
            	setSessionMessage(request, "Invalid amount format. Amount must be a valid number.", "error");
            }
            if (email == null || email.isEmpty()) {
                setSessionMessage(request, "{\"success\": false, \"message\": \"Email address is required.\"}", "error");
                return;
            }

            String qrData = "🔹 Cleanify - Payment Confirmation 🔹\n"
                          + "---------------------------------\n"
                          + "✅ Payment Received Successfully!\n"
                          + "💳 Amount Paid: S$" + amountStr + "\n"
                          + "📌 Payment Method: QR Code Payment\n\n"
                          + "📅 Service Date: " + appointmentDate + "\n"
                          + "⏰ Service Time: " + appointmentTime + "\n\n"
                          + "🧹 Your cleaning appointment is now confirmed.\n"
                          + "🚀 We are assigning a professional cleaner to your booking.\n"
                          + "📢 You will receive an update with cleaner details soon!\n\n"
                          + "🙏 Thank you for choosing **Cleanify**!\n"
                          + "✨ We look forward to making your space shine.\n\n";

            BufferedImage qrImage = generateQRCodeImage(qrData);
            
            String messageBody = "Dear Customer,\n\n"
                    + "Thank you for choosing **Cleanify**! Your cleaning service payment request has been received.\n\n"
                    + "💳 **Payment Details:**\n"
                    + "✔ Amount: S$" + amountStr + "\n"
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
            	setSessionMessage(request, "Processing...Please wait for a while...", "success");
                boolean bookingCreated = createBooking(
                        request.getContextPath() + "/BookingServlet", 
                        memberId, phoneNumber, address, postalCode, 
                        specialRequest, appointmentDate, appointmentTime, 
                        (UUID.randomUUID()).toString(), amount, billingAddress, billingPostalCode, bookingCartStr
                );

                if (bookingCreated) {
                    cleanUpSession(request.getSession(false), serviceIds);
                    
                    setSessionMessage(request, "Booking Confirmed", "success");
                    response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
                } else {
                    setSessionMessage(request, "Booking was not successful.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/member/cart.jsp");
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
            System.out.println(url);
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
                    "&paymentMethodId=2" + 
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