package main.java.Application.Controllers;

import builder.ItemBuilder;
import controller.Controller;
import entities.Deco;
import entities.Pot;
import entities.Seed;
import entities.ShopItem;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import main.java.Application.Boundary.Store;

import java.util.ArrayList;

/**
 * StoreController class manages the store's items and interactions.
 * It handles displaying items, purchasing items, and showing item details.
 *
 * Author: Mouhammed Fakhro
 */
public class StoreController {

    // Instances
    private ItemBuilder itemBuilder = new ItemBuilder();
    private Controller controller = Controller.getInstance();
    private MainController mainController;
    private Store storeContent;

    // UI Components
    private TilePane shopPane;
    private Group plantInformationPopup;
    private Group storeView;
    private Group selectedItem;
    private ImageView purchaseItemButton;
    private ImageView closePopupButton;
    private Text priceText;

    // Item Lists
    private ArrayList<Group> seedItems = new ArrayList<>();
    private ArrayList<Group> potItems = new ArrayList<>();
    private ArrayList<Group> decoItems = new ArrayList<>();

    // Shop Items
    private ArrayList<Seed> shopSeeds = controller.getShopSeeds();
    private ArrayList<Pot> shopPots = controller.getShopPots();
    private ArrayList<Deco> shopDecos = controller.getShopDecos();

    // State
    private boolean plantInformationPopupIsOpened = false;

    /**
     * Constructor for StoreController.
     *
     * @param mainController the main controller instance
     * @param storeView the root view of the store
     * @param shopPane the pane containing shop items
     * @param priceText the text displaying the item price
     * @param purchaseItemButton the button for purchasing items
     * @param plantInformationPopup the popup for displaying plant information
     * @param closePopupButton the button for closing the popup
     */
    public StoreController(MainController mainController, Group storeView, TilePane shopPane, Text priceText, ImageView purchaseItemButton, Group plantInformationPopup, ImageView closePopupButton) {
        this.mainController = mainController;
        this.storeView = storeView;
        this.shopPane = shopPane;
        this.priceText = priceText;
        this.plantInformationPopup = plantInformationPopup;
        this.purchaseItemButton = purchaseItemButton;
        this.closePopupButton = closePopupButton;
        this.storeContent = new Store(this, storeView, shopPane, plantInformationPopup);

        populateShopArrays();
        showCategory("Seeds");
        purchaseButtonClick();
        setupDetections();
    }

    /**
     * Sets up the purchase button click behavior.
     */
    private void purchaseButtonClick() {
        purchaseItemButton.setOnMouseEntered(event -> {
            storeContent.addButtonGlow(purchaseItemButton);
        });
        purchaseItemButton.setOnMouseExited(event -> {
            storeContent.removeButtonGlow(purchaseItemButton);
        });

        purchaseItemButton.setOnMouseClicked(event -> {
            if (selectedItem != null) {
                boolean success = false;
                int index = getIndexOfSelectedItem(selectedItem);
                String itemType = getItemTypeFromButton(selectedItem);
                if (itemType.equals("Pots")) {
                    success = controller.purchasePot(index);
                } else if (itemType.equals("Seeds")) {
                    success = controller.purchaseSeed(index);
                } else {
                    success = controller.purchaseDeco(index);
                }

                storeContent.purchaseResponseEffect(success);
                mainController.updateUserCoins();
            }
        });
    }

    /**
     * Sets up the close button click behavior.
     */
    private void setupDetections() {
        closePopupButton.setOnMouseClicked(event -> {
            storeContent.animatePopupFrame(false);
            plantInformationPopupIsOpened = false;
        });
    }

    /**
     * Closes the store's running content.
     */
    public void closeStoreRunningContent() {
        storeContent.animatePopupFrame(false);
        plantInformationPopupIsOpened = false;
        storeView.setEffect(null);
    }

    /**
     * Populates the shop item arrays.
     */
    private void populateShopArrays() {
        for (Seed seed : shopSeeds) {
            Group seedItem = storeContent.createItemView(seed.imageFilePath, seed.getPrice(), seed.getName(), "Seeds");
            seedItems.add(seedItem);
        }
        for (Pot pot : shopPots) {
            Group potItem = storeContent.createItemView(pot.imageFilePath, pot.getPrice(), pot.getName(), "Pots");
            potItems.add(potItem);
        }
        for (Deco deco : shopDecos) {
            Group decoItem = storeContent.createItemView(deco.imageFilePath, deco.getPrice(), deco.getName(), "Deco");
            decoItems.add(decoItem);
        }
    }

    /**
     * Shows the items of a specific category.
     *
     * @param category the category to show
     */
    public void showCategory(String category) {
        ArrayList<Group> items;
        if (category.equals("Seeds")) {
            items = seedItems;
        } else if (category.equals("Pots")) {
            items = potItems;
        } else {
            items = decoItems;
        }

        shopPane.getChildren().clear();
        storeContent.makeAllCategoriesInvisible(seedItems, potItems, decoItems);

        for (Group item : items) {
            item.setVisible(true);
            shopPane.getChildren().add(item);
        }
    }

    /**
     * Handles item click events.
     *
     * @param item the item that was clicked
     */
    private void itemClick(Group item) {
        selectedItem = item;
        storeContent.removeGlowFromAllItems(seedItems, potItems, decoItems);
        if (!selectedItem.getStyleClass().contains("purpleGlow")) {
            selectedItem.getStyleClass().add("purpleGlow");
        }
        storeContent.addButtonGlow(selectedItem);
        storeContent.displayPrice(selectedItem, priceText);
    }

    /**
     * Sets up item clicks for a specific item and info button.
     *
     * @param item the item to set up clicks for
     * @param infoButton the info button to set up clicks for
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
                storeContent.animatePopupFrame(true);
                plantInformationPopupIsOpened = true;
                storeContent.openPlantInformationPopup(selectedItem);
            }
        });
    }

    /**
     * Gets the index of the selected item.
     *
     * @param item the selected item
     * @return the index of the selected item
     */
    private int getIndexOfSelectedItem(Group item) {
        ArrayList<Group> array = getItemTypeArrayFromButton(item);
        if (array != null) {
            return array.indexOf(item);
        }
        return -1;
    }

    /**
     * Gets the array of items for a specific button.
     *
     * @param button the button to get the item array for
     * @return the array of items
     */
    private ArrayList<Group> getItemTypeArrayFromButton(Group button) {
        if (seedItems.contains(button)) {
            return seedItems;
        } else if (potItems.contains(button)) {
            return potItems;
        } else {
            return decoItems;
        }
    }

    /**
     * Gets the item type for a specific button.
     *
     * @param button the button to get the item type for
     * @return the item type
     */
    public String getItemTypeFromButton(Group button) {
        if (potItems.contains(button)) {
            return "Pots";
        } else if (seedItems.contains(button)) {
            return "Seeds";
        } else {
            return "Decos";
        }
    }

    /**
     * Gets the ShopItem object for a specific button.
     *
     * @param button the button to get the ShopItem for
     * @return the ShopItem object
     */
    public ShopItem getObjectFromButton(Group button) {
        if (seedItems.contains(button)) {
            return shopSeeds.get(seedItems.indexOf(button));
        } else if (potItems.contains(button)) {
            return shopPots.get(potItems.indexOf(button));
        } else {
            return shopDecos.get(decoItems.indexOf(button));
        }
    }
}
