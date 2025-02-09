package service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import model.BookingCategory;

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
}
