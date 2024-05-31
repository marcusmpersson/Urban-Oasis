package entities;

import java.io.Serializable;
import java.util.ArrayList;

/** entity class represents the player's room.
 * @author Rana Noorzadeh
 * */
public class Room implements Serializable {
    private ArrayList<PlacementSlot> slots;
    public ArrayList<String> imageFilePaths;

    /** constructor creates a room with given PlacementSlots and given image file-paths. */
    public Room(ArrayList<PlacementSlot> slots, ArrayList<String> imageFilePaths) {
        this.slots = slots;
        this.imageFilePaths = imageFilePaths;
    }

    /** returns the placement slot at given index.
     * returns null if index is out of bounds. */
    public PlacementSlot getSlot(int index) {
        if (index < slots.size()) {
            return slots.get(index);
        }
        return null;
    }

    /** method iterates the PlacementSlots of room and returns the first non-taken slot.
     * returns null if all slots are taken. */
    public PlacementSlot getNextAvailableSlot() {
        for (int i = 0; i < slots.size(); i++) {
            if (!slots.get(i).checkSlotTaken()) {
                return slots.get(i);
            }
        }
        return null;
    }

    /** returns a reference to the arraylist containing all PlacementSlots in the room */
    public ArrayList<PlacementSlot> getSlots() {
        return slots;
    }

    /** returns filepath to room's daytime image */
    public String getDaytimeFilepath (){
        return imageFilePaths.get(0);
    }

    /** returns filepath to room's sunset image */
    public String getSunsetFilepath (){
        return imageFilePaths.get(1);
    }

    /** returns filepath to room's night image */
    public String getNightFilepath (){
        return imageFilePaths.get(2);
    }

    /** returns filepath to room's sunrise image */
    public String getSunriseFilepath (){
        return imageFilePaths.get(3);
    }

    /** method returns an ArrayList of all placed items in the room */
    public ArrayList<Placeable> getPlacedItems() {
        ArrayList<Placeable> placedItems = new ArrayList<>();
        for (PlacementSlot slot : slots){
            placedItems.add(slot.getPlacedItem());
        }
        return placedItems;
    }

    /** returns a reference to the arraylist containing all image file-paths of the room */
    public ArrayList<String> getImageFilePaths() {
        return imageFilePaths;
    }

}
