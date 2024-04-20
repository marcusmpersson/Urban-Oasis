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
    private int basePrice;
    private int age;
    private PottedPlant belongingPottedPlant;

    /** Constructor, sets instance variables. Initiates age to 0, updates plant stage. */
    public PlantTop(ArrayList<String> imageFilePaths, Species species, int price, Rarity rarity){
        this.imageFilePaths = imageFilePaths;
        this.species= species;
        this.healthStat= new HealthStat();
        this.basePrice = price;
        this.rarity = rarity;
        age = 0;
        updateStage();
    }

    /** saves a reference to the belonging PottedPlant */
    public void setBelongingPottedPlant (PottedPlant belongingPottedPlant){
        this.belongingPottedPlant = belongingPottedPlant;
    }

    /** returns species of plant */
    public Species getSpecies() {
        return species;
    }

    /** returns the level of rarity of plant */
    public Rarity getRarity() {
        return rarity;
    }

    /** checks age and health, then returns current stage */
    public Stage getStage() {
        updateStage();
        checkHealth();
        return stage;
    }

    /** checks age of plant, updates STAGE if needed */
    public void updateStage(){
        // every minute the age is auto raised by +1
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
        else if (age >= 440){
            this.stage = Stage.ADULT;
        }
    }

    /** raises age of plant by given amount, updates stage */
    public void raiseAge(int amount){
        age += amount;
        updateStage();
    }

    /** checks health of PlantTop and if plant has died.
     * If yes, sets stage to DEAD, sets price to 0. */
    public void checkHealth(){
        // if water level 0 or below, overall health 0 or below, or water level 200 or above
        if (healthStat.getWaterLevel() <= 0 || healthStat.getOverallMood() <= 0
        || healthStat.getWaterLevel() >= 200){

            this.stage = Stage.DEAD;
            basePrice = 0;
        }
    }

    /** updates the environment satisfaction of plant by given amount
     * @param amount the number of hours passed, used for bigger
     *               updates at application startup*/
    public void updateEnvSatisfaction(int amount){
        // if placed at desired environment, raise satisfaction
        if (belongingPottedPlant.getPlacedAt().getEnvironment()
                == species.getPreferredEnvironment()){
            healthStat.raiseEnvSatisfaction(amount);
        }
        // otherwise, lower satisfaction
        else {
            healthStat.lowerEnvSatisfaction(amount);
        }
    }

    /** returns the HealthStat instance of this PlantTop*/
    public HealthStat getHealthStat(){return healthStat;}

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /** re-calculates the price of plant based on age,
     * returns the price of the plantTop */
    public int getPrice() {
        return basePrice + (age / 2);
    }

    /** returns the current image for the plant-top */
    public String getImageFilePath() {
        updateStage();
        checkHealth();

        synchronized (this) {
            switch (stage) {
                case PLANTED:
                    return imageFilePaths.get(0);
                case BABY:
                    return imageFilePaths.get(1);
                case YOUNG:
                    return imageFilePaths.get(2);
                case ADULT:
                    return imageFilePaths.get(3);
                case DEAD:
                    return imageFilePaths.get(4);
            }
        }

        return null;
    }

    /** lowers the water level of plant by the water rate of its species
     * @param multiplier the number of minutes passed, used for bigger
     *                   updates at application startup*/
    public void lowerWaterLevel(int multiplier) {
        healthStat.lowerWaterLevel(species.getWaterRate() * multiplier);
        healthStat.establishOverallMood();
        checkHealth();
    }

    /** method waters the plant, re-calculates overall mood and checks health of plant */
    public void water() {
        healthStat.water();
        healthStat.establishOverallMood();
        checkHealth();
    }
}
