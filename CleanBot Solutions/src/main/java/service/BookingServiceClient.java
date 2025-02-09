package service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import model.BookingCategory;
import model.CompletedBookingDTO;

public class BookingServiceClient {

    private static final String CLEANIFY_API_URL = "http://localhost:8081/cleanify-ws/getAllBookings";

    public static List<BookingCategory> fetchAvailableBookings() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(CLEANIFY_API_URL);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

        List<BookingCategory> bookings = invocationBuilder.get(new GenericType<List<BookingCategory>>() {});
        client.close();
        return bookings;
    }
    
    public static List<BookingCategory> fetchAcceptedBookings(int workerId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8081/cleanify-ws/getAcceptedBookings/" + workerId);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        // ✅ Print Response JSON
        String jsonResponse = response.readEntity(String.class);
        System.out.println("🚀 API Response: " + jsonResponse); 

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return client.target("http://localhost:8081/cleanify-ws/getAcceptedBookings/" + workerId)
                         .request(MediaType.APPLICATION_JSON)
                         .get(new GenericType<List<BookingCategory>>() {});
        } else {
            return new ArrayList<>();
        }
    }
    
    
    
 
    
    public static List<CompletedBookingDTO> fetchCompletedBookings(int workerId) {
    	 Client client = ClientBuilder.newClient();
         WebTarget target = client.target("http://localhost:8081/cleanify-ws/getCompletedBookings/" + workerId);
         Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
         Response response = invocationBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<CompletedBookingDTO>>() {});
        } else {
            return new ArrayList<>();
        }
    }



    

}
