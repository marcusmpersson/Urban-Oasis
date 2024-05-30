package entities;

import enums.Stage;

import java.io.Serializable;
import java.util.ArrayList;

public class PottedPlant extends Item implements Placeable, Serializable {

    private Pot pot;
    private PlantTop plant;
    private PlacementSlot placedAt;

    /** constructor creates a PottedPlant with given Pot and PlantTop instances. */
    public PottedPlant(Pot pot, PlantTop plant){
        super(pot.getPrice() + plant.getPrice(), plant.getDescriptionText(), plant.getName());
        this.pot = pot;
        this.plant = plant;
    }

    // ------------------------------------------
    // SETTERS
    // ------------------------------------------

    /** sets the PlacementSlot of this potted plant. */
    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }

    // ------------------------------------------
    // GETTERS
    // ------------------------------------------

    /** returns the placementSlot of this potted plant.
     * If not placed anywhere, returns null */
    @Override
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }

    /** method re-calculates the total price of PottedPlant and returns it */
    @Override
    public int getPrice() {
        price = plant.getPrice() + pot.getPrice();
        return price;
    }

    /** returns the description text of this PottedPlant
     * (is the same as description text of its plant top) */
    @Override
    public String getDescriptionText() {
        this.descriptionText = plant.getDescriptionText();
        return descriptionText;
    }

    /** returns reference to the Pot instance in this PottedPlant */
    public Pot getPot() {
        return pot;
    }

    /** returns reference to the PlantTop instance in this PottedPlant */
    public PlantTop getPlantTop() {
        return plant;
    }

    // ------------------------------------------
    // MOST LIKELY WILL NEVER BE USED:
    // ------------------------------------------

    /** method returns an arraylist containing the plantTop image (index 0)
     * and pot image (index 1) */
    public ArrayList<String> getImagesFilePaths(){
        ArrayList<String> filePathArray = new ArrayList<>();
        filePathArray.add(plant.getImageFilePath());
        filePathArray.add(pot.getImageFilePath());

        return filePathArray;
    }

    /** returns plant name */
    public String getName(){
        if (this.plant.getStage() == Stage.PLANTED){
            return "Mystery Plant";
        }
        return this.plant.getSpecies().getSpeciesName();
    }

}
