package entities;
import controller.Controller;
import enums.Rarity;
import enums.Species;
import enums.Stage;

import java.io.Serializable;
import java.util.ArrayList;

public class PlantTop implements Serializable {

    private ArrayList<String> imageFilePaths;
    private Species species;
    private Stage stage;
    private HealthStat healthStat;
    private Rarity rarity;
    private int basePrice;
    private int age;
    private PottedPlant belongingPottedPlant;
    private String descriptionText;
    private String name;

    /** Constructor, sets instance variables.
     * Initiates age to 0, updates plant stage. */
    public PlantTop (ArrayList<String> imageFilePaths, Species species, int price,
                     Rarity rarity, String descriptionText) {
        this.imageFilePaths = imageFilePaths;
        this.species= species;
        this.healthStat= new HealthStat();
        this.basePrice = price;
        this.rarity = rarity;
        this.descriptionText = descriptionText;
        this.name = this.species.getSpeciesName();
        age = 0;
        updateStage();
    }

    // ------------------------------------------
    // ENTITY LOGIC
    // ------------------------------------------

    /** checks age of plant, updates STAGE if needed. Checks if plant has died */
    public synchronized void updateStage() {

        if (this.stage != Stage.DEAD) {
            // the plant would be "planted" for 20 minutes:
            if (age < 20) {
                this.stage = Stage.PLANTED;
            }
            // the plant would be a baby for 2 hours (120 mins):
            else if (age < 22) {
                this.stage = Stage.BABY;
            }
            // the plant would be young for 5 hours (300 mins):
            else if (age < 24) {
                this.stage = Stage.YOUNG;
            }
            // after, the plant would be adult forever (unless it dies):
            else {
                this.stage = Stage.ADULT;
            }
            checkHealth();
        }
    }

    /** if plant isn't dead, raises age of plant by given amount, updates stage */
    public void raiseAge(int amount) {
        if (this.stage != Stage.DEAD) {
            age += amount;
            updateStage();
        }
    }

    /** checks health of PlantTop and if plant has died.
     * If yes, sets stage to DEAD, sets PlantTop's price to 0. */
    public void checkHealth() {
        if (this.stage != Stage.DEAD) {

            // if water level 0 or below, overall health 0 or below, water level 200 or above,
            // or environment satisfaction 0 or below
            if (healthStat.getWaterLevel() <= 0 || healthStat.getOverallMood() <= 0
                    || healthStat.getWaterLevel() >= 200 || healthStat.getEnvSatisfaction() <= 0) {

                this.stage = Stage.DEAD;
                basePrice = 0;
                Controller.getInstance().playDeathSound();
            }
        }
    }

    /** updates the environment satisfaction of plant by given amount
     * @param amount the number of hours passed, used for bigger
     *               updates at application startup. Otherwise 1 */
    public void updateEnvSatisfaction(int amount) {
        if (this.stage != Stage.DEAD) {
            // if placed at desired environment, raise satisfaction
            if (
                    belongingPottedPlant.getPlacedAt().getEnvironment()
                    == species.getPreferredEnvironment())
            {
                healthStat.raiseEnvSatisfaction(amount);
            }
            // otherwise, lower satisfaction
            else {
                healthStat.lowerEnvSatisfaction(amount);
            }
        }
    }

    /** lowers the water level of plant by the water rate of its species
     * @param amount the number of minutes passed, used for bigger
     *                   updates at application startup. Otherwise 1*/
    public void lowerWaterLevel(int amount) {
        if (this.stage != Stage.DEAD) {
            healthStat.lowerWaterLevel(species.getWaterRate() * amount);
            checkHealth();
        }
    }

    /** method waters the plant and checks health of plant */
    public void water() {
        if (this.stage != Stage.DEAD) {
            healthStat.water();
            Controller.getInstance().playWaterSound();
            checkHealth();
        }
    }

    // ------------------------------------------
    // GETTERS
    // ------------------------------------------

    public String getDescriptionText() {
        if (this.stage == Stage.PLANTED){
            return "this is a planted "+this.rarity.toString()+" seed waiting to sprout!";
        }
        return descriptionText;
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

    /** re-calculates the price of the plantTop based on age,
     * returns the price of the plantTop */
    public int getPrice() {
        return ( basePrice + (age/2) );
    }

    /** returns the HealthStat instance of this PlantTop*/
    public HealthStat getHealthStat() {
        return healthStat;
    }

    /** returns the level of rarity of plant */
    public Rarity getRarity() {
        return rarity;
    }

    /** returns species of plant */
    public Species getSpecies() {
        return species;
    }

    /** checks age and health, then returns current stage */
    public Stage getStage() {
        updateStage();
        return stage;
    }

    public String getName() {
        return name;
    }

    // ------------------------------------------
    // SETTERS - mainly for test purposes
    // ------------------------------------------

    /** saves a reference to the belonging PottedPlant */
    public void setBelongingPottedPlant (PottedPlant belongingPottedPlant) {
        this.belongingPottedPlant = belongingPottedPlant;
    }

    /** sets the current stage of plant */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
