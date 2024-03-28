package entities;

public class PottedPlant extends Item{

    private Pot pot;
    private PlantTop plant;
    private PlacementSlot placedAt;

    public PottedPlant(Pot pot, PlantTop plant){
        this.pot = pot;
        this.plant = plant;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }
}
