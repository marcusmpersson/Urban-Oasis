package entities;

/** abstract class Item is the superclass of all game items in the player's room, inventory and shop
 * @author Rana Noorzadeh
 * @author Ingrid Merz */
public abstract class Item {
    protected int price;
    protected String descriptionText;
    protected String name;

    /** constructor assigns price and descriptive text of the item */
    public Item (int price, String descriptionText, String name){
        this.price = price;
        this.descriptionText = descriptionText;
        this.name = name;
    }

    /** returns price of the item */
    public int getPrice(){
        return this.price;
    }

    /** returns description of the item */
    public String getDescriptionText() {
        return descriptionText;
    }

    /** returns the name of this Item */
    public String getName(){
        return this.name;
    }

}
