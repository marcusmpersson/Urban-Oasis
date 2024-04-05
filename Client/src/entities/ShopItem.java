package entities;
import javafx.scene.image.Image;

public class ShopItem extends Item {

    public Image image;
    public String name;

    public ShopItem(Image image, String name, int price){
        super(price);
        this.image = image;
        this.name = name;
    }

    public Image getImage(){
        return this.image;
    }

}
