package main.java.Application.Controllers;

import controller.Controller;
import entities.*;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import main.java.Application.Boundary.Inventory;

import java.util.ArrayList;

public class InventoryController {

    private TilePane inventoryPane;
    private Group inventoryView;
    private Controller clientController;
    private ArrayList<Group> seedItems = new ArrayList<>();
    private ArrayList<Group> potItems = new ArrayList<>();
    private ArrayList<Group> decoItems = new ArrayList<>();
    private ArrayList<Group> plantItems = new ArrayList<>();

    private ArrayList<Seed> ownedSeeds;
    private ArrayList<Pot> ownedPots;
    private ArrayList<Deco> ownedDecos;
    private ArrayList<PottedPlant> ownedPottedPlants;

    private Group selectedItem;
    private Group plantInformationPopup;

    private ImageView closePopupButton;
    private boolean plantInformationPopupIsOpened = false;
    private String currentCategory;
    private Inventory inventoryContent;

    private ImageView plantSeedButton;
    private ImageView cancelPlantSeed;
    private ImageView disposeItemButton;

    private ImageView putInRoomButton;
    private boolean isPlanting = false;
    private Seed pickedSeedForPlanting;
    private Pot pickedPotForPlanting;
    private PottedPlant pickedPottedPlantToPutInRoom;

    private MainController mainController;

    public InventoryController (MainController mainController, Controller clientController, TilePane inventoryPane, Group inventoryView, Group plantInformationPopup, ImageView closePopupButton, ImageView plantSeedButton, ImageView cancelPlantSeed, ImageView disposeItemButton, ImageView putInRoomButton) {
        this.mainController = mainController;
        this.inventoryPane = inventoryPane;
        this.inventoryView = inventoryView;
        this.clientController = clientController;
        this.plantInformationPopup = plantInformationPopup;
        this.closePopupButton = closePopupButton;
        this.plantSeedButton = plantSeedButton;
        this.cancelPlantSeed = cancelPlantSeed;
        this.disposeItemButton = disposeItemButton;
        this.putInRoomButton = putInRoomButton;

        inventoryContent = new Inventory(inventoryPane, this, plantInformationPopup, inventoryView);
        showCategory("Plants");
        setupDetections();
    }

    private void startPlanting() {
        System.out.println("clicked");
        isPlanting = true;
        plantSeedButton.setVisible(false);
        plantSeedButton.setDisable(true);

        cancelPlantSeed.setVisible(true);
        cancelPlantSeed.setDisable(false);
        cancelPlantSeed.setOpacity(1);

        showCategory("Pots");
        System.out.println(selectedItem);
        if (getObjectFromButton(selectedItem) instanceof Seed) {
            pickedSeedForPlanting = (Seed) getObjectFromButton(selectedItem);
        }
        selectPotForPlanting();
        // force pot view
    }
    private void cancelPlant() {
        plantSeedButton.setVisible(true);
        plantSeedButton.setDisable(false);
        plantSeedButton.setOpacity(1);

        cancelPlantSeed.setVisible(false);
        cancelPlantSeed.setDisable(true);
        cancelPlantSeed.setOpacity(0);
        isPlanting = false;
    }

    public void animatePopupFrame(boolean bool) {
        inventoryContent.animatePopupFrame(bool);
        plantInformationPopupIsOpened = bool;
    }

    private void setupDetections() {
        plantSeedButton.setOnMouseClicked(event -> {
            startPlanting();
        });

        cancelPlantSeed.setOnMouseClicked(event -> {
            cancelPlant();
        });

        disposeItemButton.setOnMouseClicked(event -> {
            disposeItem();
        });

        putInRoomButton.setOnMouseClicked(event -> {
            if (getObjectFromButton(selectedItem) instanceof PottedPlant) {
             pickedPottedPlantToPutInRoom = (PottedPlant) getObjectFromButton(selectedItem);
             int inventoryIndex = clientController.getInventoryPlants().indexOf(pickedPottedPlantToPutInRoom);
             clientController.placePlantInRoom(inventoryIndex);

             populateOwnedItemsArray();
             updateInventoryButtons();


            }
        });
    }


    private void selectPotForPlanting() {
        for (Group pot : potItems) {
            if (isPlanting) {
                pot.setOnMouseClicked(event -> {
                    if (isPlanting) {
                        pickedPotForPlanting = (Pot) getObjectFromButton(pot);
                        if (pickedSeedForPlanting != null && pickedPotForPlanting != null) {
                            int seedIndex = ownedSeeds.indexOf(pickedSeedForPlanting);
                            int potIndex = ownedPots.indexOf(pickedPotForPlanting);

                            clientController.plantSeed(potIndex, seedIndex);
                            isPlanting = false;
                            currentCategory = "Seeds";
                            openInventoryView();
                            cancelPlant();
                        }
                    }
                });
            }
        }
    }


    public void openInventoryView() {
        deselectItem();
        populateOwnedItemsArray();
        updateInventoryButtons();
        showCategory(currentCategory);
    }

    public void showCategory(String category) {
        if (!isPlanting) {
            deselectItem();
        }
        currentCategory = category;
        ArrayList<Group> items;
        if (category.equals("Seeds")) {
            items = seedItems;
        } else if (category.equals("Pots")) {
            items = potItems;
        } else if (category.equals("Decos")) {
            items = decoItems;
        } else {
            items = plantItems;
        }

        inventoryPane.getChildren().clear();
        makeAllCategoriesInvisible(seedItems, potItems, decoItems, plantItems);

        for (Group item : items) {
            item.setVisible(true);
            inventoryPane.getChildren().add(item);
        }
    }

