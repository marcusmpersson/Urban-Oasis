package main.java.Application.Boundary;

import entities.Item;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.java.Application.Controllers.InventoryController;

import java.util.ArrayList;

/**
 * The Inventory class is responsible for managing the display and interactions
 * with the inventory items in the application. It includes methods to create item views,
 * animate popup frames, and handle item selections.
 *
 * Author: Mouhammed Fakhro
 */
public class Inventory {

    private TilePane inventoryPane;
    private InventoryController inventoryController;
    private Group plantInformationPopup;
    private Group inventoryView;

    /**
     * Constructs an Inventory object.
     *
     * @param inventoryPane          the TilePane for displaying inventory items
     * @param inventoryController    the controller managing the inventory
     * @param plantInformationPopup  the Group for the plant information popup
     * @param inventoryView          the Group for the inventory view
     */
    public Inventory(TilePane inventoryPane, InventoryController inventoryController, Group plantInformationPopup,
                     Group inventoryView) {
        this.inventoryPane = inventoryPane;
        this.inventoryController = inventoryController;
        this.plantInformationPopup = plantInformationPopup;
        this.inventoryView = inventoryView;
    }

    /**
     * Creates an ImageView for an item using the provided file path.
     *
     * @param itemImageFilePath the file path of the item's image
     * @return the created ImageView
     */
    public ImageView createImageViewForItem(String itemImageFilePath) {
        Image itemImage = new Image(getClass().getClassLoader().getResource(itemImageFilePath).toString());
        return new ImageView(itemImage);
    }

    /**
     * Animates the popup frame to open or close.
     *
     * @param open true to open the popup, false to close it
     */
    public void animatePopupFrame(boolean open) {
        double fromY;
        double toY;
        if (open) {
            fromY = 1000 - plantInformationPopup.getLayoutY();
            toY = 180 - plantInformationPopup.getLayoutY();
        } else {
            fromY = 180 - plantInformationPopup.getLayoutY();
            toY = 1000 - plantInformationPopup.getLayoutY();
        }

        TranslateTransition transition = new TranslateTransition(Duration.millis(500), plantInformationPopup);
        transition.setFromY(fromY);
        transition.setToY(toY);
        transition.play();

        if (open) {
            GaussianBlur blur = new GaussianBlur(30);
            inventoryView.setEffect(blur);
        } else {
            inventoryView.setEffect(null);
        }
    }

    /**
     * Sets the size and position of an image view in the store.
     *
     * @param imageView the ImageView to set the size and position for
     * @param itemType  the type of item
     */
    public void setImageViewSizeAndPositionStore(ImageView imageView, String itemType) {
        imageView.setPreserveRatio(true);
        switch (itemType) {
            case "Seeds":
                imageView.setFitHeight(90);
                imageView.setFitWidth(90);
                imageView.setLayoutX(55);
                imageView.setLayoutY(30);
                break;
            case "Pots":
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                imageView.setLayoutX(41);
                imageView.setLayoutY(45);
                break;
            default:
                imageView.setFitHeight(130);
                imageView.setFitWidth(130);
                imageView.setLayoutX(55);
                imageView.setLayoutY(0);
                break;
        }
    }

    /**
     * Sets the size and position of an image view in the popup.
     *
     * @param itemType  the type of item
     * @param imageView the ImageView to set the size and position for
     * @author Mouhammed Fakhro
     * @author Christian Storck
     */
    public void setImageViewSizeAndPositionPopup(String itemType, ImageView imageView) {
        imageView.setPreserveRatio(true);
        switch (itemType) {
            case "Seeds":
                imageView.setFitHeight(125);
                imageView.setFitWidth(125);
                imageView.setLayoutX(250);
                imageView.setLayoutY(50);
                break;
            case "Pots":
                imageView.setFitHeight(145);
                imageView.setFitWidth(145);
                imageView.setLayoutX(230);
                imageView.setLayoutY(60);
                break;
            case "Deco":
                imageView.setFitHeight(125);
                imageView.setFitWidth(125);
                imageView.setLayoutX(230);
                imageView.setLayoutY(60);
                break;
            case "PottedPlant":
                imageView.setFitHeight(125);
                imageView.setFitWidth(125);
                imageView.setLayoutX(250);
                imageView.setLayoutY(60);
                break;
        }
    }

