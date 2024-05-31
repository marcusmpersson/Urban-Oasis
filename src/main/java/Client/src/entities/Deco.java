package entities;

import enums.DecoType;

import java.io.Serializable;

/** entity class represents a deco item in the game.
 * @author Rana Noorzadeh
 * @author Ingrid Merz*/
public class Deco extends ShopItem implements Placeable, Serializable {
    private PlacementSlot placedAt;
    private DecoType type;

    /** constructor assigns all instance variables */
    public Deco(String name, String imageFilePath, int price, String descriptionText, DecoType type) {
        super(imageFilePath, name, price, descriptionText);
        this.type = type;
    }

    /** returns the DecoType of this deco. */
    public DecoType getDecoType() {
        return type;
    }

    /** returns the placementSlot of this deco.
     * If not placed anywhere, returns null */
    @Override
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }

    /** sets the PlacementSlot of this deco. */
    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }

}
