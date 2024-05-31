package entities;

/** Class represents a widget as an entity to be interacted with.
 * NOTE: methods in this class ended up not used.
 * @author Rana Noorzadeh
 * */
public class WidgetEntity {

    private PottedPlant pottedPlant;
    private int x;
    private int y;

    /** creates a widget connected to a PottedPlant instance, with x and y coordinates */
    public WidgetEntity(PottedPlant pottedPlant, int x, int y) {
        this.pottedPlant = pottedPlant;
        this.x = x;
        this.y = y;
    }

    /** returns the X position of the widget on the desktop */
    public int getX() {
        return x;
    }

    /** returns the Y position of the widget on the desktop */
    public int getY() {
        return y;
    }

    /** updates the XY position of the widget on the desktop */
    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** returns the PlantTop image filepath of the widget */
    public String getPlantFilePath() {
        return pottedPlant.getPlantTop().getImageFilePath();
    }

    /** returns the Pot image filepath of the widget */
    public String getPotFilePath() {
        return pottedPlant.getPot().imageFilePath;
    }

    /** waters the connected PlantTop instance via the widget */
    public void waterWidget(){
        pottedPlant.getPlantTop().getHealthStat().water();
        pottedPlant.getPlantTop().checkHealth();
    }

}
