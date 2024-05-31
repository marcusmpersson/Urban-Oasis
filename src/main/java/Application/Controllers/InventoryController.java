package main.java.Application.Controllers;

import controller.Controller;
import entities.*;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import main.java.Application.Boundary.Inventory;

import java.util.ArrayList;

/**
 * Controller class to manage the inventory view and interactions.
 * Handles displaying, selecting, planting, and disposing of inventory items.
 *
 * Author: Mouhammed Fakhro
 */
public class InventoryController {
    private TilePane inventoryPane;
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

    /**
     * Constructor to initialize the InventoryController.
     *
     * @param mainController        the main controller
     * @param clientController      the client controller
     * @param inventoryPane         the tile pane for inventory items
     * @param inventoryView         the group for inventory view
     * @param plantInformationPopup the group for plant information popup
     * @param plantSeedButton       the button to plant a seed
     * @param cancelPlantSeed       the button to cancel planting
     * @param disposeItemButton     the button to dispose of an item
     * @param putInRoomButton       the button to put an item in the room
     */
    public InventoryController(MainController mainController, Controller clientController, TilePane inventoryPane, Group inventoryView, Group plantInformationPopup, ImageView plantSeedButton, ImageView cancelPlantSeed, ImageView disposeItemButton, ImageView putInRoomButton) {
        this.mainController = mainController;
        this.inventoryPane = inventoryPane;
        this.clientController = clientController;
        this.plantInformationPopup = plantInformationPopup;
        this.plantSeedButton = plantSeedButton;
        this.cancelPlantSeed = cancelPlantSeed;
        this.disposeItemButton = disposeItemButton;
        this.putInRoomButton = putInRoomButton;

        inventoryContent = new Inventory(inventoryPane, this, plantInformationPopup, inventoryView);
        showCategory("Plants");
        setupDetections();
    }

    /**
     * Starts the planting process.
     */
    private void startPlanting() {
        isPlanting = true;
        plantSeedButton.setVisible(false);
        plantSeedButton.setDisable(true);

        cancelPlantSeed.setVisible(true);
        cancelPlantSeed.setDisable(false);
        cancelPlantSeed.setOpacity(1);

        showCategory("Pots");
        if (getObjectFromButton(selectedItem) instanceof Seed) {
            pickedSeedForPlanting = (Seed) getObjectFromButton(selectedItem);
        }
        selectPotForPlanting();
    }

    /**
     * Cancels the planting process.
     */
    private void cancelPlant() {
        plantSeedButton.setVisible(true);
        plantSeedButton.setDisable(false);
        plantSeedButton.setOpacity(1);

        cancelPlantSeed.setVisible(false);
        cancelPlantSeed.setDisable(true);
        cancelPlantSeed.setOpacity(0);
        isPlanting = false;
    }

    /**
     * Animates the popup frame.
     *
     * @param bool true to open the popup, false to close it
     */
    public void animatePopupFrame(boolean bool) {
        inventoryContent.animatePopupFrame(bool);
        plantInformationPopupIsOpened = bool;
    }

    /**
     * Sets up event handlers for buttons.
     */
    private void setupDetections() {
        plantSeedButton.setOnMouseClicked(event -> startPlanting());
        cancelPlantSeed.setOnMouseClicked(event -> cancelPlant());
        disposeItemButton.setOnMouseClicked(event -> disposeItem());
        putInRoomButton.setOnMouseClicked(event -> handlePutInRoomButton());
    }

    /**
     * Handles the put in room button click event.
     */
    private void handlePutInRoomButton() {
        if (getObjectFromButton(selectedItem) instanceof PottedPlant) {
            pickedPottedPlantToPutInRoom = (PottedPlant) getObjectFromButton(selectedItem);
            int inventoryIndex = clientController.getInventoryPlants().indexOf(pickedPottedPlantToPutInRoom);
            clientController.placePlantInRoom(inventoryIndex);
            clientController.disposePlantFromInventory(inventoryIndex);

            inventoryPane.getChildren().remove(selectedItem);
            populateOwnedItemsArray();
            updateInventoryButtons();
        }
    }

    /**
     * Sets up click event handlers for pot items to handle planting.
     */
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

    /**
     * Opens the inventory view and refreshes the items displayed.
     */
    public void openInventoryView() {
        deselectItem();
        populateOwnedItemsArray();
        updateInventoryButtons();
        showCategory(currentCategory);
    }

    /**
     * Shows items of the specified category in the inventory.
     *
     * @param category the category of items to display
     */
    public void showCategory(String category) {
        if (!isPlanting) {
            deselectItem();
        }
        currentCategory = category;
        ArrayList<Group> items;
        switch (category) {
            case "Seeds":
                items = seedItems;
                break;
            case "Pots":
                items = potItems;
                break;
            case "Decos":
                items = decoItems;
                break;
            default:
                items = plantItems;
        }

        inventoryPane.getChildren().clear();
        makeAllCategoriesInvisible(seedItems, potItems, decoItems, plantItems);

        for (Group item : items) {
            item.setVisible(true);
            inventoryPane.getChildren().add(item);
        }
    }

