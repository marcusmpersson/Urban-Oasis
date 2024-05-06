package entities;

import java.io.Serializable;

public class HealthStat implements Serializable {
    private int overallMood;
    private int waterLevel;
    private int envSatisfaction;

    /** constructor sets all starting health bars to 50 and calculates overall mood */
    public HealthStat() {
        this.waterLevel = 50;
        this.envSatisfaction = 50;
        calculateOverallMood();
    }

    // ------------------------------------------
    // ENTITY LOGIC
    // ------------------------------------------

    /** method calculates and updates overall mood */
    public synchronized void calculateOverallMood() {
        overallMood = (envSatisfaction + waterLevel) / 2;
    }

    /** method updates waterLevel.
     * If plant needs water, watering set the level to 100%
     * If plant doesn't need watering, watering raises the level by 20%
     * Re-calculates overall mood. */
    public void water() {
        //if water level is below 100%, watering it should put it to 100%
        if (waterLevel < 90){
            waterLevel = 100;
        }
        // otherwise, should raise by 20%
        else if (waterLevel >= 100 ) {
            waterLevel += 20;
        }
        calculateOverallMood();
    }

    /** method lowers the water level by given amount, limited down to 0.
     * Re-calculates overall mood.
     * @param amount the amount to be lowered */
    public void lowerWaterLevel(int amount) {
        if (waterLevel > 0) {
            this.waterLevel -= amount;
            calculateOverallMood();
        }
    }

    /** method raises environment satisfaction by given amount, limited up to 100.
     * Re-calculates overall mood.
    * @param amount the amount to be raised */
    public void raiseEnvSatisfaction(int amount) {
        if (envSatisfaction < 100) {
            this.envSatisfaction += amount;
            calculateOverallMood();
        }
    }

    /** method lowers environment satisfaction by given amount, limited down to 0.
     * Re-calculates overall mood.
     * @param amount the amount to be lowered */
    public void lowerEnvSatisfaction(int amount){
        if (envSatisfaction > 0) {
            this.envSatisfaction -= amount;
            calculateOverallMood();
        }
    }

    // ------------------------------------------
    // GETTERS
    // ------------------------------------------

    /** returns the current water level*/
    public int getWaterLevel() {
        return waterLevel;
    }

    /** returns the current environment satisfaction level*/
    public int getEnvSatisfaction() {
        return envSatisfaction;
    }

    /** re-calculates and returns the current overall mood level*/
    public int getOverallMood() {
        calculateOverallMood();
        return overallMood;
    }
}
