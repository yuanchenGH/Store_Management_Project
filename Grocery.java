// --== CS400 File Header Information ==--
// Name: Alexander Peseckis
// Email: peseckis@wisc.edu
// Team: DC
// Role: Data Wrangler
// TA: Yulan BAO
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

/**
 * A public class to hold the data about each Grocery Store Item.
 */
public class Grocery {

    /**
     * An Enum showing what category the Grocery is a part of.
     */
    enum Category {
        CLOTHING,
        FURNITURE,
        SCHOOLSUPPLIES,
        GAMES,
        FOOD
    }

    private String productName;
    private int quantity;
    private double price; // In US dollars
    private Category category;

    /**
     * A constructor that constructs the Grocery item.
     */
    public Grocery(String productName, int quantity, double price, Category category) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    //
    // Getter methods

    /**
     * Gets the productName.
     * @return the productName
     */
    public String getProductName() {
        return this.productName;
    }

    /**
     * Gets the quantity
     * @return the quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Gets the price.
     * @return the price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Gets the category.
     * @return the category
     */
    public Category getCategory() {
        return this.category;
    }

    //
    // Setters

    /**
     * Sets the productName.
     * @param newProductName the new productName
     */
    public void setProductName(String newProductName) {
        this.productName = newProductName;
    }

    /**
     * Sets the quantity.
     * @param newQuantity the new quantity
     */
    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * Sets the price.
     * @param newPrice the new price
     */
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    /**
     * Sets the category.
     * @param newCategory the new category
     */
    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }
}















