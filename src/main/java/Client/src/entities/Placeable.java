package entities;

/** Interface Placeable represents all items that can be placed in the player's room. */
public interface Placeable {

    public void setPlacedAt(PlacementSlot slot);

    public PlacementSlot getPlacedAt();
}
