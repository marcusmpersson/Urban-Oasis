package entities;

/** abstract class ShopItem is the superclass of all purchasable game items
 * @author Rana Noorzadeh
 * @author Ingrid Merz */
public class ShopItem extends Item {

    public String imageFilePath;

    /** constructor assigns imageFilePath, name, price and description of the ShopItem. */
    public ShopItem(String imageFilePath, String name, int price, String descriptionText){
        super(price, descriptionText, name);
        this.imageFilePath = imageFilePath;
    }

    /** returns the image filepath of this ShopItem */
    public String getImageFilePath(){
        return this.imageFilePath;
    }

}
