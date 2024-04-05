package entities;
import enums.Rarity;
import enums.Species;
import enums.Stage;

import java.util.ArrayList;

public class PlantTop {
    private ArrayList<String> imageFilePaths;
    private Species species;
    private Stage stage;
    private HealthStat healthStat;
    private Rarity rarity;
    private int price;
    private int age;
    private PottedPlant belongingPottedPlant;

    public PlantTop(ArrayList<String> imageFilePaths, Species species, Stage stage, int price){
        this.imageFilePaths = imageFilePaths;
        this.stage = stage;
        this.species= species;
        this.healthStat= new HealthStat();
        this.price = price;
        age = 0;
    }

    public void setBelongingPottedPlant (PottedPlant belongingPottedPlant){
        this.belongingPottedPlant = belongingPottedPlant;
    }

    public Species getSpecies() {
        return species;
    }

    public Stage getStage() {
        updateStage();
        checkHealth();
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
        if (healthStat.getWaterLevel() == 0 || healthStat.getOverallMood() == 0
        || healthStat.getWaterLevel() >= 200){
            this.stage = Stage.DEAD;
        }
    }

    public void updateEnvSatisfaction(){
        // if placed at desired environment, raise satisfaction
        if (belongingPottedPlant.getPlacedAt().getEnvironment() == species.getPreferredEnvironment()){
            healthStat.raiseEnvSatisfaction();
        }
        // otherwise, lower satisfaction
        else {
            healthStat.lowerEnvSatisfaction();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public int getPrice(){return price;}

    public String getImageFilePath() {
        updateStage();
        checkHealth();

        synchronized (this) {
            if (this.stage == Stage.BABY) {
                return imageFilePaths.get(0);
            } else if (this.stage == Stage.YOUNG) {
                return imageFilePaths.get(1);
            } else if (this.stage == Stage.ADULT) {
                return imageFilePaths.get(2);
            } else if (this.stage == Stage.DEAD) {
                return imageFilePaths.get(3);
            }
            return null; //if stage "planted", returns no image for PlantTop
        }
    }
}
