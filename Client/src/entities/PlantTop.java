package entities;
import enums.Rarity;
import enums.Species;
import enums.Stage;
import javafx.scene.image.Image;

public class PlantTop {
    private Image image;
    private Species species;
    private Stage stage;
    private HealthStat healthStat;
    private Rarity rarity;
    private int price;

    public PlantTop(Image image, Species species, Stage stage, int price){
        this.image = image;
        this.stage = stage;
        this.species= species;
        this.healthStat= new HealthStat();
        this.price = price;
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

    public void setImage(Image image) {
        this.image = image;
    }
    public int getPrice(){return price;}
}
