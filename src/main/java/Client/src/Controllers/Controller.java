package Controllers;

import entities.*;

import java.util.ArrayList;

public class Controller {
    private GameHandler gameHandler;
    private ClientConnection clientConnection;
    //private GUIController guiController;
    private WidgetHandler widgetHandler;
    private LocalFileHandler localFileHandler;
    private LoginHandler loginHandler;
    private InformationConverter infoConverter;
    private User currentUser;

    /** Constructor */
    public Controller(){
        clientConnection = new ClientConnection(this);
        //guiController = new GUIController(this);
        widgetHandler = new WidgetHandler(this);
        localFileHandler = new LocalFileHandler(this);
        loginHandler = new LoginHandler(this);
        infoConverter = new InformationConverter(this);
    }

    /* --------------------------
     *  methods for ClientConnection, LoginHandler, InformationConverter
     *  -------------------------- */
    /** method called after a successful login and user data conversion.
     * Initiates gameHandler, loads game on GUI, loads widget preferences on device.
     * */
    public void loadGame(User user){
        this.currentUser = user;
        gameHandler = new GameHandler(this, user);
        //TODO: guiController.startGame(); eller metod med annan namn
        widgetHandler.loadWidgets(user.getUsername());
    }

    public void saveGame(User user) {
        clientConnection.saveUser(user);
        //TODO: save game (user object "currentUser") in the database --- SHOULD WORK NOW
    }

    /** Method called if email change was approved by server */
    public void emailChangeSuccessful(String newEmail){
        currentUser.setEmail(newEmail);
    }

    /** Method called if email change was NOT approved by server */
    public void emailChangeUnsuccessful(){
        // TODO: show error in GUI
    }

    /* --------------------------
    *  methods for GUIController
    *  -------------------------- */

    public void loginAttempt(String email, String password){
            clientConnection.setJwtToken(loginHandler.login(email,password));
    }
    public void registerAccountAttempt(String email, String userName, String password){
       // loginHandler.register(email, userName, password);
        System.out.println("nice");
    }
    public void logoutAttempt(){
        clientConnection.logout();
    }
    public void deleteAccountAttempt(){
        clientConnection.delete();
    }

    /** method returns an ArrayList of String containing filepath to room images,
     * for daytime (index 0), sunset (index 1), night (index 2), sun-rise (index 3).
     * @param index the index of chosen room */
    public ArrayList<String> getRoomImagePaths(int index){
        return gameHandler.getRoomImagePaths(index);
    }

    /** Method returns an ArrayList containing only the PottedPlant items placed in the room */
    public ArrayList<PottedPlant> getRoomPlants(int index){
        ArrayList<PottedPlant> plants = new ArrayList<PottedPlant>();
        for (Placeable item : gameHandler.getRoomItems(index)){
            if (item instanceof PottedPlant){
                plants.add((PottedPlant) item);
            }
        }
        return plants;
    }

    /** Method returns an ArrayList containing only the Pot items placed in the room.
     * @param index the index of chosen room */
    public ArrayList<Pot> getRoomPots(int index){
        ArrayList<Pot> pots = new ArrayList<Pot>();
        for (Placeable item : gameHandler.getRoomItems(index)){
            if (item instanceof Pot){
                pots.add((Pot) item);
            }
        }
        return pots;
    }

    /** Method returns an ArrayList containing only the Deco items placed in the room.
     * @param index the index of chosen room */
    public ArrayList<Deco> getRoomDecos(int index){
        ArrayList<Deco> decos = new ArrayList<Deco>();
        for (Placeable item : gameHandler.getRoomItems(index)){
            if (item instanceof Deco){
                decos.add((Deco) item);
            }
        }
        return decos;
    }

    /** Method returns an ArrayList containing all items placed in the room as implementations
     * of Placeable.
     * @param index the index of chosen room*/
    public ArrayList<Placeable> getRoomItems(int index){
        return gameHandler.getRoomItems(index);
    }

    /** Method returns an ArrayList of PlacementSlots in a room.
     * @param index the index of chosen room*/
    public ArrayList<PlacementSlot> getRoomPlacementSlots(int index){return currentUser.getRoom(index).getSlots();}

    /** Method returns an ArrayList containing all PottedPlant items in players inventory.*/
    public ArrayList<PottedPlant> getInventoryPlants(){
        return gameHandler.getInventoryPlants();
    }

    /** Method returns an ArrayList containing all Pot items in players inventory.*/
    public ArrayList<Pot> getInventoryPots(){
        return gameHandler.getInventoryPots();
    }

    /** Method returns an ArrayList containing all Seed items in players inventory.*/
    public ArrayList<Seed> getInventorySeeds(){
        return gameHandler.getInventorySeeds();
    }

    /** Method returns an ArrayList containing all Deco items in players inventory.*/
    public ArrayList<Deco> getInventoryDecos(){
        return gameHandler.getInventoryDecos();
    }

    /** Gets current player's username */
    public String getPlayerUsername(){
        return currentUser.getUsername();
    }

    /** Gets current player's currency amount */
    public int getPlayerCoins(){
        return currentUser.getShopCurrency();
    }

    /** Gets current player's email */
    public String getPlayerEmail(){
        return currentUser.getEmail();
    }

    /** Method called by GUI when user attempts to change email */
    public void emailChangeAttempt(String newEmail){
        //TODO: skicka till ClientConnection för att kontrollera email change på server
        // (asynchronous method call)
    }

    /** Method called by GUI when user attempts to purchase an item from the shop.
     * @return true if user had enough currency, false if not */
    public boolean purchaseShopItem(int index){
        return gameHandler.purchaseShopItem(index);
    }


    /* --------------------------------------------
     *  methods for GameHandler + TimeEventHandler
     *  ------------------------------------------- */
    /** method called every second by TimeEventHandler to update the gui */
    public void updateGUI() {
        widgetHandler.update();

        //TODO: if (application is open)
        // guiController.update();
    }
}
