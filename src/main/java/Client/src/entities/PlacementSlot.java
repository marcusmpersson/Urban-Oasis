package entities;

import enums.Environment;

import java.io.Serializable;

/** entity class represents a placement Slot in the player's room.
 * @author Rana Noorzadeh
 * */
public class PlacementSlot implements Serializable {

    private Environment environment;
    private Placeable placedItem;
    private int x;
    private int y;
    private boolean taken;

    /** constructor creates a new PlacementSlot with given environment and xy coordinates.
     * sets taken to false. */
    public PlacementSlot(Environment environment, int x, int y){
        this.environment = environment;
        this.x = x;
        this.y = y;
        this.taken = false;
    }

    /** returns the x coordinate of the slot*/
    public int getX(){return x;}

    /** returns the y coordinate of the slot*/
    public int getY() {return y;}

    /** returns the environment type of the slot*/
    public Environment getEnvironment() {
        return environment;
    }

    /** returns the item placed at the slot.
     * If no item is placed, this value will be null. */
    public Placeable getPlacedItem() {
        return placedItem;
    }

    /** returns a boolean representing if slot is taken or not */
    public boolean checkSlotTaken() {
        return this.taken;
    }

    /** sets a Placeable instance as the current placed item in this slot.
     * If the value is null, sets taken to false, otherwise sets to true. */
    public void setPlacedItem(Placeable placedItem) {
        this.placedItem = placedItem;

        if (placedItem != null) {
            this.taken = true;
            this.placedItem.setPlacedAt(this);
        } else {
            this.taken = false;
        }
    }

    /** method clears the slot from any placed items. sets taken to false. */
    public void clear() {
        placedItem.setPlacedAt(null);
        this.placedItem = null;
        this.taken = false;
    }
}
