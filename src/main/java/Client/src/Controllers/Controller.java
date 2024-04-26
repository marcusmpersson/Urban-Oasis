package Controllers;

import Builders.ItemBuilder;
import Builders.PlantTopBuilder;
import Builders.RoomBuilder;
import entities.*;
import enums.PotType;
import enums.Rarity;
import enums.Species;
import main.java.Application.MainController;

import java.util.ArrayList;

public class Controller {

    private GameHandler gameHandler;
    private ClientConnection clientConnection;
    private WidgetHandler widgetHandler;
    private LoginHandler loginHandler;
    private InformationConverter infoConverter;
    private User currentUser;
    private MainController guiController;

    /** Constructor initializes all controller classes connected to this controller. */
    public Controller() {
        clientConnection = new ClientConnection(this);
        loginHandler = new LoginHandler(this);
        infoConverter = new InformationConverter(this);
        //this.guiController = guiController;
        widgetHandler = new WidgetHandler(guiController);
        User user = generateTestUser();
        clientConnection.saveUser(user);
    }

    public User generateTestUser(){

        // room
        ArrayList<Room> rooms = new ArrayList<>();
        Room room = RoomBuilder.buildCommonRoom();
        rooms.add(room);

        // inventory items
        Item pot1 = ItemBuilder.buildPot(PotType.ROUND_POT_CLAY);
        Item seed1 = ItemBuilder.buildSeed(Rarity.COMMON);
        Item seed2 = ItemBuilder.buildSeed(Rarity.RARE);
        Item pot2 = ItemBuilder.buildPot(PotType.POT_LILAC);

        // inventory
        Inventory inventory = new Inventory();
        inventory.addItem(pot1);
        inventory.addItem(pot2);
        inventory.addItem(seed1);
        inventory.addItem(seed2);

        // potted plant
        Pot pot = ItemBuilder.buildPot(PotType.POT_STRIPED_BLUE);
        PlantTop plantTop = PlantTopBuilder.buildPlantTop(Species.COFFEE_PLANT);
        plantTop.raiseAge(440);
        PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

        // placing potted plant in room
        room.getSlot(2).setPlacedItem(pottedPlant);

        // user
        return new User("MarcusPantman", "Marcus@live.se", inventory, rooms, 1000);
    }

    /* ----------------------------------------
     *  methods for ClientConnection, LoginHandler, InformationConverter
     *  --------------------------------------- */

    /** method called after a successful login and user data conversion.
     * Initiates gameHandler, loads game view on GUI, loads widget preferences on device.
     * */
    public void loadGame(User user) {
        this.currentUser = user;
        gameHandler = new GameHandler(this, user);
        gameHandler.updateSinceLast();

        //TODO: load GUI game view
    }

    public Boolean checkUserNameAvailability() {
        return clientConnection.checkUserNameAvailability();
    }

    public Boolean checkEmailAvailability() {
        return clientConnection.checkEmailAvailability();
    }

    /** Method called if email change was NOT approved by server */
    public void emailChangeUnsuccessful() {
        // TODO: show error in GUI
    }

    /* --------------------------
    *  methods for GUIController
    *  -------------------------- */

    public boolean loginAttempt(String email, String password) {
        String response = loginHandler.login(email, password);

        if(response.contains("token")) {
            clientConnection.setJwtToken(response);
            loadGame(currentUser);
            widgetHandler.loadWidgets(currentUser.getUsername());
            return true;
        }

        else {
            return false;
        }
    }

    public void registerAccountAttempt(String email, String userName, String password){
        loginHandler.register(email, userName, password);
        System.out.println("nice");
    }

    public void logoutAttempt() {
        widgetHandler.updateLocalFile(currentUser.getUsername());
        clientConnection.logout();
    }

    public void deleteAccountAttempt() {
        clientConnection.delete();
    }
    public void setJwtToken(String token){
        clientConnection.setJwtToken(token);
    }
    /* --------------------------------------------
     *  methods called by GameHandler/TimeEventHandler
     *  ------------------------------------------- */

    public void updateGUI() {
        //TODO: update/repaint GUI
    }

    public void saveGame() {
        widgetHandler.updateLocalFile(currentUser.getUsername());
        clientConnection.saveUser(currentUser);
    }

    public void popUpMessage(String message) {
        //TODO: show pop-up message in GUI
    }

    /* ------------------------------------------------
     *  event driven game functions - called by GUI Controller
     *  ----------------------------------------------- */

    /** Method called by GUI controller class when user attempts to purchase
     * an item from the shop.
     * @return true if user has enough currency, false if not */
    public boolean purchaseShopItem(int index) {
        return gameHandler.purchaseShopItem(index);
    }

    /** waters plant at given placement slot index */
    public void waterPlant(int placementIndex){
        gameHandler.waterPlant(0, placementIndex);
    }

