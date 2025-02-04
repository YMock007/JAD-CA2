package Reviews;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/reviewServlet")
public class ReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            setSessionMessage(request, "No action specified.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
            return;
        }

        try {
            switch (action) {
                case "create":
                    createReview(request, response);
                    break;
                case "update":
                    updateReview(request, response);
                    break;
                case "delete":
                    deleteReview(request, response);
                    break;
                default:
                    setSessionMessage(request, "Unknown action.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/user/reviews/index.jsp");
            }
        } catch (Exception e) {
            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/user/reviews/index.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            setSessionMessage(request, "No action specified.", "error");
            response.sendRedirect(request.getContextPath() + "/views/user/reviews/index.jsp");
            return;
        }

        try {
            switch (action) {
                case "getByUserId":
                    getReviewsByUserId(request, response);
                    break;
                case "getById":
                    getReviewById(request, response);
                    break;
                default:
                    setSessionMessage(request, "Unknown action.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
            }
        } catch (Exception e) {
            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
        }
    }

    private void createReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int rating = Integer.parseInt(request.getParameter("rating"));
        String content = request.getParameter("reviewContent");
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        Review review = new Review(0, rating, content, null, bookingId);

        try {
            boolean success = ReviewList.createReview(review);

            if (success) {
                setSessionMessage(request, "Review added successfully.", "success");
            } else {
                setSessionMessage(request, "Review already exists for this booking.", "error");
            }
        } catch (SQLException e) {
            setSessionMessage(request, "Failed to add review: " + e.getMessage(), "error");
        }

        response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
    }

    private void updateReview(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String content = request.getParameter("content");

        Review review = new Review(id, rating, content, null, 0);
        if (ReviewList.updateReview(review)) {
            setSessionMessage(request, "Review updated successfully.", "success");
        } else {
            setSessionMessage(request, "Failed to update review.", "error");
        }
        response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));

        if (ReviewList.deleteReview(id)) {
            setSessionMessage(request, "Review deleted successfully.", "success");
        } else {
            setSessionMessage(request, "Failed to delete review.", "error");
        }
        response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
    }

    private void getReviewById(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Review review = ReviewList.getReviewById(id);

        if (review != null) {
            request.getSession().setAttribute("review", review);
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
        } else {
            setSessionMessage(request, "Review not found.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
        }
    }

    private void getReviewsByUserId(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        List<Review> reviews = ReviewList.getReviewsByUserId(userId);

        if (reviews != null) {
            request.getSession().setAttribute("reviews", reviews);
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
        } else {
            setSessionMessage(request, "No reviews found for this user.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/booking/index.jsp");
        }
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }
}
