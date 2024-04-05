package Controllers;

import entities.*;

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

}
