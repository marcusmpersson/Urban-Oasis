package Controllers;

import entities.*;
import enums.Rarity;

import java.util.ArrayList;

public class GameHandler {
    private User currentUser;
    private Controller controller;
    private Shop shop;


    /** Constructor */
    public GameHandler(Controller controller, User user) {
        shop = new Shop();
        this.controller = controller;
        this.currentUser = user;
    }

    public boolean purchaseShopItem(int index){
        // if user can afford it
        if (currentUser.getShopCurrency() >= shop.getShopItem(index).getPrice()){
            currentUser.subtractCurrency(shop.getShopItem(index).getPrice());
            currentUser.getInventory().addItem(shop.getShopItem(index));
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
    public void autoIncreaseCurrency() {
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
        currentUser.increaseCurrency(amount);
    }

    public void raiseAges() {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().raiseAge(1);
                }
            }
        }
    }

    public void lowerAllWaterLevels() {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().lowerWaterLevel();
                }
            }
        }
    }

    public void updateEnvSatisfactions() {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().updateEnvSatisfaction();
                }
            }
        }
    }

    /* ---------------------------------
     * Methods for Startup / changes since last time
     * --------------------------------- */
    public void raiseAges(int amount) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().raiseAge(amount);
                }
            }
        }
    }
}
