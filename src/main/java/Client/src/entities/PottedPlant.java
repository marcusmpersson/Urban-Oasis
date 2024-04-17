package Client.src.entities;

public class PottedPlant extends Item implements Placeable {

    private Pot pot;
    private PlantTop plant;
    private PlacementSlot placedAt;
    private HealthStat healthStat;

    public PottedPlant(Pot pot, PlantTop plant){
        this.pot = pot;
        this.plant = plant;
        price = pot.getPrice() + plant.getPrice();
    }

    public HealthStat getHealthStat() {
        return plant.getHealthStat();
    }

    public void setHealthStat(HealthStat healthStat) {
        this.healthStat = healthStat;
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

    @Override
    public String getImageFilePath(){
        //TODO: create/return imageFilePath (if used)
        return "Add return here.";
    }

}
