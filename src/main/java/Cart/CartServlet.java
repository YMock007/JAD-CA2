package Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CartServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        String serviceIdParam = request.getParameter("serviceId");
        
        if (serviceIdParam == null || serviceIdParam.isEmpty()) {
            setSessionMessage(request, "Invalid service ID.", "error");
            response.sendRedirect(request.getHeader("Referer"));
            return;
        }

        int serviceId = -1;
        try {
            serviceId = Integer.parseInt(serviceIdParam);
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Service ID is not a valid number.", "error");
            response.sendRedirect(request.getHeader("Referer"));
            return;
        }

        String bookNowParam = request.getParameter("bookNow");
        boolean bookNow = Boolean.parseBoolean(bookNowParam);

        HttpSession session = request.getSession();

        // Initialize cart if it's not already present
        HashMap<Integer, Integer> cart = (HashMap<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        // Initialize booking if it's not already present
        HashMap<Integer, Integer> booking = (HashMap<Integer, Integer>) session.getAttribute("booking");
        if (booking == null) {
            booking = new HashMap<>();
            session.setAttribute("booking", booking);
        }

        if (bookNow) {
            if ("add".equals(action)) {
                if (!booking.containsKey(serviceId)) {
                    booking.put(serviceId, 1);
                    cart.put(serviceId, 1);
                    setSessionMessage(request, "Leading to collect booking details...", "info");
                    response.sendRedirect(request.getContextPath() + "/views/member/cart/index.jsp"); 
                } else {
                    setSessionMessage(request, "This service has already been added, but the user details are still pending", "info");
                    response.sendRedirect(request.getContextPath() + "/views/member/cart/index.jsp"); 
                }
            } else if ("remove".equals(action)) {
                // Remove from booking
                if (booking.containsKey(serviceId)) {
                    booking.remove(serviceId);
                    setSessionMessage(request, "Service removed from booking", "success");
                } else {
                    setSessionMessage(request, "Service not found in booking", "error");
                }
                response.sendRedirect(request.getHeader("Referer"));
            } else {
                setSessionMessage(request, "Invalid action for booking.", "error");
                response.sendRedirect(request.getHeader("Referer"));
            }
        } else {
            // Handling cart actions
            if ("add".equals(action)) {
                if (!cart.containsKey(serviceId)) {
                    cart.put(serviceId, 1);
                    setSessionMessage(request, "Service added to cart", "success");
                } else {
                    setSessionMessage(request, "Service is already in the cart.", "info");
                }
            } else if ("remove".equals(action)) {
                if (cart.containsKey(serviceId)) {
                    cart.remove(serviceId);
                    setSessionMessage(request, "Service removed from cart", "success");
                } else {
                    setSessionMessage(request, "Service not found in cart", "error");
                }
            } else {
                setSessionMessage(request, "Invalid action for cart.", "error");
            }
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