    /**
     * Opens the plant information popup for the selected item.
     *
     * @param selectedItem the selected item
     */
    public void openPlantInformationPopup(Group selectedItem) {
        if (selectedItem != null) {
            ImageView previousImageView = (ImageView) plantInformationPopup.lookup("#ItemImage");
            if (previousImageView != null) {
                plantInformationPopup.getChildren().remove(previousImageView);
            }

            Item item = inventoryController.getObjectFromButton(selectedItem);
            String itemType = inventoryController.getItemTypeFromButton(selectedItem);

            Text itemNameText = (Text) plantInformationPopup.lookup("#plantName");
            Text itemDescriptionText = (Text) plantInformationPopup.lookup("#plantDescription");
            Text itemPriceText = (Text) plantInformationPopup.lookup("#price");

            String imagePath = (String) selectedItem.getProperties().get("ItemImagePath");
            System.out.println(imagePath);
            if (imagePath != null) {
                ImageView imageView = createImageViewForItem(imagePath);
                imageView.setId("ItemImage");
                plantInformationPopup.getChildren().add(imageView);
                setImageViewSizeAndPositionPopup(itemType, imageView);

                imageView.toFront();
            }

            if (itemNameText != null && itemDescriptionText != null && itemPriceText != null) {
                itemNameText.setText(item.getName());
                itemDescriptionText.setText(item.getDescriptionText());
                itemPriceText.setText("Price: " + item.getPrice());
                if (itemDescriptionText.getText().isEmpty()) {
                    itemDescriptionText.setText("No description available for this item.");
                }
            }
        }
    }

    /**
     * Sets up images and properties for inventory items.
     *
     * @param container     the container for the item view
     * @param itemImagePath the file path of the item's image
     * @param price         the price of the item
     * @param name          the name of the item
     * @param itemType      the type of item
     */
    private void setUpItemsImages(Group container, String itemImagePath, double price, String name, String itemType) {
        Image backgroundImage = new Image(getClass().getClassLoader().getResource("itemBackground.png").toString());
        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(192);
        background.setFitWidth(198);

        ImageView itemImageView = createImageViewForItem(itemImagePath);
        setImageViewSizeAndPositionStore(itemImageView, itemType);
        container.getProperties().put("ItemImagePath", itemImagePath);

        Text itemName = new Text(name);
        itemName.setWrappingWidth(150);
        itemName.setFill(Color.WHITE);
        itemName.setFont(Font.font("Pixeloid Sans", 15));
        itemName.setTextAlignment(TextAlignment.CENTER);
        itemName.setLayoutX(27);
        itemName.setLayoutY(145);

        Group buttonContainer = new Group();
        buttonContainer.setLayoutX(38);
        buttonContainer.setLayoutY(173);

        Image infoImage = new Image(getClass().getClassLoader().getResource("icons/info3.png").toString());
        ImageView info = new ImageView(infoImage);
        info.setScaleX(0.1);
        info.setScaleY(0.1);
        info.setLayoutY(-300);
        info.setLayoutX(-10);
        info.getProperties().put("Parent", container);

        buttonContainer.getChildren().addAll(info);

        container.getChildren().addAll(background, itemImageView, buttonContainer, itemName);
        container.setVisible(false);
        inventoryPane.getChildren().add(container);
        inventoryController.setupItemClicks(container, info);
    }

