package Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostalCodeLookup {
    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    // Function to get Neighborhood and Country
    public static String getLocationDetails(String postalCode) {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            // Replace with your API Key
            String apiKey = ConfigLoader.get("POSTAL_CODE_LOOK_UP_API_KEY");
            URL url = new URL(API_URL + "?address=" + postalCode + "&components=country:SG&key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Check HTTP response
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Unknown Location";
            }

            // Read response
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode resultsArray = rootNode.get("results");

            if (resultsArray != null && resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode result = resultsArray.get(0);

                // Get Address Components
                String neighborhood = "";
                String country = "";
                for (JsonNode component : result.get("address_components")) {
                    JsonNode types = component.get("types");
                    if (types.toString().contains("\"neighborhood\"")) {
                        neighborhood = component.get("long_name").asText();
                    }
                    if (types.toString().contains("\"country\"")) {
                        country = component.get("short_name").asText();
                    }
                }

                // Combine neighborhood and country
                if (!neighborhood.isEmpty() && !country.isEmpty()) {
                    return neighborhood + ", " + country;
                }
            }

            return "Unknown Location";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Location";
        } finally {
            // Close resources
            try {
                if (br != null) br.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Function to get Latitude and Longitude
    public static String getLatLng(String postalCode) {
        HttpURLConnection conn = null;
        BufferedReader br = null;

        try {
            // Replace with your API Key
            String apiKey = ConfigLoader.get("POSTAL_CODE_LOOK_UP_API_KEY");
            URL url = new URL(API_URL + "?address=" + postalCode + "&components=country:SG&key=" + apiKey);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Check HTTP response
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Unknown Coordinates";
            }

            // Read response
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode resultsArray = rootNode.get("results");

            if (resultsArray != null && resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode locationNode = resultsArray.get(0).get("geometry").get("location");
                double lat = locationNode.get("lat").asDouble();
                double lng = locationNode.get("lng").asDouble();
                return lat + "," + lng;
            }

            return "Unknown Coordinates";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Coordinates";
        } finally {
            // Close resources
            try {
                if (br != null) br.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
