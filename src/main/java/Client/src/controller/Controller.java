package controller;

import builder.ItemBuilder;
import builder.PlantTopBuilder;
import builder.RoomBuilder;
import entities.*;
import enums.DecoType;
import enums.PotType;
import enums.Rarity;
import enums.Species;
import main.java.Application.Controllers.MainController;
import main.java.Application.Controllers.SoundEffectHandler;
import main.java.Application.Controllers.WidgetHandler;

import java.util.ArrayList;

/**
 * Singleton client controller class that handles communication between backend and frontend controllers.
 * @author Rana Noorzadeh
 * @author Christian Storck
 * @author Mouhammed Fakhro
 * */
public class Controller {

    private GameHandler gameHandler;
    private ClientConnection clientConnection;
    private WidgetHandler widgetHandler;
    private LoginHandler loginHandler;
    private WeatherUpdater weatherUpdater;
    private InformationConverter infoConverter;
    private User currentUser;
    private SoundEffectHandler soundEffectHandler;
    private static Controller instance;

    /** Constructor initializes all controller classes connected to this controller. */
    private Controller() {
        clientConnection = new ClientConnection(this);
        loginHandler = new LoginHandler(this);
        infoConverter = new InformationConverter(this);
        weatherUpdater = new WeatherUpdater(this);
        soundEffectHandler = new SoundEffectHandler();

        currentUser = getTestUser();
    }

    /** returns singleton instance of controller */
    public static synchronized Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /** generates and returns a test user */
    public User getTestUser() {
        // room
        ArrayList<Room> rooms = new ArrayList<>();
        Room room = RoomBuilder.buildCommonRoom();
        rooms.add(room);

        // inventory items
        Item pot1 = ItemBuilder.buildPot(PotType.ROUND_POT_CLAY);
        Item seed1 = ItemBuilder.buildSeed(Rarity.COMMON);
        Item seed2 = ItemBuilder.buildSeed(Rarity.RARE);
        Item pot2 = ItemBuilder.buildPot(PotType.POT_LILAC);
        Item deco1 = ItemBuilder.buildDeco(DecoType.TERRARIUM);
        Item deco2 = ItemBuilder.buildDeco(DecoType.GLOBE_BLUE);

        // add to inventory
        Inventory inventory = new Inventory();
        inventory.addItem(pot1);
        inventory.addItem(pot2);
        inventory.addItem(seed1);
        inventory.addItem(seed2);
        inventory.addItem(deco1);
        inventory.addItem(deco2);

        // make potted plants
        Pot pot = ItemBuilder.buildPot(PotType.ROUND_POT_STRIPED_GREEN);
        PlantTop plantTop = PlantTopBuilder.buildPlantTop(Species.ROSE_PLANT);
        plantTop.raiseAge(440);
        PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

        Pot pot3 = ItemBuilder.buildPot(PotType.POT_LILAC);
        PlantTop plantTop2 = PlantTopBuilder.buildPlantTop(Species.COFFEE_PLANT);
        plantTop2.raiseAge(440);
        PottedPlant pottedPlant2 = new PottedPlant(pot3, plantTop2);

        Pot pot4 = ItemBuilder.buildPot(PotType.ROUND_POT_GOLDEN);
        PlantTop plantTop3 = PlantTopBuilder.buildPlantTop(Species.ORCHID);
        plantTop3.raiseAge(440);
        PottedPlant pottedPlant3 = new PottedPlant(pot4, plantTop3);

        Pot pot5 = ItemBuilder.buildPot(PotType.POT_ORANGE);
        PlantTop plantTop4 = PlantTopBuilder.buildPlantTop(Species.CHILI_PEPPER);
        plantTop4.raiseAge(440);
        PottedPlant pottedPlant4 = new PottedPlant(pot5, plantTop4);

        Pot pot6 = ItemBuilder.buildPot(PotType.ROUND_POT_STRIPED_GREEN);
        PlantTop plantTop5 = PlantTopBuilder.buildPlantTop(Species.SWORD_FERN);
        plantTop5.raiseAge(440);
        PottedPlant pottedPlant5 = new PottedPlant(pot6, plantTop5);

        Pot pot7 = ItemBuilder.buildPot(PotType.ROUND_POT_GOLDEN);
        PlantTop plantTop6 = PlantTopBuilder.buildPlantTop(ItemBuilder.buildSeed(Rarity.EPIC).getSpecies());
        plantTop6.raiseAge(18);
        PottedPlant pottedPlant6 = new PottedPlant(pot7, plantTop6);


        // placing potted plants in room
        room.getSlot(2).setPlacedItem(pottedPlant);
        room.getSlot(9).setPlacedItem(pottedPlant2);
        room.getSlot(20).setPlacedItem(pottedPlant3);
        room.getSlot(14).setPlacedItem(pottedPlant4);
        room.getSlot(16).setPlacedItem(pottedPlant5);
        room.getSlot(18).setPlacedItem(pottedPlant6);

        // user
        return new User("MarcusPantman", "Marcus@live.se", inventory, rooms, 1000);
    }

    /* ----------------------------------------
     *  methods for ClientConnection and LoginHandler
     *  --------------------------------------- */

    /** method called after a successful login and user data conversion.
     * Initiates gameHandler, loads widget preferences on device.
     * */
    public void loadGame(User user) {
        this.currentUser = user;
        gameHandler = new GameHandler(currentUser);
        soundEffectHandler.startBackgroundMusic();
        //gameHandler.updateSinceLast();
        gameHandler.startTimer();
        //widgetHandler.loadWidgets(currentUser.getUsername());
    }

    /**
     * Method that sends a string to the clientConnection which confirms with the server
     * whether a username is taken or not.
     * @param username
     * @return Boolean
     */
    public Boolean checkUserNameAvailability(String username) {
        return clientConnection.checkUserNameAvailability(username);
    }

