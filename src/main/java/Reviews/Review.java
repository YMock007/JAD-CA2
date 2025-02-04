package Reviews;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Review {
    private int id;
    private int rating;
    private String content;
    private Timestamp dateCreated;
    private int bookingId;

    public Review(int id, int rating, String content, Timestamp dateCreated, int bookingId) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.dateCreated = dateCreated;
        this.bookingId = bookingId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getFormattedDateCreated() {
        if (dateCreated != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(dateCreated);
        }
        return null;
    }


}
