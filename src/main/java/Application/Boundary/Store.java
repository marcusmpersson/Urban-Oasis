package main.java.Application.Boundary;

import entities.ShopItem;
import javafx.animation.FadeTransition;
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
import main.java.Application.Controllers.StoreController;

import java.util.ArrayList;

/**
 * Store class manages the display and interaction with store items.
 * It handles animations, item displays, and effects within the store.
 *
 * Author: Mouhammed Fakhro
 */
public class Store {

    // UI Components
    private Group plantInformationPopup;
    private Group storeView;
    private TilePane shopPane;

    // Controller
    private StoreController storeController;

    /**
     * Constructor for Store.
     *
     * @param storeController the store controller instance
     * @param storeView the root view of the store
     * @param shopPane the pane containing shop items
     * @param plantInformationPopup the popup for displaying plant information
     */
    public Store(StoreController storeController, Group storeView, TilePane shopPane, Group plantInformationPopup) {
        this.storeController = storeController;
        this.storeView = storeView;
        this.shopPane = shopPane;
        this.plantInformationPopup = plantInformationPopup;
    }

    /**
     * Adds a glow effect to a node.
     *
     * @param node the node to add the glow effect to
     */
    public void addButtonGlow(Node node) {
        if (!node.getStyleClass().contains("purpleGlow")) {
            node.getStyleClass().add("purpleGlow");
        }
    }

    /**
     * Removes the glow effect from an image view.
     *
     * @param button the image view to remove the glow effect from
     */
    public void removeButtonGlow(ImageView button) {
        button.getStyleClass().remove("purpleGlow");
    }