    /**
     * Sets up images and properties for potted plant items.
     *
     * @param container     the container for the item view
     * @param itemImagePath the file path of the plant's image
     * @param itemImagePath2 the file path of the pot's image
     * @param name          the name of the item
     */
    public void setUpPottedPlantImages(Group container, String itemImagePath, String itemImagePath2, String name) {
        Image backgroundImage = new Image(getClass().getClassLoader().getResource("itemBackground.png").toString());
        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(192);
        background.setFitWidth(198);

        Group plantImagesContainer = new Group();

        Image plantImage = new Image(getClass().getClassLoader().getResource(itemImagePath).toString());
        ImageView plantImageView = new ImageView(plantImage);
        plantImageView.setId("PlantImage");

        Image potImage = new Image(getClass().getClassLoader().getResource(itemImagePath2).toString());
        ImageView potImageView = new ImageView(potImage);
        potImageView.setId("PotImage");

        plantImageView.setFitHeight(100);
        plantImageView.setFitWidth(100);
        potImageView.setFitHeight(60);
        potImageView.setFitWidth(100);

        plantImageView.setLayoutY(potImageView.getLayoutY() - plantImageView.getFitHeight());

        plantImagesContainer.getChildren().addAll(plantImageView, potImageView);

        plantImagesContainer.setLayoutY(100);
        plantImagesContainer.setLayoutX(50);

        Text itemName = new Text(name);
        itemName.setWrappingWidth(150);
        itemName.setFill(Color.WHITE);
        itemName.setFont(Font.font("Pixeloid Sans", 15));
        itemName.setTextAlignment(TextAlignment.CENTER);
        itemName.setLayoutX(27);
        itemName.setLayoutY(180);

        Group buttonContainer = new Group();
        buttonContainer.setLayoutX(38);
        buttonContainer.setLayoutY(173);

        Image infoImage = new Image(getClass().getClassLoader().getResource("icons/info3.png").toString());
        ImageView info = new ImageView(infoImage);
        info.setScaleX(0.1);
        info.setScaleY(0.1);
        info.setLayoutY(-300);
        info.setLayoutX(-10);
        info.getProperties().put("Parent", container);

        buttonContainer.getChildren().addAll(info);

        container.getChildren().addAll(background, plantImagesContainer, buttonContainer, itemName);
        container.setVisible(false);
        inventoryPane.getChildren().add(container);

        inventoryController.setupItemClicks(container, info);
    }

    /**
     * Creates an item view with the given parameters.
     *
     * @param itemImagePath  the file path of the item's image
     * @param itemImagePath2 the file path of the second item's image (if applicable)
     * @param price          the price of the item
     * @param name           the name of the item
     * @param itemType       the type of item
     * @return the created Group representing the item view
     */
    public Group createItemView(String itemImagePath, String itemImagePath2, double price, String name, String itemType) {
        Group container = new Group();
        if (itemType.equals("PottedPlant")) {
            setUpPottedPlantImages(container, itemImagePath, itemImagePath2, name);
        } else {
            setUpItemsImages(container, itemImagePath, price, name, itemType);
        }

        return container;
    }

    /**
     * Adds a glow effect to the specified node.
     *
     * @param node the node to add the glow effect to
     */
    public void addButtonGlow(Node node) {
        if (!node.getStyleClass().contains("purpleGlow")) {
            node.getStyleClass().add("purpleGlow");
        }
    }

    /**
     * Removes the glow effect from all items in the specified categories.
     *
     * @param seedItems  the seed items
     * @param potItems   the pot items
     * @param decoItems  the deco items
     * @param plantItems the plant items
     */
    public void removeGlowFromAllItems(ArrayList<Group> seedItems, ArrayList<Group> potItems,
                                       ArrayList<Group> decoItems, ArrayList<Group> plantItems) {
        for (Group seed : seedItems) {
            seed.getStyleClass().remove("purpleGlow");
        }
        for (Group pot : potItems) {
            pot.getStyleClass().remove("purpleGlow");
        }
        for (Group deco : decoItems) {
            deco.getStyleClass().remove("purpleGlow");
        }
        for (Group plant : plantItems) {
            plant.getStyleClass().remove("purpleGlow");
        }
    }
}
