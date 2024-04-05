package Controllers;

import entities.*;

import java.util.ArrayList;

public class Controller {
    private GameHandler gameHandler;
    private ClientConnection connection;
    private GUIController guiController;
    private WidgetHandler widgetHandler;
    private LocalFileHandler localFileHandler;
    private LoginHandler loginHandler;
    private InformationConverter infoConverter;
    private User currentUser;

    /** Constructor */
    public Controller(){
        connection = new ClientConnection(this);
        guiController = new GUIController(this);
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

    /* --------------------------
    *  methods for GUIController
    *  -------------------------- */
    /** method returns an ArrayList<String> containing filepath to room images,
     * for daytime (index 0), sunset (index 1), night (index 2), sun-rise (index 3).
     * @param index the index of chosen room */
    public ArrayList<String> getRoomImagePaths(int index){
        return gameHandler.getRoomImagePaths(index);
    }
    public ArrayList<PottedPlant> getRoomPlants(){
        ArrayList<PottedPlant> plants = new ArrayList<PottedPlant>();
        for (Item item : gameHandler.getRoomItems()){
            if (item instanceof PottedPlant){
                plants.add((PottedPlant) item);
            }
        }
        return plants;
    }
    public ArrayList<Pot> getRoomPots(){
        ArrayList<Pot> pots = new ArrayList<Pot>();
        for (Item item : gameHandler.getRoomItems()){
            if (item instanceof Pot){
                pots.add((Pot) item);
            }
        }
        return pots;
    }
    public ArrayList<Deco> getRoomDecos(){
        ArrayList<Deco> decos = new ArrayList<Deco>();
        for (Item item : gameHandler.getRoomItems()){
            if (item instanceof Deco){
                decos.add((Deco) item);
            }
        }
        return decos;
    }
    public ArrayList<Item> getRoomItems(){
        return gameHandler.getRoomItems();
    }
    public ArrayList<PottedPlant> getInventoryPlants(){
        return gameHandler.getInventoryPlants();
    }
    public ArrayList<Pot> getInventoryPots(){
        return gameHandler.getInventoryPots();
    }
    public ArrayList<Seed> getInventorySeeds(){
        return gameHandler.getInventorySeeds();
    }
    public ArrayList<Deco> getInventoryDecos(){
        return gameHandler.getInventoryDecos();
    }
    public String getPlayerUsername(){
        return currentUser.getUsername();
    }
    public int getPlayerCoins(){
        return currentUser.getShopCurrency();
    }
    public String getPlayerEmail(){
        return currentUser.getEmail();
    }
}
