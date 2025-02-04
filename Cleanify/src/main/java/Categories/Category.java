package Categories;

import Services.Service;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Service> services;

    // Constructor for new categories (without ID)
    public Category(String name, List<Service> services) {
        this.name = name;
        this.services = services;
    }

    // Constructor for existing categories (with ID)
    public Category(int id, String name, List<Service> services) {
        this.id = id;
        this.name = name;
        this.services = services;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Service> getServices() {
        return services;
    }

    // Setter for ID if you need to set it after insertion
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        StringBuilder servicesString = new StringBuilder();
        if (services != null && !services.isEmpty()) {
            for (Service service : services) {
                servicesString.append(service.toString()).append("\n");
            }
        } else {
            servicesString.append("No services available");
        }

        return "Category{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", services=\n" + servicesString.toString() +
               '}';
    }

}
