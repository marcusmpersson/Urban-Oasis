package entities;
import javafx.scene.image.Image;

public class Deco extends ShopItem {
    public PlacementSlot placedAt;

    public Deco(String name, Image image, int price){
        super.name = name ;
        super.price = price;
        super.image = image;
    }
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }
    public void setPlacedAt(PlacementSlot placedAt) {
        this.placedAt = placedAt;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return this.price;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }



}
