package Controllers;

import Builders.ItemBuilder;
import Builders.PlantTopBuilder;
import Builders.RoomBuilder;
import entities.*;
import enums.PotType;
import enums.Rarity;
import enums.Species;
import main.java.Application.Controllers.MainController;
import main.java.Application.Controllers.WidgetHandler;

import java.util.ArrayList;

public class Controller {

    private GameHandler gameHandler;
    private ClientConnection clientConnection;
    private WidgetHandler widgetHandler;
    private LoginHandler loginHandler;
    private WeatherUpdater weatherUpdater;
    private InformationConverter infoConverter;
    private User currentUser;
    private MainController guiController;
    private static Controller instance;

    /** Constructor initializes all controller classes connected to this controller. */
    private Controller() {
        clientConnection = new ClientConnection(this);
        loginHandler = new LoginHandler(this);
        infoConverter = new InformationConverter(this);
        //widgetHandler = new WidgetHandler();
        weatherUpdater = new WeatherUpdater(this);
        //widgetHandler = new WidgetHandler();

        currentUser = generateTestUser();
    }

    /** returns singleton instance of controller */
    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
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

        Pot pot3 = ItemBuilder.buildPot(PotType.POT_LILAC);
        PlantTop plantTop2 = PlantTopBuilder.buildPlantTop(Species.COFFEE_PLANT);
        plantTop2.raiseAge(440);
        PottedPlant pottedPlant2 = new PottedPlant(pot3, plantTop2);

        // placing potted plant in room
        room.getSlot(2).setPlacedItem(pottedPlant);
        room.getSlot(9).setPlacedItem(pottedPlant2);

