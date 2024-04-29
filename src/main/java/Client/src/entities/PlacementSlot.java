package entities;

import enums.Environment;

public class PlacementSlot {

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
        this.taken = true;
        placedItem.setPlacedAt(this);
    }

    public void clear() {
        placedItem.setPlacedAt(null);
        this.placedItem = null;
        this.taken = false;
    }
}
