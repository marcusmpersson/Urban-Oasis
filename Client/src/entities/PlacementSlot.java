package entities;

import enums.Environment;

public class PlacementSlot {

    private Environment environment;
    private Placeable placedItem;

    public PlacementSlot(Environment environment){
        this.environment = environment;
    }

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
