package Services;
import java.util.ArrayList;
import java.util.List;

import Reviews.Review;

public class Service {
    private int id;
    private String name;
    private String description;
    private float estDuration;
    private float price;
    private int categoryId;
    private String imageUrl;
    private List<Review> reviews;  

    public Service(int id, String name, String description, float price, int categoryId, String imageUrl, float estDuration ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.estDuration = estDuration;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.reviews = new ArrayList<>();
    }
    
    public Service(String name, String description, float price, int categoryId, String imageUrl, float estDuration) {
        this.name = name;
        this.description = description;
        this.estDuration = estDuration;
        this.price = price;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getEstDuration() {
		return estDuration;
	}

	public void setEstDuration(float estDuration) {
		this.estDuration = estDuration;
	}

	public float getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }
    
    public String getImageUrl() {
    	return imageUrl;
    }
    
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    
}
