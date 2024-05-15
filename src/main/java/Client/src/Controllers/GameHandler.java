package Controllers;

import Builders.PlantTopBuilder;
import entities.*;
import enums.Species;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GameHandler {

    private User currentUser;
    private Controller controller;
    private Shop shop;
    private TimeEventHandler timeEventHandler;

    /** Constructor, receives reference of the logged-in user. */
    public GameHandler(User user) {
        shop = new Shop();
        this.controller = Controller.getInstance();
        this.currentUser = user;
    }

    /* ---------------------------------
     * Event driven game events
     * --------------------------------- */

    /** private method attempts to purchase an item.
     * If affordable, buys and adds item to user inventory, returns true.
     * otherwise returns false. */
    private boolean purchase (ShopItem item){
        // if sufficient funds
        if (currentUser.getShopCurrency() >= item.getPrice()) {
            //subtract currency
            currentUser.subtractCurrency(item.getPrice());
            //add to inventory
            currentUser.getInventory().addItem(item);
            return true;
        }
        else {
            return false;
        }
    }

    /** attempts to purchase a pot from the shop. returns false if insufficient funds.
     * @param index the index of the pot */
    public boolean purchasePot(int index) {
        if (index < shop.getPotsSize()) {
            return purchase(shop.getPot(index));
        }
        return false;
    }

    /** attempts to purchase a seed from the shop. returns false if insufficient funds.
     * @param index the index of the seed */
    public boolean purchaseSeed(int index) {
        if (index < shop.getSeedsSize()) {
            return purchase(shop.getSeed(index));
        }
        return false;
    }

    /** attempts to purchase a deco from the shop. returns false if insufficient funds.
     * @param index the index of the deco */
    public boolean purchaseDeco(int index) {
        if (index < shop.getDecosSize()) {
            return purchase(shop.getDeco(index));
        }
        return false;
    }

    /** method waters the PottedPlant item placed at given index at given room
     * @param roomIndex index of chosen room
     * @param placementIndex index of the placement slot item is placed in
     * */
    public void waterPlant(int roomIndex, int placementIndex) {

        // if valid index for both
        if ((roomIndex < currentUser.getRoomsArray().size() && roomIndex >= 0)
        && (placementIndex < currentUser.getRoom(roomIndex).getSlots().size() &&
                placementIndex >= 0)){

            Placeable itemPlaced = currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem();

            if (itemPlaced instanceof PottedPlant) {
                ((PottedPlant)itemPlaced).getPlantTop().water();
            }
        }
    }

    /** method places a pottedPlant from the inventory in a room slot.
    /* The PottedPlant is then removed from the inventory. */
    public void placeInventoryPlantInSlot(int inventoryIndex, int roomIndex, int placementIndex) {

        // place item
        Placeable placeableItem = currentUser.getInventory().getPottedPlantAt(inventoryIndex);
        currentUser.getRoom(roomIndex).getSlot(placementIndex).setPlacedItem(placeableItem);

        // remove from inventory
        currentUser.getInventory().removePottedPlantAt(inventoryIndex);
    }

    /** method places a Pot from the inventory in a room slot.
     /* The Pot is then removed from the inventory. */
    public void placeInventoryPotInSlot(int inventoryIndex, int roomIndex, int placementIndex) {

        // place item
        Placeable placeableItem = currentUser.getInventory().getPotAt(inventoryIndex);
        currentUser.getRoom(roomIndex).getSlot(placementIndex).setPlacedItem(placeableItem);

        // remove from inventory
        currentUser.getInventory().removePotAt(inventoryIndex);
    }

    /** method places a Deco from the inventory in a room slot.
     /* The Deco is then removed from the inventory. */
    public void placeInventoryDecoInSlot(int inventoryIndex, int roomIndex, int placementIndex) {

        // place item
        Placeable placeableItem = currentUser.getInventory().getDecorationAt(inventoryIndex);
        currentUser.getRoom(roomIndex).getSlot(placementIndex).setPlacedItem(placeableItem);

        // remove from inventory
        currentUser.getInventory().removeDecorationAt(inventoryIndex);
    }

    /** removes Placeable item from given slot at given room,
     *  places item back in the inventory
     * */
    public void removeItemFromSlot(int roomIndex, int placementIndex) {

        // if valid index for both
        if ((roomIndex < currentUser.getRoomsArray().size() && roomIndex >= 0)
                && (placementIndex < currentUser.getRoom(roomIndex).getSlots().size() &&
                placementIndex >= 0)) {

            Item item = ((Item) currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem());

            if (item != null) {
                currentUser.getInventory().addItem(item);
                currentUser.getRoom(roomIndex).getSlot(placementIndex).clear();
            }
        }
    }

    /*** method creates a new PottedPlant Item with selected seed and pot, places in inventory
     * @param inventoryPotIndex index of the selected pot in inventory
     * @param inventorySeedIndex index of the selected seed in inventory
     * */
    public void plantSeed(int inventoryPotIndex, int inventorySeedIndex) {
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
    }

    /** method removes the selected Plant from the inventory */
    public void disposePlantFromInventory(int index) {
        if (index < currentUser.getInventory().getPottedPlants().size()
        && index >= 0) {
            currentUser.getInventory().removePottedPlantAt(index);
        }
    }

    /** method removes the selected Pot from the inventory */
    public void disposePotFromInventory(int index) {
        if (index < currentUser.getInventory().getPots().size()
                && index >= 0) {
            currentUser.getInventory().removePotAt(index);
        }
    }

    /** method removes the selected Seed from the inventory */
    public void disposeSeedFromInventory(int index) {
        if (index < currentUser.getInventory().getSeeds().size()
                && index >= 0) {
            currentUser.getInventory().removeSeedAt(index);
        }
    }

    /** method removes the selected Deco from the inventory */
    public void disposeDecoFromInventory(int index) {
        if (index < currentUser.getInventory().getDecorations().size()
                && index >= 0) {
            currentUser.getInventory().removeDecorationAt(index);
        }
    }

    /** method called when a Placeable item in the room is dragged to another slot.
     * swaps placements if both slots taken. If empty, swaps with null */
    public void swapItems(int draggingIndex, int droppingIndex, int roomIndex){

        // save reference to both items placed at the two slots
        Placeable slot1Item = currentUser.getRoom(roomIndex).getSlot(draggingIndex).getPlacedItem();
        Placeable slot2Item = currentUser.getRoom(roomIndex).getSlot(droppingIndex).getPlacedItem();

        // swap items
        currentUser.getRoom(roomIndex).getSlot(droppingIndex).setPlacedItem(slot1Item);
        currentUser.getRoom(roomIndex).getSlot(draggingIndex).setPlacedItem(slot2Item);
    }

    /** method sells a PottedPlant that is placed in room. Adds currency to user */
    public void sellPlacedPlant (int roomIndex, int placementIndex) {
        Placeable placeable = currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem();
        if (placeable instanceof PottedPlant) {
            currentUser.increaseCurrency( ((PottedPlant)placeable).getPrice() );
            currentUser.getRoom(roomIndex).getSlot(placementIndex).clear();
        }
    }

    /** method sells a PottedPlant that is placed in the inventory. Adds currency to user
     * @param index index of the PottedPlant item in the inventory */
    public void sellInventoryPlant (int index) {
        if (index < currentUser.getInventory().getPottedPlants().size()) {

            PottedPlant pottedPlant = currentUser.getInventory().getPottedPlantAt(index);
            currentUser.increaseCurrency(pottedPlant.getPrice());

            currentUser.getInventory().removePottedPlantAt(index);
        }
    }

    /** method places an item back in the inventory. Clears the slot.
     * @param roomIndex index of the room
     * @param placementIndex index of the slot */
    public void placeItemBackInInventory(int roomIndex, int placementIndex) {
        Placeable placedItem = currentUser.getRoom(roomIndex).getSlot(placementIndex).getPlacedItem();

        currentUser.getInventory().addItem((Item)placedItem);
        currentUser.getRoom(roomIndex).getSlot(placementIndex).clear();
    }

    /* ---------------------------------
     * Time based game events (TimeEventHandler)
     * --------------------------------- */

    /** increases currency of user for every placed plant in every room, depending
     * on plant rarities. Multiplies by number of minutes passed.
     * @param minutes the number of minutes passed
     * */
    public void increaseCurrency(int minutes) {
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
        currentUser.increaseCurrency(amount * minutes);
    }

    /** updates age of all plants in all rooms by given amount
     * @param minutes number of minutes passed */
    public void raiseAges(int minutes) {
        // for every room
        for (Room room : currentUser.getRoomsArray()) {
            // for every placed item
            for (Placeable item : room.getPlacedItems()) {
                // if it's a plant, raise age
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().raiseAge(minutes);
                }
            }
        }
    }

    /** updates water level of all plants in all rooms by given amount
     * @param hours number of hours passed */
    public void lowerAllWaterLevels(int hours) {
        // for every room
        for (Room room : currentUser.getRoomsArray()){
            // for every placed item
            for (Placeable item : room.getPlacedItems()){
                // if it's a plant, lower water level
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().lowerWaterLevel(hours);
                }
            }
        }
    }

    /** updates environment satisfaction of all plants in all rooms by given amount
     * @param hours number of hours passed */
    public void updateEnvSatisfactions(int hours) {
        // for every room
        for (Room room : currentUser.getRoomsArray()){
            // for every placed item
            for (Placeable item : room.getPlacedItems()){
                // if it's a plant, update environment satisfaction
                if (item instanceof PottedPlant){
                    ((PottedPlant)item).getPlantTop().updateEnvSatisfaction(hours);
                }
            }
        }
    }

    /** updates the last updated time of current logged-in user */
    public void updateUserLastUpdatedTime(LocalDateTime now) {
        currentUser.setLastUpdatedTime(now);
    }

    /* ---------------------------------
     * Methods for Startup and closing
     * --------------------------------- */

    /** method initiates TimeEventHandler and starts tracking time-based game events. */
    public void startTimer(){
        this.timeEventHandler = new TimeEventHandler(this);
        timeEventHandler.startThreads();
    }

    /** method stops all time tracking threads */
    public void stopTimer(){
        timeEventHandler.stopAllThreads();
    }

    /** method compares last saved time for user (since last logout)
     * and applies passage of time to all affected game components */
    public void updateSinceLast(){
        try {
            // get current time
            LocalDateTime now = LocalDateTime.now();

            // create formatter ?
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // get last saved time
            LocalDateTime dateTime = LocalDateTime.parse(currentUser.getLastUpdatedTime(), formatter);

            // compare and calculate amount of time passed
            long minutes = Duration.between(dateTime, now).toMinutes(); // minutes passed since last
            long hours = Duration.between(dateTime, now).toHours(); // hours passed since last

            // 1 minute pace updates
            raiseAges(Math.toIntExact(minutes));
            increaseCurrency(Math.toIntExact(minutes));

            // 1 hour pace updates
            lowerAllWaterLevels(Math.toIntExact(hours));
            updateEnvSatisfactions(Math.toIntExact(hours));

            // update user last updated time
            updateUserLastUpdatedTime(now);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ---------------------------------
     * getters for Controller - might be deleted in the end
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
