package entities;

import java.io.Serializable;

public class Deco extends ShopItem implements Placeable, Serializable {
    public PlacementSlot placedAt;

    public Deco(String name, String imageFilePath, int price, String descriptionText){
        super(imageFilePath, name, price, descriptionText);
    }
    @Override
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
