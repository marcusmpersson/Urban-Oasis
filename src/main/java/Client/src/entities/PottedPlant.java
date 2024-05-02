package entities;

import java.io.Serializable;
import java.util.ArrayList;

public class PottedPlant extends Item implements Placeable, Serializable {

    private Pot pot;
    private PlantTop plant;
    private PlacementSlot placedAt;

    public PottedPlant(Pot pot, PlantTop plant){
        super(pot.getPrice() + plant.getPrice());
        this.pot = pot;
        this.plant = plant;

    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    public Pot getPot() {
        return pot;
    }

    public PlantTop getPlantTop() {
        return plant;
    }

    @Override
    public PlacementSlot getPlacedAt() {
        return placedAt;
    }

    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }

    public ArrayList<String> getImagesFilePaths(){
        ArrayList<String> filePathArray = new ArrayList<>();
        filePathArray.add(plant.getImageFilePath());
        filePathArray.add(pot.getImageFilePath());

        return filePathArray;
    }

    /** method re-calculates the price of PottedPlant and returns it */
    @Override
    public int getPrice() {
        price = plant.getPrice() + pot.getPrice();
        return price;
    }
}
