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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.setContentType("application/json");

        try {
            String email = request.getParameter("email");
            String amountStr = request.getParameter("amount");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");
            long amount = (long) (Double.parseDouble(amountStr)); 
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
            response.getWriter().print("{\"success\": " + emailSent + ", \"message\": \"" + (emailSent ? "Email sent successfully." : "Failed to send email.") + "\"}");
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