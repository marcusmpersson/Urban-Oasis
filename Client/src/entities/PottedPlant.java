package entities;

import java.util.ArrayList;

public class PottedPlant extends Item implements Placeable {

    private Pot pot;
    private PlantTop plant;
    private PlacementSlot placedAt;

    public PottedPlant(Pot pot, PlantTop plant){
        this.pot = pot;
        this.plant = plant;
        price = pot.getPrice() + plant.getPrice();
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

}
