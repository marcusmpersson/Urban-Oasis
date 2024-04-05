package entities;
import javafx.scene.image.Image;

public class Deco extends ShopItem implements Placeable {
    public PlacementSlot placedAt;

    public Deco(String name, Image image, int price){
        super(image, name, price);
    }
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }
    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }


    public void setPrice(int price) {
        this.price = price;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }



}