    private void populateOwnedItemsArray() {
        ownedSeeds = clientController.getInventorySeeds();
        ownedPots = clientController.getInventoryPots();
        ownedDecos = clientController.getInventoryDecos();
        ownedPottedPlants = clientController.getInventoryPlants();
    }

    private void clearAllButtonArrays() {
        seedItems.clear();
        potItems.clear();
        decoItems.clear();
        plantItems.clear();
    }

    private void updateInventoryButtons () {
        clearAllButtonArrays();
        for (Seed seed : ownedSeeds) {
            Group seedItem = inventoryContent.createItemView(seed.imageFilePath, null, seed.getPrice(), seed.getName(),
                    "Seeds");
            seedItems.add(seedItem);
        }
        for (Pot pot : ownedPots) {
            Group potItem = inventoryContent.createItemView(pot.imageFilePath, null, pot.getPrice(), pot.getName(),
                    "Pots");
            potItems.add(potItem);
        }
        for (Deco deco : ownedDecos) {
            Group decoItem = inventoryContent.createItemView(deco.imageFilePath, null, deco.getPrice(), deco.getName(),
                    "Deco");
            decoItems.add(decoItem);
        }
        for (PottedPlant plant : ownedPottedPlants) {
            Group plantItem = inventoryContent.createItemView(plant.getPlantTop().getImageFilePath(),
                    plant.getPot().getImageFilePath(),
                    plant.getPrice(),
                    plant.getName(), "PottedPlant");
            plantItems.add(plantItem);
        }
    }

    public void makeAllCategoriesInvisible(ArrayList<Group> seedItems, ArrayList<Group> potItems, ArrayList<Group> decoItems, ArrayList<Group> plantItems) {
        for (Group seed : seedItems) {
            seed.setVisible(false);
        }
        for (Group pot : potItems) {
            pot.setVisible(false);
        }
        for (Group deco : decoItems) {
            deco.setVisible(false);
        }
        for (Group plant : plantItems) {
            plant.setVisible(false);
        }
    }

    public void deselectItem() {
        inventoryContent.removeGlowFromAllItems(seedItems, potItems, decoItems, plantItems);
        this.selectedItem = null;
        disposeItemButton.setOpacity(0.3);
        plantSeedButton.setOpacity(0.3);
        plantSeedButton.setVisible(true);
        cancelPlantSeed.setVisible(false);
    }

    private void itemClick(Group item) {
        selectedItem = item;
        inventoryContent.removeGlowFromAllItems(seedItems, potItems, decoItems, plantItems);
        if (!selectedItem.getStyleClass().contains("purpleGlow")) {
            selectedItem.getStyleClass().add("purpleGlow");
            disposeItemButton.setOpacity(1);

            if (getItemTypeFromButton(item).equals("Seeds")) {
                plantSeedButton.setVisible(true);
                plantSeedButton.setOpacity(1);
                plantSeedButton.setDisable(false);
                plantSeedButton.toFront();

            } else if (getItemTypeFromButton(item).equals("PottedPlant")) {
                plantSeedButton.setVisible(false);
                plantSeedButton.setDisable(true);
                putInRoomButton.setVisible(true);
            } else {
                putInRoomButton.setVisible(false);
                plantSeedButton.setOpacity(0.3);
            }
        }
        inventoryContent.addButtonGlow(selectedItem);
    }

    public Item getObjectFromButton(Group button) {
        if (seedItems.contains(button)) {
            return ownedSeeds.get(seedItems.indexOf(button));
        } else if (potItems.contains(button)) {
            return ownedPots.get(potItems.indexOf(button));
        } else if (decoItems.contains(button)) {
            return ownedDecos.get(decoItems.indexOf(button));
        } else {
            return ownedPottedPlants.get(plantItems.indexOf(button));
        }
    }

    public String getItemTypeFromButton(Group button) {
        if (potItems.contains(button)) {
            return "Pots";
        } else if (seedItems.contains(button)) {
            return "Seeds";
        } else if (decoItems.contains(button)){
            return "Decos";
        } else {
            return "Plants";
        }
    }

    public void setupItemClicks(Group item, ImageView infoButton) {
        item.setOnMouseClicked(event -> {
            if (!this.plantInformationPopupIsOpened) {
                itemClick(item);
            }
        });

        infoButton.setOnMouseClicked(event -> {
            if (!this.plantInformationPopupIsOpened) {
                selectedItem = (Group) infoButton.getProperties().get("Parent");
                itemClick(selectedItem);
                plantInformationPopup.toFront();
                inventoryContent.animatePopupFrame(true);
                plantInformationPopupIsOpened = true;
                inventoryContent.openPlantInformationPopup(selectedItem);
            }
        });
    }

    public void disposeItem(){
        if(selectedItem != null){
            Item item = getObjectFromButton(selectedItem);

            if(item instanceof Seed){
                int index = ownedSeeds.indexOf(item);
                clientController.disposeSeedFromInventory(index);
            }
            else if(item instanceof Pot){
                int index = ownedPots.indexOf(item);
                clientController.disposePotFromInventory(index);
            }
            else if(item instanceof Deco){
                int index = ownedDecos.indexOf(item);
                clientController.disposeDecoFromInventory(index);
            }
            else if(item instanceof PottedPlant){
                int index = ownedPottedPlants.indexOf(item);
                clientController.disposePlantFromInventory(index);
            }
            mainController.updateUserCoins();
            openInventoryView();
        }
    }
}
