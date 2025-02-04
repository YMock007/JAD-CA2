package Saved;


public class Saved {
    private int savedId;
    private int serviceId;
    private String serviceName;
    private String serviceImageUrl;
    private String categoryName; 

    // Constructor with categoryName
    public Saved(int savedId, int serviceId, String serviceName, String serviceImageUrl, String categoryName) {
        this.savedId = savedId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceImageUrl = serviceImageUrl;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getSavedId() {
        return savedId;
    }

    public void setCartId(int savedId) {
        this.savedId = savedId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }

    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImageUrl = serviceImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}

