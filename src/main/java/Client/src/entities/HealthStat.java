package entities;

public class HealthStat {
    private int overallMood;
    private int waterLevel;
    private int envSatisfaction;

    public HealthStat() {
        this.waterLevel=50;
        this.envSatisfaction= 50;
        this.overallMood= establishOverallMood();

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
    public void raiseEnvSatisfaction() {
        if (envSatisfaction < 100) {
            this.envSatisfaction++;
        }
    }
    public void lowerEnvSatisfaction(){
        if (envSatisfaction > 0) {
            this.envSatisfaction--;
        }
    }

    public int getOverallMood() {
        establishOverallMood();
        return overallMood;
    }

    public int establishOverallMood(){
        return 0;
    }
        /*
        condition1 = envSatisfaction
        condition2 = waterLevel
        Both will be conditions to establish the overallMood on a scale.
        Which scale and the impact of each condition should be discussed further
        */


}
