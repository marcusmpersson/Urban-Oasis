package entities;

/** abstract class Item is the superclass of all game items in the player's room, inventory and shop */
public abstract class Item {
    public int price;
    public String descriptionText;

    /** constructor assigns price and descriptive text of the item */
    public Item (int price, String descriptionText) {
        this.price = price;
        this.descriptionText = descriptionText;
    }

    /** returns price of the item */
    public int getPrice() {
        return this.price;
    }

    /** returns description of the item */
    public String getDescriptionText() {
        return descriptionText;
    }

}
