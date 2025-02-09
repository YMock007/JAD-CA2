package cleanify_ws.cleanify_ws.dto;

public class CompletedServiceDTO {
    private String name;
    private double price;

    public CompletedServiceDTO(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}