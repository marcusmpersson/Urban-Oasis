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

    public PlantTop(ArrayList<String> imageFilePaths, Species species, int price, Rarity rarity){
        this.imageFilePaths = imageFilePaths;
        this.species= species;
        this.healthStat= new HealthStat();
        this.price = price;
        this.rarity = rarity;
        age = 0;
        updateStage();
    }

    public void setBelongingPottedPlant (PottedPlant belongingPottedPlant){
        this.belongingPottedPlant = belongingPottedPlant;
    }

    public Species getSpecies() {
        return species;
    }
    public Rarity getRarity() {
        return rarity;
    }

    public Stage getStage() {
        updateStage();
        checkHealth();
        return stage;
    }

    public void updateStage(){
        // every minute the age is raised by +1
        // the plant would be "planted" for 20 minutes:
        if (age < 20){
            this.stage = Stage.PLANTED;
        }
        // the plant would be a baby for 2 hours (120 mins):
        else if (age < 140){
            this.stage = Stage.BABY;
        }
        // the plant would be young for 5 hours (300 mins):
        else if (age < 440){
            this.stage = Stage.YOUNG;
        }
        // after, the plant would be adult forever (unless it dies):
        else if (440 <= age){
            this.stage = Stage.ADULT;
        }
    }

    public void raiseAge(int amount){
        age += amount;
        updateStage();
    }

    public void checkHealth(){
        // if water level 0 or below, overall health 0 or below, or water level 200 or above
        if (healthStat.getWaterLevel() <= 0 || healthStat.getOverallMood() <= 0
        || healthStat.getWaterLevel() >= 200){
            // then kill plant
            this.stage = Stage.DEAD;
            price = 0;
        }
    }

    public void updateEnvSatisfaction(){
        // if placed at desired environment, raise satisfaction
        if (belongingPottedPlant.getPlacedAt().getEnvironment()
                == species.getPreferredEnvironment()){
            healthStat.raiseEnvSatisfaction();
        }
        // otherwise, lower satisfaction
        else {
            healthStat.lowerEnvSatisfaction();
        }
    }

    public HealthStat getHealthStat(){return healthStat;}

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

    public void lowerWaterLevel() {
        healthStat.lowerWaterLevel(species.getWaterRate());
        healthStat.establishOverallMood();
        checkHealth();
    }
}