        // user
        return new User("MarcusPantman", "Marcus@live.se", inventory, rooms, 1000);
    }

    public User getTestUser() {
        return currentUser;
    }

    /** method initiates a default User item for a newly signed-up account,
     * adds starter items and values
     * @param username the username of the signed up account
     * @param email the email of the signed up account */
    public User generateDefaultUser(String username, String email){

        // user gets a default room
        ArrayList<Room> rooms = new ArrayList<>();
        Room room = RoomBuilder.buildCommonRoom();
        rooms.add(room);

        // user starts with two common seeds and two basic pots in their inventory
        Item pot1 = ItemBuilder.buildPot(PotType.ROUND_POT_CLAY);
        Item pot2 = ItemBuilder.buildPot(PotType.POT_ORANGE);

        Item seed1 = ItemBuilder.buildSeed(Rarity.COMMON);
        Item seed2 = ItemBuilder.buildSeed(Rarity.COMMON);

        // create inventory and add the items
        Inventory inventory = new Inventory();
        inventory.addItem(pot1);
        inventory.addItem(pot2);
        inventory.addItem(seed1);
        inventory.addItem(seed2);

        // user starts with 400 shop currency
        return new User(username, email, inventory, rooms, 400);
    }

    /* ----------------------------------------
     *  methods for ClientConnection, LoginHandler, InformationConverter
     *  --------------------------------------- */

    /** method called after a successful login and user data conversion.
     * Initiates gameHandler, loads widget preferences on device.
     * */
    public void loadGame(User user) {
        this.currentUser = user;
        gameHandler = new GameHandler(currentUser);
        gameHandler.updateSinceLast();
        gameHandler.startTimer();
        widgetHandler.loadWidgets(currentUser.getUsername());
    }

    /** MIGHT BE DELETED
     * method called when user logging in for the first time.
     * Creates a User with default values, initiates GameHandler, loads game on GUI.
     * */
    public void loadGameFirstTime(String username, String email) {
        this.currentUser = generateDefaultUser(username, email);
        gameHandler = new GameHandler(currentUser);
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

    /**
     * Method called for making a login attempt.
     * @param email
     * @param password
     * @return boolean
     */
    public boolean loginAttempt(String email, String password) {
        return loginHandler.login(email, password);
    }

    public Boolean registerAccountAttempt(String email, String userName, String password) {
        return loginHandler.register(email, userName, password);
    }

    /**
     * Method called when trying to log out. The widgets position on the desktop is saved, the current user information
     * is saved and then finally we try to log out.
     */
    public void logoutAttempt() {
        gameHandler.stopTimer();
        gameHandler = null;
        widgetHandler.updateLocalFile(currentUser.getUsername());
        clientConnection.saveUser(currentUser);
        clientConnection.logout();
    }

    /**
     * Method called for trying to delete the currently used user account.
     */
    public void deleteAccountAttempt() {
        clientConnection.delete();
    }

    public void setJwtToken(String token){
        clientConnection.setJwtToken(token);
    }

    /* --------------------------------------------
     *  methods called by GameHandler/TimeEventHandler
     *  ------------------------------------------- */

    public void saveGame() {
        widgetHandler.updateLocalFile(currentUser.getUsername());
        clientConnection.saveUser(currentUser);
    }
    public String getCurrentWeather(){
        return weatherUpdater.getCurrentWeather();
    }
    public void popUpMessage(String message) {
        //TODO: show pop-up message in GUI
    }

    /* ------------------------------------------------
     *  event driven game functions - called by GUI Controller
     *  ----------------------------------------------- */

    /** Method called by GUI controller when user attempts to purchase a pot.
     * @return true if user has enough currency, false if not */
    public boolean purchasePot(int index) {
        return gameHandler.purchasePot(index);
    }

    /** Method called by GUI controller when user attempts to purchase a seed.
     * @return true if user has enough currency, false if not */
    public boolean purchaseSeed(int index) {
        return gameHandler.purchaseSeed(index);
    }

    /** Method called by GUI controller when user attempts to purchase a deco.
     * @return true if user has enough currency, false if not */
    public boolean purchaseDeco(int index) {
        return gameHandler.purchaseDeco(index);
    }

    /** waters plant at given placement slot index */
    public void waterPlant(int placementIndex){
        gameHandler.waterPlant(0, placementIndex);
    }

    /** method places a pottedPlant from the inventory in a room slot,
     * removes from inventory */
    public void placePlantInSlot (int inventoryIndex, int placementIndex) {
        gameHandler.placeInventoryPlantInSlot(inventoryIndex, 0, placementIndex);
    }

    /** method places a Pot from the inventory in a room slot,
     * removes from inventory */
    public void placePotInSlot (int inventoryIndex, int placementIndex) {
        gameHandler.placeInventoryPotInSlot(inventoryIndex, 0, placementIndex);
    }

    /** method places a Deco from the inventory in a room slot,
     * removes from inventory */
    public void placeDecoInSlot (int inventoryIndex, int placementIndex) {
        gameHandler.placeInventoryDecoInSlot(inventoryIndex, 0, placementIndex);
    }

    /** places item in PlacementSlot back to inventory */
    public void removeItemFromSlot(int placementIndex) {
        gameHandler.removeItemFromSlot(0, placementIndex);
    }

    /** plants a seed in a pot, creates a PottedPlant, places in inventory */
    public void plantSeed(int inventoryPotIndex, int inventorySeedIndex) {
        gameHandler.plantSeed(inventoryPotIndex, inventorySeedIndex);
    }

    /** disposes Pot from inventory.
     * @param index index of the item in inventory */
    public void disposePotFromInventory(int index) {
        gameHandler.disposePotFromInventory(index);
    }

    /** disposes Plant from inventory.
     * @param index index of the item in inventory */
    public void disposePlantFromInventory(int index) {
        gameHandler.disposePlantFromInventory(index);
    }

    /** disposes Deco from inventory.
     * @param index index of the item in inventory */
    public void disposeDecoFromInventory(int index) {
        gameHandler.disposeDecoFromInventory(index);
    }

    /** disposes Seed from inventory.
     * @param index index of the item in inventory */
    public void disposeSeedFromInventory(int index) {
        gameHandler.disposeSeedFromInventory(index);
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

    /** method places an item back in the inventory. Clears the slot. */
    public void placeItemBackInInventory(int placementIndex) {
        gameHandler.placeItemBackInInventory(0, placementIndex);
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
            widgetHandler.addWidget(item, null);
        }
    }

}