    /**
     * Populates the owned items arrays from the client controller.
     */
    private void populateOwnedItemsArray() {
        ownedSeeds = clientController.getInventorySeeds();
        ownedPots = clientController.getInventoryPots();
        ownedDecos = clientController.getInventoryDecos();
        ownedPottedPlants = clientController.getInventoryPlants();
    }

    /**
     * Clears all button arrays for inventory items.
     */
    private void clearAllButtonArrays() {
        seedItems.clear();
        potItems.clear();
        decoItems.clear();
        plantItems.clear();
    }

    /**
     * Updates the inventory buttons with the owned items.
     */
    private void updateInventoryButtons() {
        clearAllButtonArrays();
        for (Seed seed : ownedSeeds) {
            Group seedItem = inventoryContent.createItemView(seed.imageFilePath, null, seed.getPrice(), seed.getName(), "Seeds");
            seedItems.add(seedItem);
        }
        for (Pot pot : ownedPots) {
            Group potItem = inventoryContent.createItemView(pot.imageFilePath, null, pot.getPrice(), pot.getName(), "Pots");
            potItems.add(potItem);
        }
        for (Deco deco : ownedDecos) {
            Group decoItem = inventoryContent.createItemView(deco.imageFilePath, null, deco.getPrice(), deco.getName(), "Deco");
            decoItems.add(decoItem);
        }
        for (PottedPlant plant : ownedPottedPlants) {
            Group plantItem = inventoryContent.createItemView(plant.getPlantTop().getImageFilePath(), plant.getPot().getImageFilePath(), plant.getPrice(), plant.getName(), "PottedPlant");
            plantItems.add(plantItem);
        }
    }

    /**
     * Makes all items in the specified categories invisible.
     *
     * @param seedItems the seed items
     * @param potItems  the pot items
     * @param decoItems the deco items
     * @param plantItems the plant items
     */
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

    /**
     * Deselects the currently selected item.
     */
    public void deselectItem() {
        inventoryContent.removeGlowFromAllItems(seedItems, potItems, decoItems, plantItems);
        this.selectedItem = null;
        disposeItemButton.setOpacity(0.3);
        plantSeedButton.setOpacity(0.3);
        plantSeedButton.setVisible(true);
        cancelPlantSeed.setVisible(false);
    }

    /**
     * Handles item click events.
     *
     * @param item the clicked item
     */
    private void itemClick(Group item) {
        selectedItem = item;
        inventoryContent.removeGlowFromAllItems(seedItems, potItems, decoItems, plantItems);
        if (!selectedItem.getStyleClass().contains("purpleGlow")) {
            selectedItem.getStyleClass().add("purpleGlow");
            disposeItemButton.setOpacity(1);

            switch (getItemTypeFromButton(item)) {
                case "Seeds":
                    plantSeedButton.setVisible(true);
                    plantSeedButton.setOpacity(1);
                    plantSeedButton.setDisable(false);
                    plantSeedButton.toFront();
                    break;
                case "Plants":
                    plantSeedButton.setVisible(false);
                    plantSeedButton.setDisable(true);

                    putInRoomButton.setDisable(false);
                    putInRoomButton.setVisible(true);
                    putInRoomButton.setOpacity(1);
                    putInRoomButton.toFront();
                    break;
                default:
                    putInRoomButton.setVisible(false);
                    putInRoomButton.setDisable(true);
                    plantSeedButton.setOpacity(0.3);
                    plantSeedButton.setDisable(false);
            }
        }
        inventoryContent.addButtonGlow(selectedItem);
    }

    /**
     * Retrieves the item object from the button.
     *
     * @param button the button representing the item
     * @return the item object
     */
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

    /**
     * Retrieves the item type from the button.
     *
     * @param button the button representing the item
     * @return the item type as a string
     */
    public String getItemTypeFromButton(Group button) {
        if (potItems.contains(button)) {
            return "Pots";
        } else if (seedItems.contains(button)) {
            return "Seeds";
        } else if (decoItems.contains(button)) {
            return "Decos";
        } else {
            return "Plants";
        }
    }

    /**
     * Sets up click event handlers for inventory items and information buttons.
     *
     * @param item       the inventory item
     * @param infoButton the information button for the item
     */
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

    /**
     * Disposes of the currently selected item.
     */
    public void disposeItem() {
        if (selectedItem != null) {
            Item item = getObjectFromButton(selectedItem);

            if (item instanceof Seed) {
                int index = ownedSeeds.indexOf(item);
                clientController.disposeSeedFromInventory(index);
            } else if (item instanceof Pot) {
                int index = ownedPots.indexOf(item);
                clientController.disposePotFromInventory(index);
            } else if (item instanceof Deco) {
                int index = ownedDecos.indexOf(item);
                clientController.disposeDecoFromInventory(index);
            } else if (item instanceof PottedPlant) {
                int index = ownedPottedPlants.indexOf(item);
                clientController.disposePlantFromInventory(index);
            }
            mainController.updateUserCoins();
            openInventoryView();
        }
    }
}
