package entities;

import java.util.ArrayList;

public class Room {
    private ArrayList<PlacementSlot> slots;


    public Room(ArrayList<PlacementSlot> slots){
        this.slots = slots;
    }
    public PlacementSlot getSlot(int index){
        if (index < slots.size()) {
            return slots.get(index);
        }
        return null;
    }

    /** places the given placeable item at given plcaementslot index, returns the item that was
     * placed in the slot prior, returns null if slot was empty */
    public Placeable placeItemIn(Placeable placeableItem, int index){
        if (index < slots.size()) {
            Placeable swappingItem = slots.get(index).getPlacedItem();
            slots.get(index).setPlacedItem(placeableItem);
            return swappingItem;
        }
        return null;
    }

}
