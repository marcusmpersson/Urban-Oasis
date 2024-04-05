package Controllers;

import entities.*;

import java.util.ArrayList;

public class GameHandler {
    private User currentUser;
    private Controller controller;


    /** Constructor */
    public GameHandler(Controller controller, User user) {
        this.controller = controller;
        this.currentUser = user;
    }


    /* ---------------------------------
    * [BELOW] Methods for Controller -> GUIController - May be removed later!
    * --------------------------------- */
    public ArrayList<Item> getRoomItems() {
    }
    public ArrayList<PottedPlant> getInventoryPlants() {
    }
    public ArrayList<Pot> getInventoryPots() {
    }
    public ArrayList<Seed> getInventorySeeds() {
    }
    public ArrayList<Deco> getInventoryDecos() {
    }
    public ArrayList<String> getRoomImagePaths(int index) {
    }
}
