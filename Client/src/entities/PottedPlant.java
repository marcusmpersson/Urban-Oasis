package entities;

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

    public PlacementSlot getPlacedAt() {
        return placedAt;
    }

    @Override
    public void setPlacedAt(PlacementSlot slot) {
        this.placedAt = slot;
    }

}