    /** method places a pottedPlant from the inventory in a room slot,
     * removes from inventory */
    public void placePlantInSlot (int inventoryIndex, int placementIndex) {
        gameHandler.placePlantInSlot(inventoryIndex, 0, placementIndex);
    }

    /** method places a Pot from the inventory in a room slot,
     * removes from inventory */
    public void placePotInSlot (int inventoryIndex, int placementIndex) {
        gameHandler.placePotInSlot(inventoryIndex, 0, placementIndex);
    }

    /** places item in PlacementSlot back to inventory */
    public void removeItemFromSlot(int placementIndex) {
        gameHandler.removeItemFromSlot(0, placementIndex);
    }

    /** plants a seed in a pot, creates a PottedPlant, places in inventory */
    public void plantSeed(int inventoryPotIndex, int inventorySeedIndex) {
        gameHandler.plantSeed(inventoryPotIndex, inventorySeedIndex);
    }

    /** disposes item in inventory.
     * @param item an item of the class being deleted
     * @param index index of the item in inventory */
    public void disposeItemFromInventory(Item item, int index) {
        gameHandler.disposeItemFromInventory(item, index);
    }

    /** swaps placement of two items in different slots.
     * swaps even with null value. */
    public void swapItems(int draggingIndex, int droppingIndex) {
        gameHandler.swapItems(draggingIndex, droppingIndex, 0);
    }

    /** method sells a PottedPlant that is placed in room. Adds currency to user
     * @param placementIndex placementSlot index of the PottedPlant */
    public void sellPlacedPlant (int placementIndex) {
        gameHandler.sellPlacedPlant(0, placementIndex);
    }

    /** method sells a PottedPlant that is in the inventory. Adds currency to user
     * @param index index of the PottedPlant in inventory */
    public void sellInventoryPlant (int index) {
        gameHandler.sellInventoryPlant(index);
    }

    /* --------------------------------------------
     *  getters for GUI controller class
     *  ------------------------------------------- */

    /** Gets current player's currency amount */
    public int getPlayerCoins() {
        return currentUser.getShopCurrency();
    }

    /** Gets current player's username */
    public String getPlayerUsername() {
        return currentUser.getUsername();
    }

    /** Gets current player's email */
    public String getPlayerEmail() {
        return currentUser.getEmail();
    }

    /** method returns an ArrayList of String containing filepath to room images,
     * for daytime (index 0), sunset (index 1), night (index 2), sun-rise (index 3). */
    public ArrayList<String> getRoomImagePaths() {
        return gameHandler.getRoomImagePaths(0);
    }

    /** Method returns an ArrayList containing all items placed in the room as implementations
     * of Placeable. */
    public ArrayList<Placeable> getRoomItems() {
        return gameHandler.getRoomItems(0);
    }

    /** Method returns an ArrayList containing only the PottedPlant items placed in the room */
    public ArrayList<PottedPlant> getRoomPlants() {
        ArrayList<PottedPlant> plants = new ArrayList<PottedPlant>();
        for (Placeable item : gameHandler.getRoomItems(0)){
            if (item instanceof PottedPlant){
                plants.add((PottedPlant) item);
            }
        }
        return plants;
    }

    /** Method returns an ArrayList containing only the Pot items placed in the room. */
    public ArrayList<Pot> getRoomPots() {
        ArrayList<Pot> pots = new ArrayList<Pot>();
        for (Placeable item : gameHandler.getRoomItems(0)){
            if (item instanceof Pot){
                pots.add((Pot) item);
            }
        }
        return pots;
    }

    /** Method returns an ArrayList containing only the Deco items placed in the room. */
    public ArrayList<Deco> getRoomDecos() {
        ArrayList<Deco> decos = new ArrayList<Deco>();
        for (Placeable item : gameHandler.getRoomItems(0)){
            if (item instanceof Deco){
                decos.add((Deco) item);
            }
        }
        return decos;
    }

    /** Method returns an ArrayList containing all PottedPlant items in players inventory.*/
    public ArrayList<PottedPlant> getInventoryPlants() {
        return gameHandler.getInventoryPlants();
    }

    /** Method returns an ArrayList containing all Pot items in players inventory.*/
    public ArrayList<Pot> getInventoryPots() {
        return gameHandler.getInventoryPots();
    }

    /** Method returns an ArrayList containing all Seed items in players inventory.*/
    public ArrayList<Seed> getInventorySeeds() {
        return gameHandler.getInventorySeeds();
    }

    /** Method returns an ArrayList containing all Deco items in players inventory.*/
    public ArrayList<Deco> getInventoryDecos() {
        return gameHandler.getInventoryDecos();
    }

    /* --------------------------------------------
     *  widget methods
     *  ------------------------------------------- */

    public void addWidget(int index){
        Placeable item = currentUser.getRoom(0).getSlot(index).getPlacedItem();

        if (item instanceof PottedPlant){
            widgetHandler.addWidget(item);
        }
    }

}
