package entities;
import enums.Species;
import enums.Stage;

public class PlantTop {
    private Image image;
    private Species species;
    private Stage stage;
    private HealthStat healthStat;

    public PlantTop(Image image, Species species, Stage stage){
        this.image = image;
        this.stage = stage;
        this.species= species;
        this.healthStat= new HealthStat();
    }

    public Species getSpecies() {
        return species;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
