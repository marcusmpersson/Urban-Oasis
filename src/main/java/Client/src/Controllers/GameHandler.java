package Controllers;

import entities.*;
import enums.Rarity;

import java.util.ArrayList;

public class GameHandler {

    private User currentUser;
    private Controller controller;
    private Shop shop;

    /** Constructor, receives reference of the main controller and the user
     * that is logged in. */
    public GameHandler(Controller controller, User user) {
        shop = new Shop();
        this.controller = controller;
        this.currentUser = user;
        updateSinceLast();
    }

    public boolean purchaseShopItem(int index) {
        ShopItem item = shop.getShopItem(index);

        // if user can afford it
        if (currentUser.getShopCurrency() >= item.getPrice()){
            currentUser.subtractCurrency(item.getPrice());
            currentUser.getInventory().addItem(item);
            return true;
        }
        // otherwise if user cannot afford it
        else {
            return false;
        }
    }

    /* ---------------------------------
    * [BELOW] Methods for Controller -> GUIController - May be removed later!
    * --------------------------------- */

    public ArrayList<Placeable> getRoomItems(int index) {
        return currentUser.getRoom(index).getPlacedItems();
    }
    public ArrayList<PottedPlant> getInventoryPlants() {
        return currentUser.getInventory().getPottedPlants();
    }
    public ArrayList<Pot> getInventoryPots() {
        return currentUser.getInventory().getPots();
    }
    public ArrayList<Seed> getInventorySeeds() {
        return currentUser.getInventory().getSeeds();
    }
    public ArrayList<Deco> getInventoryDecos() {
        return currentUser.getInventory().getDecorations();
    }
    public ArrayList<String> getRoomImagePaths(int index) {
        return currentUser.getRoom(index).getImageFilePaths();
    }

    /* ---------------------------------
     * Methods for TimeEventHandler
     * --------------------------------- */

    public void increaseCurrency(int multiplier) {
        int amount = 0;

        // for every room player has
        for (Room room : currentUser.getRoomsArray()){
            // for every item placed in the room
            for (Placeable item : room.getPlacedItems()){
                // if it's a plant
                if (item instanceof PottedPlant){
                    if (((PottedPlant) item).getPlantTop().getRarity() == Rarity.COMMON) {
                        amount += 1;
                    } else if (((PottedPlant) item).getPlantTop().getRarity() == Rarity.RARE){
                        amount += 2;
                    } else if (((PottedPlant) item).getPlantTop().getRarity() == Rarity.EPIC){
                        amount += 3;
                    } else {
                        amount += 4;
                    }
                }
            }
        }
        currentUser.increaseCurrency(amount * multiplier);
    }

    public void raiseAges(int multiplier) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().raiseAge(multiplier);
                }
            }
        }
    }

    public void lowerAllWaterLevels(int multiplier) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().lowerWaterLevel(multiplier);
                }
            }
        }
    }

    public void updateEnvSatisfactions(int multiplier) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().updateEnvSatisfaction(multiplier);
                }
            }
        }
    }

    /* ---------------------------------
     * Methods for Startup / changes since last time
     * --------------------------------- */

    /** method compares last saved time for user (since last logout)
     * and applies passage of time to all affected game components */
    public void updateSinceLast(){
        int minutes = 1;
        //TODO: get and compare time

        // 1 minute pace updates
        raiseAges(minutes);
        increaseCurrency(minutes);

        // 1 hour pace updates
        lowerAllWaterLevels(minutes/60);
        updateEnvSatisfactions(minutes/60);
    }

}
