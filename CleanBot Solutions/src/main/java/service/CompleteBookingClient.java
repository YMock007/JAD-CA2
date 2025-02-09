package service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.UpdateBookingDTO;

public class CompleteBookingClient {

    private static final String CLEANIFY_API_URL = "http://localhost:8081/cleanify-ws/completeBooking";

    public static boolean completeBooking(UpdateBookingDTO updateBookingDTO) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(CLEANIFY_API_URL);
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        System.out.println("Complete booking client");
        System.out.println(updateBookingDTO.getBookingId());
        System.out.println(updateBookingDTO.getProviderId());
        System.out.println(updateBookingDTO.getStatusId());

        // ✅ Print UpdateBookingDTO in text format
        System.out.println("📌 Sending Booking Update Request:");
        System.out.println(updateBookingDTO); // This now prints nicely formatted text

        Response response = invocationBuilder.put(Entity.entity(updateBookingDTO, MediaType.APPLICATION_JSON));

        System.out.println("✅ Response Code: " + response.getStatus());  // ✅ Print response code
        System.out.println("📩 Response Body: " + response.readEntity(String.class)); // ✅ Print response body

        boolean success = response.getStatus() == Response.Status.OK.getStatusCode();
        client.close();
        return success;
    }
}
