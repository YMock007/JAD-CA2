package Services;

import jakarta.servlet.ServletException;	
import Utils.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import jakarta.servlet.ServletContext;
import java.io.File;

@WebServlet("/serviceServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serviceId = request.getParameter("service_id");

        if (serviceId != null && !serviceId.isEmpty() && serviceId.matches("\\d+")) {
            try {
                int serviceIdInt = Integer.parseInt(serviceId);

                Service service = ServiceList.getServiceById(serviceIdInt);
                
                if (service != null) {
                	HttpSession session = request.getSession();
                    session.setAttribute("service", service);
                    session.setAttribute("activeServiceId", serviceId);
                    response.sendRedirect(request.getContextPath() + "/views/member/services/serviceDetails/index.jsp");
                } else {
                    setSessionMessage(request, "Service not found.", "error");
                    response.sendRedirect(request.getContextPath() + "/views/member/services/index.jsp");
                }
            } catch (NumberFormatException e) {
                setSessionMessage(request, "Invalid service ID format.", "error");
                response.sendRedirect(request.getContextPath() + "/views/member/services/index.jsp");
            }
        } else {
            setSessionMessage(request, "Only numeric service ID is allowed.", "error");
            response.sendRedirect(request.getContextPath() + "/views/member/services/index.jsp");
        }
    }

    
  

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            setSessionMessage(request, "No action specified.", "error");
            redirectToDashboard(response, request);
            return;
        }

        try {
            switch (action) {
                case "add":
                    addService(request, response);
                    break;
                case "update":
                    updateService(request, response);
                    break;
                case "delete":
                    deleteService(request, response);
                    break;
                default:
                    setSessionMessage(request, "Unknown action.", "error");
                    redirectToDashboard(response, request);
            }
        } catch (Exception e) {
            setSessionMessage(request, "Operation failed: " + e.getMessage(), "error");
            redirectToDashboard(response, request);
        }
    }

    private void addService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");
        String estDurationStr = request.getParameter("estDuration");

        if (isNullOrEmpty(name, description, priceStr, categoryIdStr)) {
            setSessionMessage(request, "Invalid service data. Please fill in all required fields.", "error");
            redirectToDashboard(response, request);
            return;
        }

        // Check if a service with the same name already exists
        if (ServiceList.getServiceByName(name) != null) {
            setSessionMessage(request, "A service with the name '" + name + "' already exists.", "error");
            redirectToDashboard(response, request);
            return;
        }

        Part filePart = request.getPart("image");
        String imageUrl;

        try {
            if (filePart == null || filePart.getSize() == 0) {
                // Use the default image if no file is provided
                imageUrl = "https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/DefaultPicture.png";
            } else {
                imageUrl = FileUploadHelper.uploadToCloudinary(filePart);
            }
        } catch (IOException e) {
            setSessionMessage(request, "Image upload failed: " + e.getMessage(), "error");
            redirectToDashboard(response, request);
            return;
        }

        try {
            float price = Float.parseFloat(priceStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            float estDuration = Float.parseFloat(estDurationStr);
            Service service = new Service(name, description, price, categoryId, imageUrl, estDuration);

            boolean success = ServiceList.addService(service);
            if (success) {
                setSessionMessage(request, "Service '" + name + "' added successfully.", "success");
            } else {
                setSessionMessage(request, "Failed to add service. Please try again.", "error");
            }
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid price format.", "error");
        }

        redirectToDashboard(response, request);
    }

    private void updateService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idStr = request.getParameter("serviceId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");
        String estDurationStr = request.getParameter("estDuration");

        if (isNullOrEmpty(idStr, name, description, priceStr, categoryIdStr)) {
            setSessionMessage(request, "Invalid service data. Please fill in all required fields.", "error");
            redirectToDashboard(response, request);
            return;
        }

        int id = Integer.parseInt(idStr);
        Service existingService = ServiceList.getServiceById(id);
        if (existingService == null) {
            setSessionMessage(request, "Service not found.", "error");
            redirectToDashboard(response, request);
            return;
        }

        Part filePart = request.getPart("image");
        String imageUrl;

        try {
            if (filePart == null || filePart.getSize() == 0) {
                // Use the default image if no file is uploaded
                imageUrl = "https://res.cloudinary.com/dr7rxzsgz/image/upload/v1738572872/cleaning_service/DefaultPicture.png";
            } else {
                // Upload file to Cloudinary
                imageUrl = FileUploadHelper.uploadToCloudinary(filePart);
            }
        } catch (IOException e) {
            setSessionMessage(request, "Image upload failed: " + e.getMessage(), "error");
            redirectToDashboard(response, request);
            return;
        }


        redirectToDashboard(response, request);
    }

    private void deleteService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("serviceId");

        if (isNullOrEmpty(idStr)) {
            setSessionMessage(request, "Service ID is required for deletion.", "error");
            redirectToDashboard(response, request);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Service existingService = ServiceList.getServiceById(id);

            if (existingService == null) {
                setSessionMessage(request, "Service not found.", "error");
                redirectToDashboard(response, request);
                return;
            }

            // Delete the associated image
            if (!existingService.getImageUrl().equals("/resources/serviceImg/DefaultPicture.png")) {
                FileUploadHelper.deleteFromCloudinary(existingService.getImageUrl());
            }

            boolean success = ServiceList.deleteService(id);
            if (success) {
                setSessionMessage(request, "Service deleted successfully.", "success");
            } else {
                setSessionMessage(request, "Failed to delete service. Please try again.", "error");
            }
        } catch (NumberFormatException e) {
            setSessionMessage(request, "Invalid service ID format.", "error");
        }

        redirectToDashboard(response, request);
    }

    private void setSessionMessage(HttpServletRequest request, String message, String status) {
        HttpSession session = request.getSession();
        session.setAttribute("message", message);
        session.setAttribute("status", status);
    }

    private boolean isNullOrEmpty(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void redirectToDashboard(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.sendRedirect(request.getContextPath() + "/views/admin/dashboard/managing/categories-services/index.jsp");
    }
}
