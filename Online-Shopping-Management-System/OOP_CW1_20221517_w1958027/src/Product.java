public abstract class Product {
    private String productId;
    private String productName;
    private int availableItems;
    private double price;

    //* CONSTRUCTORS
    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    //* GETTERS AND SETTER
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product ID: "+ productId+ ", Name: " + productName + ", Available Items: " + availableItems + ", Price: " + price;
    }

    public String getProductCategory() {
        if (this instanceof Electronics)
            return "Electronics";
        else if (this instanceof Clothing)
            return "Clothing";
        else
            return "Not Found";

    }
}
