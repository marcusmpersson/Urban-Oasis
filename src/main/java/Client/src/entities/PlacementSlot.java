package entities;

import enums.Environment;

import java.io.Serializable;

public class PlacementSlot implements Serializable {

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

    public void clear() {
        placedItem.setPlacedAt(null);
        this.placedItem = null;
    }
}