    /**
     * Method that sends a string to the clientconnection which confirms with the server
     * whether an email is used or not.
     * @param email
     * @return Boolean
     */
    public Boolean checkEmailAvailability(String email) {
        return clientConnection.checkEmailAvailability(email);
    }

    /**
     * Method that is called when a user is trying to update their account information
     * with new details.
     * @param email
     * @param username
     * @param password
     * @return Boolean
     */
    public Boolean updateAccountInfo(String email, String username, String password){
        return clientConnection.updateAccountInfo(email, username, password);
    }

    /* ------------------------------------------
    *  login/logout methods for GUIController
    *  ------------------------------------------ */

    /**
     * Method called for making a login attempt.
     * @param email
     * @param password
     * @return boolean
     */
    public boolean loginAttempt(String email, String password) {
       // boolean success = loginHandler.login(email, password);
        boolean success = true;
        if (success) {
            loadGame(currentUser);
        }
        return success;
        //loadGame(currentUser);
        //return true;
    }

    /**
     * Method that is called when a user is trying to register a new account.
     * @param email
     * @param userName
     * @param password
     * @return
     */
    public Boolean registerAccountAttempt(String email, String userName, String password) {
        return loginHandler.register(email, userName, password);
    }

    /**
     * Method called when trying to log out. The widgets position on the desktop is saved, the current user information
     * is saved and then finally we try to log out.
     */
    public void logoutAttempt() {
        soundEffectHandler.stopBackgroundMusic();

        gameHandler.stopTimer();
        //saveGame();
        gameHandler = null;

        //widgetHandler.updateLocalFile(currentUser.getUsername());
        //clientConnection.saveUser(currentUser);
        //clientConnection.logout();
    }

    public void setJwtToken(String token){
        clientConnection.setJwtToken(token);
    }

    /* --------------------------------------------
     *  methods called by GameHandler/TimeEventHandler
     *  ------------------------------------------- */

    /** method attempts to save the game in the database */
    public void saveGame() {
        clientConnection.saveUser(currentUser);
    }

    /**
     * Method that returns the current weather from weatherupdater class.
     * @return String current weather
     */
    public String getCurrentWeather(){
        return weatherUpdater.getCurrentWeather();
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

    /** waters the given plant via gameHandler */
    public void waterPlant(PottedPlant plant){
        gameHandler.waterPlant(plant);
    }

    /** returns the water level of given PottedPlant */
    public int getWaterLevelOf (PottedPlant plant){
        return plant.getPlantTop().getHealthStat().getWaterLevel();
    }

    /** returns the water level of given PottedPlant */
    public int getEnvironmentLevelOf (PottedPlant plant){
        return plant.getPlantTop().getHealthStat().getEnvSatisfaction();
    }

    /** method places a pottedPlant from the inventory in the room,
     * removes from inventory */
    public void placePlantInRoom (int inventoryIndex) {
        gameHandler.placeInventoryPlantInRoom(inventoryIndex, 0);
    }

    /** method places a Pot from the inventory in the room,
     * removes from inventory */
    public void placePotInRoom (int inventoryIndex) {
        gameHandler.placeInventoryPotInRoom(inventoryIndex, 0);
    }

    /** method places a Deco from the inventory in the room,
     * removes from inventory */
    public void placeDecoInRoom (int inventoryIndex) {
        gameHandler.placeInventoryDecoInRoom(inventoryIndex, 0);
    }

    /** places item in PlacementSlot back to inventory */
    public void removeItemFromSlot(int placementIndex) {
        gameHandler.removeItemFromSlot(0, placementIndex);
    }

    /** plants a seed in a pot, creates a PottedPlant, places in inventory */
    public void plantSeed(int inventoryPotIndex, int inventorySeedIndex) {
        gameHandler.plantSeed(inventoryPotIndex, inventorySeedIndex);
        playPlantingSound();
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

    /* --------------------------------------------
     *  getters for GUI controller class
     *  ------------------------------------------- */

    /** Returns reference to current logged-in user */
    public User getCurrentUser() {
        return this.currentUser;
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

    /** Method returns an ArrayList containing all Pot items in the shop.*/
    public ArrayList<Pot> getShopPots() {
        return gameHandler.getShopPots();
    }

    /** Method returns an ArrayList containing all Seed items in the shop.*/
    public ArrayList<Seed> getShopSeeds() {
        return gameHandler.getShopSeeds();
    }

    /** Method returns an ArrayList containing all Deco items in the shop.*/
    public ArrayList<Deco> getShopDecos() {
        return gameHandler.getShopDecos();
    }

    /* ------------------------------------------
    * sound effect methods
    * ------------------------------------------- */

    /** method plays the watering sound */
    public void playWaterSound() {
        soundEffectHandler.playWaterSound();
    }

    /** method plays the swapping items sound */
    public void playSwappingSound() {
        soundEffectHandler.playSwappingSound();
    }

    /** method plays the plant dying sound */
    public void playDeathSound() {
        soundEffectHandler.playDeathSound();
    }

    /** method plays cash register sound */
    public void playPurchaseSound() {
        soundEffectHandler.playPurchaseSound();
    }

    /** method plays the planting seed sound */
    public void playPlantingSound() {
        soundEffectHandler.playPlantingSound();
    }

    /** method stops the background music */
    public void pauseMusic() {
        soundEffectHandler.stopBackgroundMusic();
    }

    /** method starts the background music */
    public void playMusic() {
        soundEffectHandler.startBackgroundMusic();
    }

    /** method checks if music is currently playing */
    public boolean musicIsPlaying() {
        return soundEffectHandler.musicIsPlaying();
    }

}
