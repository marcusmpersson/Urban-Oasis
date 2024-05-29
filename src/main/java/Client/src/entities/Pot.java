package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.PotType;

import java.io.Serializable;

public class Pot extends ShopItem implements Placeable, Serializable {

    @SerializedName("PotType")
    @Expose
    private PotType type;
    private PlacementSlot placedAt;

    /** constructor assigns imageFilePath, name, price, type, and description of the Pot. */
    public Pot (String name, String imageFilePath, int price, PotType type, String descriptionText) {
        super(imageFilePath, name, price, descriptionText);
        this.type = type;
    }

    /** returns the PotType of this Pot. */
    public PotType getPotType() {
        return type;
    }

    /** returns the placementSlot of this Pot.
     * If not placed anywhere, returns null */
    @Override
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }

    /** sets the PlacementSlot of this Pot. */
    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }

}
