package Controllers;

import Builders.PlantTopBuilder;
import entities.*;
import enums.Rarity;
import enums.Species;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    }

    /* ---------------------------------
     * Event driven game events
     * --------------------------------- */

    /** attempts to purchase a shop item. If affordable, buys and adds item to
     * user inventory, returns true. If not affordable, returns false.
     * @param index the index of the shop item */
    public boolean purchaseShopItem(int index) {
        ShopItem item = shop.getShopItem(index);

        if (currentUser.getShopCurrency() >= item.getPrice()) {

            currentUser.subtractCurrency(item.getPrice());
            currentUser.getInventory().addItem(item);
            return true;
        }
        else {
            return false;
        }
    }

    /** method waters the PottedPlant item placed at given index at given room
     * @param roomIndex index of chosen room
     * @param placementIndex index of the placement slot item is placed in
     * */
    public void waterPlant(int roomIndex, int placementIndex) {

        // if valid index for both
        if ((roomIndex < currentUser.getRoomsArray().size() && roomIndex > 0)
        && (placementIndex < currentUser.getRoom(roomIndex).getSlots().size() &&
                placementIndex > 0)){

            Placeable itemPlaced = currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem();

            if (itemPlaced instanceof PottedPlant) {
                ((PottedPlant)itemPlaced).getPlantTop().water();
            }
        }
    }

    /** method places a pottedPlant from the inventory in a room slot.
    /* The PottedPlant is then removed from the inventory. */
    public void placePlantInSlot(int inventoryIndex, int roomindex, int placementIndex) {

        // place item
        Placeable placeableItem = currentUser.getInventory().getPottedPlantAt(inventoryIndex);
        currentUser.getRoom(roomindex).getSlot(placementIndex).setPlacedItem(placeableItem);

        // remove from inventory
        currentUser.getInventory().removePottedPlantAt(inventoryIndex);
    }

    /** method places a Pot from the inventory in a room slot.
     /* The Pot is then removed from the inventory. */
    public void placePotInSlot(int inventoryIndex, int roomindex, int placementIndex) {

        // place item
        Placeable placeableItem = currentUser.getInventory().getPotAt(inventoryIndex);
        currentUser.getRoom(roomindex).getSlot(placementIndex).setPlacedItem(placeableItem);

        // remove from inventory
        currentUser.getInventory().removePotAt(inventoryIndex);
    }

    /** removes Placeable item from given slot at given room,
     *  places item back in the inventory
     * */
    public void removeItemFromSlot(int roomIndex, int placementIndex) {

        // if valid index for both
        if ((roomIndex < currentUser.getRoomsArray().size() && roomIndex > 0)
                && (placementIndex < currentUser.getRoom(roomIndex).getSlots().size() &&
                placementIndex > 0)) {

            Item item = ((Item) currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem());

            if (item != null) {
                currentUser.getInventory().addItem(item);
                currentUser.getRoom(roomIndex).getSlot(placementIndex).clear();
            }
        }
    }

    /*** method creates a new PottedPlant Item with given seed and pot, places in inventory
     * @param inventoryPotIndex index of the selected pot in inventory
     * @param inventorySeedIndex index of the selected seed in inventory
     * */
    public PottedPlant plantSeed(int inventoryPotIndex, int inventorySeedIndex){
        //generate plant
        Species species = currentUser.getInventory().getSeedAt(inventorySeedIndex).getSpecies();
        PlantTop plantTop = PlantTopBuilder.buildPlantTop(species);
        Pot pot = currentUser.getInventory().getPotAt(inventoryPotIndex);
        PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

        //place in inventory
        currentUser.getInventory().addItem(pottedPlant);

        //remove seed and pot item from inventory
        currentUser.getInventory().removeSeedAt(inventorySeedIndex);
        currentUser.getInventory().removePotAt(inventoryPotIndex);

        return pottedPlant;
    }

    /** method removes the selected Item from the inventory's corresponding list */
    public void disposeItemFromInventory(Item item, int index){

        if (item instanceof PottedPlant){
            currentUser.getInventory().removePottedPlantAt(index);
        }
        else if (item instanceof Pot){
            currentUser.getInventory().removePotAt(index);
        }
        else if (item instanceof Seed){
            currentUser.getInventory().removeSeedAt(index);
        }
        else if (item instanceof Deco){
            currentUser.getInventory().removeDecorationAt(index);
        }
    }

    /** method called when a Placeable item in the room is dragged to another slot.
     * swaps placements if both slots taken. If empty, swaps with null */
    public void swapItems(int draggingIndex, int droppingIndex, int roomIndex){
        // save items
        Placeable draggedItem = currentUser.getRoom(roomIndex).getSlot(draggingIndex).getPlacedItem();
        Placeable droppedUponItem = currentUser.getRoom(roomIndex).getSlot(droppingIndex).getPlacedItem();

        // swap items
        currentUser.getRoom(roomIndex).getSlot(droppingIndex).setPlacedItem(draggedItem);
        currentUser.getRoom(roomIndex).getSlot(draggingIndex).setPlacedItem(droppedUponItem);
        System.out.println("Successfully swapped items.");
    }

    /** method sells a PottedPlant that is placed in room. Adds currency to user */
    public void sellPlacedPlant (int roomIndex, int placementIndex) {
        Placeable placeable = currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem();
        if (placeable instanceof PottedPlant){
            currentUser.increaseCurrency( ((PottedPlant)placeable).getPrice() );
        }
        currentUser.getRoom(roomIndex).getSlot(placementIndex).clear();
    }

    /** method sells a PottedPlant that is placed in the inventory. Adds currency to user
     * @param index index of the PottedPlant item in the inventory
     * @return updated user currency */
    public int sellInventoryPlant (int index){
        Placeable placeable = currentUser.getInventory().getPottedPlantAt(index);
        if (placeable != null){
            currentUser.increaseCurrency( ((PottedPlant)placeable).getPrice() );
        }
        currentUser.getInventory().removePottedPlantAt(index);
        return currentUser.getShopCurrency();
    }

    /* ---------------------------------
     * Time based game events (TimeEventHandler)
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

    /** updates the last updated time of current logged-in user */
    public void updateUserLastUpdatedTime(LocalDateTime now) {
        currentUser.setLastUpdatedTime(now);
    }

    /* ---------------------------------
     * Methods for Startup
     * --------------------------------- */

    /** method compares last saved time for user (since last logout)
     * and applies passage of time to all affected game components */
    public void updateSinceLast(){
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(currentUser.getLastUpdatedTime(), formatter);
            long minutes = Duration.between(dateTime, now).toMinutes();
            long hours = Duration.between(dateTime, now).toHours();

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

    /* ---------------------------------
     * getters for Controller
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
}
