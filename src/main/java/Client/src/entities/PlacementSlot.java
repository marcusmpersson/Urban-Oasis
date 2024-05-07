package entities;

import enums.Environment;

import java.io.Serializable;

public class PlacementSlot implements Serializable {

    private Environment environment;
    private Placeable placedItem;
    private int x;
    private int y;
    private boolean taken;

    public PlacementSlot(Environment environment, int x, int y){
        this.environment = environment;
        this.x = x;
        this.y = y;
        this.taken = false;
    }

    public int getX(){return x;}

    public int getY() {return y;}

    public Environment getEnvironment() {
        return environment;
    }

    public Placeable getPlacedItem() {
        return placedItem;
    }

    public boolean checkSlotTaken() {
        return this.taken;
    }

    public void setPlacedItem(Placeable placedItem) {
        this.placedItem = placedItem;
        if (placedItem != null) {
            this.taken = true;
            placedItem.setPlacedAt(this);
        } else {
            this.taken = false;
        }
    }

    public void clear() {
        placedItem.setPlacedAt(null);
        this.placedItem = null;
        this.taken = false;
    }
}
