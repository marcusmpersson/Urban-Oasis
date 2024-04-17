package Client.src.Controllers;

import Client.src.Builders.RoomBuilder;
import Client.src.entities.*;
import Client.src.enums.Rarity;
import Client.src.enums.Species;
import Client.src.enums.Stage;

import java.util.ArrayList;

public class GameHandler {
    private User currentUser;
    private Controller controller;
    private Shop shop;
    private Room room;
    private Inventory inventory;



    /** Constructor */
    public GameHandler(Controller controller, User user) {
        this.inventory = new Inventory();
        this.room = new Room();
        this.shop = new Shop();
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

    //This method  calls a HealthStat method that
    // increases by one the waterLevel of the plant occupying the slot indicated by the index.
    public void waterPlant(int index){
        if(index< room.slotsSize() && index >= 0){
            Placeable itemPlaced = room.getSlot(index).getPlacedItem();
            if (itemPlaced instanceof PottedPlant) {
                HealthStat healthStat = ((PottedPlant) itemPlaced).getHealthStat();
                healthStat.water();
            }
        }
    }

    //this method places a pottedPlant from the inventory in a room slot.
    //The PottedPlant is then removed from the inventory.
    public void placePlantInSlot(int inventoryIndex, int placementIndex){
        PlacementSlot slot = room.getSlot(placementIndex);
        slot.setPlacedItem(inventory.getPottedPlantAt(inventoryIndex));
        inventory.removePottedPlantAt(inventoryIndex);
    }
    //Removes the plant from the slot indicated by the index and places it back in the inventory
    public void removePlantFromSlot(int index){
        if(index >= 0 && index < room.slotsSize()) {
            Placeable plantToRemove = room.getSlot(index).getPlacedItem();
            inventory.addItem((PottedPlant) plantToRemove);
            //Remove plant by resetting placedItem to what value?????
            // if nothing else is placed?? null parameter is bad practice.
            //room.getSlot(index).setPlacedItem(null);
        }
    }

    //Creates a new PottedPlant Item of the same Species as the Seed provided and places it in the room.
    public PottedPlant plantSeed(int inventoryPotIndex,int inventorySeedIndex, int placementIndex){
        if((placementIndex >=0 && placementIndex<room.slotsSize()) &&
                (inventorySeedIndex >=0 && inventorySeedIndex<inventory.getSeeds().size())&&
                (inventoryPotIndex>=0 && inventorySeedIndex< inventory.getPots().size())){

            Species species = inventory.getSeedAt(inventorySeedIndex).getSpecies();
        Rarity rarity = inventory.getSeedAt(inventorySeedIndex).getRarity();
        int basePrice = inventory.getSeedAt(inventorySeedIndex).getPrice() + inventory.getPotAt(inventoryPotIndex).getPrice();

        PlantTop plantTop = new PlantTop(species, Stage.PLANTED, basePrice,rarity);
        return  new PottedPlant(inventory.getPotAt(inventoryPotIndex),plantTop);
        }
        return null;
    }

        //Adds the specified Item to the inventory and deducts the spent amount from the user's currency.
        //Returns the bought Item.

        public Item purchaseItem(int index){
            if((currentUser.getShopCurrency()> shop.getShopItem(index).getPrice()) &&
                    shop.checkIndex(index)){
                Item boughtItem = shop.getShopItem(index);
                inventory.addItem(boughtItem);
                shopCurrencyDown(shop.getShopItem(index).getPrice());
                return boughtItem;
            }
            return null;
        }

        //This method removes the selected Item from the inventory's corresponding list.
        public void disposeItemFromInventory(Item item, int index){
            //Before being executed this method should ask for user confirmation.
                if (item instanceof PottedPlant){
                    inventory.removePottedPlantAt(index);
                }
                else if (item instanceof Pot){
                    inventory.removePotAt(index);
                }
                else if (item instanceof Seed){
                    inventory.removeSeedAt(index);
                }
                else if (item instanceof Deco){
                    inventory.removeDecorationAt(index);
                }
            }

            //this method is called when an element of the room is dragged to another slot that
            // //is already occupied. It swaps one element for the other.
        public void swapItems(int draggingIndex, int droppingIndex){
                Placeable draggedItem = room.getSlot(draggingIndex).getPlacedItem();
                Placeable droppedUponItem = room.getSlot(droppingIndex).getPlacedItem();
                room.getSlot(droppingIndex).setPlacedItem(draggedItem);
                room.getSlot(draggingIndex).setPlacedItem(droppedUponItem);
        }
        //When time has passed this method is called to reduce the waterLevel of all plants in the room.
        public void updateWaterLevelOfALl(){
            for (int i=0; i < room.slotsSize();i++) {
                Placeable itemPlaced = room.getSlot(i).getPlacedItem();
                if (itemPlaced instanceof PottedPlant){
                    ((PottedPlant)itemPlaced).getHealthStat().lowerWaterLevel();
                }
            }
        }
        //This method ages the plants by one unit when a significant time-unit has passed.
        public void updateAgeOfALl(){
            for (int i=0; i < room.slotsSize();i++) {
                Placeable itemPlaced = room.getSlot(i).getPlacedItem();
                if (itemPlaced instanceof PottedPlant){
                    ((PottedPlant)itemPlaced).getHealthStat().lowerWaterLevel();
                }
            }
        }

        //this method adds a certain amount to the User's shopCurrency and return the updated amount.
        public int shopCurrencyUp(int amount){
            int updatedShopCurrency = currentUser.getShopCurrency() + amount;
            currentUser.setShopCurrency(updatedShopCurrency);

            return updatedShopCurrency;
    }

        public boolean shopCurrencyDown(int amount){
            if (amount < currentUser.getShopCurrency()){
                int updatedShopCurrency = currentUser.getShopCurrency() - amount;
                currentUser.setShopCurrency((updatedShopCurrency));
                return true;
            }
            else{
                return false;
            }
        }



}






