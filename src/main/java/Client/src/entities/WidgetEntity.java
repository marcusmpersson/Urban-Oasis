package entities;

public class WidgetEntity {

    private PottedPlant pottedPlant;
    private int x;
    private int y;

    public WidgetEntity(PottedPlant pottedPlant, int x, int y) {
        this.pottedPlant = pottedPlant;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getPlantFilePath() {
        return pottedPlant.getPlantTop().getImageFilePath();
    }

    public String getPotFilePath() {
        return pottedPlant.getPot().imageFilePath;
    }

    public void waterWidget(){
        pottedPlant.getPlantTop().getHealthStat().water();
        pottedPlant.getPlantTop().checkHealth();
    }

}
