package entities;

import java.io.Serializable;

public class HealthStat implements Serializable {
    private int overallMood;
    private int waterLevel;
    private int envSatisfaction;

    public HealthStat() {
        this.waterLevel = 50;
        this.envSatisfaction = 50;
        establishOverallMood();

    }
    public void water() {
        //if water level is below 100%, watering it should put it to 100%
        if (waterLevel < 90){
            waterLevel = 100;
        }
        // otherwise, should raise by 20%
        else if (waterLevel >= 100 ) {
            waterLevel += 20;
        }
    }
    public void lowerWaterLevel(int multiplicationRate){
        this.waterLevel -= multiplicationRate;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public int getEnvSatisfaction() {
        return envSatisfaction;
    }

    public void raiseEnvSatisfaction(int amount) {
        if (envSatisfaction < 100) {
            this.envSatisfaction += amount;
        }
    }
    public void lowerEnvSatisfaction(int amount){
        if (envSatisfaction > 0) {
            this.envSatisfaction -= amount;
        }
    }

    public int getOverallMood() {
        establishOverallMood();
        return overallMood;
    }

    public void establishOverallMood(){
        overallMood = (envSatisfaction+waterLevel)/2;
    }

}
