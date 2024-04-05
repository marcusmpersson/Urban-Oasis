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
    private int age;

    public PlantTop(Image image, Species species, Stage stage, int price){
        this.image = image;
        this.stage = stage;
        this.species= species;
        this.healthStat= new HealthStat();
        this.price = price;
        age = 0;
    }

    public Species getSpecies() {
        return species;
    }

    public Stage getStage() {
        updateStage();
        return stage;
    }
    public void updateStage(){
        // every 2 minutes the age is raised by +1
        // the plant would be "planted" for 20 minutes:
        if (age < 10){
            this.stage = Stage.PLANTED;
        }
        // the plant would be a baby for 2 hours (120 mins):
        else if (age < 70){
            this.stage = Stage.BABY;
        }
        // the plant would be young for 5 hours (300 mins):
        else if (age < 220){
            this.stage = Stage.YOUNG;
        }
        // after, the plant would be adult forever (unless it dies):
        else if (220 <= age){
            this.stage = Stage.ADULT;
        }
    }
    public void raiseAge(int amount){
        age += amount;
        updateStage();
    }

    public void checkHealth(){
        if (healthStat.getWaterLevel() == 0 || healthStat.getOverallMood() == 0){
            this.stage = Stage.DEAD;
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public int getPrice(){return price;}
}