    /**
     * Animates the popup frame.
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
            storeView.setEffect(blur);
        } else {
            System.out.println("tried to removing blur.");
            storeView.setEffect(null);
        }
    }

    /**
     * Shows a purchase response effect.
     *
     * @param success true if the purchase was successful, false otherwise
     */
    public void purchaseResponseEffect(boolean success) {
        Image image;
        if (success) {
            image = new Image(getClass().getClassLoader().getResource("dancingcat.gif").toString());
        } else {
            image = new Image(getClass().getClassLoader().getResource("nomoney.gif").toString());
        }
        ImageView imageView = new ImageView(image);
        imageView.setScaleY(0.35);
        imageView.setScaleX(0.35);
        imageView.setLayoutX(225);
        imageView.setLayoutY(200);
        storeView.getChildren().add(imageView);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), imageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(1));

        fadeIn.setOnFinished(event -> fadeOut.play());

        fadeOut.setOnFinished(event -> storeView.getChildren().remove(imageView));

        fadeIn.play();
    }

    /**
     * Makes all categories invisible.
     *
     * @param seedItems the list of seed items
     * @param potItems the list of pot items
     * @param decoItems the list of deco items
     */
    public void makeAllCategoriesInvisible(ArrayList<Group> seedItems, ArrayList<Group> potItems, ArrayList<Group> decoItems) {
        for (Group seed : seedItems) {
            seed.setVisible(false);
        }
        for (Group pot : potItems) {
            pot.setVisible(false);
        }
        for (Group deco : decoItems) {
            deco.setVisible(false);
        }
    }

    /**
     * Creates an image view for an item.
     *
     * @param itemImageFilePath the file path of the item image
     * @return the created image view
     */
    public ImageView createImageViewForItem(String itemImageFilePath) {
        Image itemImage = new Image(getClass().getClassLoader().getResource(itemImageFilePath).toString());
        return new ImageView(itemImage);
    }

    /**
     * Sets the size and position of an image view in the store.
     *
     * @param imageView the image view to set the size and position for
     * @param itemType the type of item
     */
    public void setImageViewSizeAndPositionStore(ImageView imageView, String itemType) {
        imageView.setPreserveRatio(true);
        if (itemType.equals("Seeds")) {
            imageView.setFitHeight(90);
            imageView.setFitWidth(90);

            imageView.setLayoutX(55);
            imageView.setLayoutY(30);
        } else if (itemType.equals("Pots")) {
            imageView.setFitHeight(120);
            imageView.setFitWidth(120);

            imageView.setLayoutX(41);
            imageView.setLayoutY(45);
        } else {
            imageView.setFitHeight(130);
            imageView.setFitWidth(130);

            imageView.setLayoutX(55);
            imageView.setLayoutY(0);
        }
    }

    /**
     * Creates a view for an item.
     *
     * @param itemImagePath the file path of the item image
     * @param price the price of the item
     * @param name the name of the item
     * @param itemType the type of item
     * @return the created item view
     */
    public Group createItemView(String itemImagePath, double price, String name, String itemType) {
        Group container = new Group();

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

        Image buttonImage = new Image(getClass().getClassLoader().getResource("priceBackground.png").toString());
        ImageView buttonImageView = new ImageView(buttonImage);
        buttonImageView.setFitWidth(136);
        buttonImageView.setFitHeight(26);
        buttonImageView.setLayoutX(-7);
        buttonImageView.setLayoutY(-15);

        Text priceText = new Text(String.valueOf(price));
        priceText.setWrappingWidth(122);
        priceText.setFill(Color.WHITE);
        priceText.setFont(Font.font("Pixeloid Sans", 15));
        priceText.setTextAlignment(TextAlignment.CENTER);
        priceText.setLayoutX(1);
        priceText.setLayoutY(4);

        Image infoImage = new Image(getClass().getClassLoader().getResource("icons/info3.png").toString());
        ImageView info = new ImageView(infoImage);
        info.setScaleX(0.1);
        info.setScaleY(0.1);
        info.setLayoutY(-300);
        info.setLayoutX(-10);
        info.getProperties().put("Parent", container);

        buttonContainer.getChildren().addAll(buttonImageView, priceText, info);

        container.getChildren().addAll(background, itemImageView, buttonContainer, itemName);
        container.setVisible(false);
        shopPane.getChildren().add(container);

        storeController.setupItemClicks(container, info);

        return container;
    }

    /**
     * Sets the size and position of an image view in the popup.
     *
     * @param itemType the type of item
     * @param imageView the image view to set the size and position for
     */
    public void setImageViewSizeAndPositionPopup(String itemType, ImageView imageView) {
        imageView.setPreserveRatio(true);
        if (itemType.equals("Seeds")) {
            imageView.setFitHeight(125);
            imageView.setFitWidth(125);

            imageView.setLayoutX(250);
            imageView.setLayoutY(50);
        } else if (itemType.equals("Pots")) {
            imageView.setFitHeight(145);
            imageView.setFitWidth(145);

            imageView.setLayoutX(250);
            imageView.setLayoutY(40);
        } else {
            imageView.setFitHeight(160);
            imageView.setFitWidth(160);

            imageView.setLayoutX(260);
            imageView.setLayoutY(25);
        }
    }

    /**
     * Handles the actions when the plant information popup is opened.
     *
     * @param selectedItem the selected item
     */
    public void openPlantInformationPopup(Group selectedItem) {
        if (selectedItem != null) {
            ImageView previousImageView = (ImageView) plantInformationPopup.lookup("#ItemImage");
            if (previousImageView != null) {
                plantInformationPopup.getChildren().remove(previousImageView);
            }

            ShopItem item = storeController.getObjectFromButton(selectedItem);
            String itemType = storeController.getItemTypeFromButton(selectedItem);

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
                itemPriceText.setText("Price: " + String.valueOf(item.getPrice()));
                if (itemDescriptionText.getText().isEmpty()) {
                    itemDescriptionText.setText("No description available for this item.");
                }
            }
        }
    }

    /**
     * Removes the glow effect from all items.
     *
     * @param seedItems the list of seed items
     * @param potItems the list of pot items
     * @param decoItems the list of deco items
     */
    public void removeGlowFromAllItems(ArrayList<Group> seedItems, ArrayList<Group> potItems,
                                       ArrayList<Group> decoItems) {
        for (Group seed : seedItems) {
            seed.getStyleClass().remove("purpleGlow");
        }
        for (Group pot : potItems) {
            pot.getStyleClass().remove("purpleGlow");
        }
        for (Group deco : decoItems) {
            deco.getStyleClass().remove("purpleGlow");
        }
    }

    /**
     * Displays the price of an item.
     *
     * @param priceText the text node to display the price
     */
    public void displayPrice(int currentAmount, Text priceText) {
        priceText.setText(String.valueOf(currentAmount));
    }
}
