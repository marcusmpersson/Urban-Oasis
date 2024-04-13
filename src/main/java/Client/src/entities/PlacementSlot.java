package entities;

import enums.Environment;

public class PlacementSlot {

    private Environment environment;
    private Placeable placedItem;
    private int x;
    private int y;

    public PlacementSlot(Environment environment, int x, int y){
        this.environment = environment;
        this.x = x;
        this.y = y;
    }

    public int getX(){return x;}

    public int getY() {return y;}

    public Environment getEnvironment() {
        return environment;
    }
    public Placeable getPlacedItem() {
        return placedItem;
    }
    public void setPlacedItem(Placeable placedItem) {
        this.placedItem = placedItem;
        placedItem.setPlacedAt(this);
    }
}