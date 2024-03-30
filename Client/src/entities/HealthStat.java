package entities;

public class HealthStat {
    private int overallMood;
    private int waterLevel;
    private int envSatisfaction;

    public HealthStat() {
        this.waterLevel=5;
        this.envSatisfaction= 5;
        this.overallMood= establishOverallMood();

    }
    public void water(){
        this.waterLevel++;
    }
    public void lowerWaterLevel(){
        this.waterLevel--;
    }
    public int getWaterLevel() {
        return waterLevel;
    }

    public int getEnvSatisfaction() {
        return envSatisfaction;
    }
    public void raiseEnvSatisfaction(){
        this.envSatisfaction++;
    }
    public void lowerEnvSatisfaction(){
        this.envSatisfaction--;
    }

    public int getOverallMood() {
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
