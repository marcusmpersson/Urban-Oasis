package Controllers;

import entities.*;
import enums.Rarity;

import java.time.Duration;
import java.time.LocalDateTime;
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

    /** attempts to purchase a shop item. If affordable, buys and adds item to
     * user inventory, returns true. If not affordable, returns false.
     * @param index the index of the shop item */
    public boolean purchaseShopItem(int index) {
        ShopItem item = shop.getShopItem(index);

        if (currentUser.getShopCurrency() >= item.getPrice()){
            currentUser.subtractCurrency(item.getPrice());
            currentUser.getInventory().addItem(item);
            return true;
        }
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

    /** increases currency of user for every placed plant in every room, depending
     * on plant rarities. Multiplies by number of minutes passed.
     * @param multiplier the number of minutes passed
     * */
    public void increaseCurrency(int multiplier) {
        int amount = 0;

        // for every room player has
        for (Room room : currentUser.getRoomsArray()){
            // for every item placed in the room
            for (Placeable item : room.getPlacedItems()){
                // if it's a plant
                if (item instanceof PottedPlant){
                    switch (((PottedPlant) item).getPlantTop().getRarity()){
                        case COMMON:
                            amount += 1;
                            break;
                        case RARE:
                            amount += 2;
                            break;
                        case EPIC:
                            amount += 3;
                            break;
                        case LEGENDARY:
                            amount += 4;
                            break;
                    }
                }
            }
        }
        currentUser.increaseCurrency(amount * multiplier);
    }

    /** updates age of all plants in all rooms by given amount
     * @param multiplier number of minutes passed */
    public void raiseAges(int multiplier) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().raiseAge(multiplier);
                }
            }
        }
    }

    /** updates water level of all plants in all rooms by given amount
     * @param multiplier number of minutes passed */
    public void lowerAllWaterLevels(int multiplier) {
        for (Room room : currentUser.getRoomsArray()){
            for (Placeable item : room.getPlacedItems()){
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().lowerWaterLevel(multiplier);
                }
            }
        }
    }

    /** updates environment satisfaction of all plants in all rooms by given amount
     * @param multiplier number of hours passed */
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
        try {
            LocalDateTime now = LocalDateTime.now();
            long minutes = Duration.between(currentUser.getLastUpdatedTime(), now).toMinutes();
            long hours = Duration.between(currentUser.getLastUpdatedTime(), now).toHours();

            // 1 minute pace updates
            raiseAges(Math.toIntExact(minutes));
            increaseCurrency(Math.toIntExact(minutes));

            // 1 hour pace updates
            lowerAllWaterLevels(Math.toIntExact(hours));
            updateEnvSatisfactions(Math.toIntExact(hours));

            updateUserLastUpdatedTime(now);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** updates the last updated time of current logged-in user */
    public void updateUserLastUpdatedTime(LocalDateTime now) {
        currentUser.setLastUpdatedTime(now);
    }
}
