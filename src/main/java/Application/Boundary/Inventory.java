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

public class Inventory {

    private TilePane inventoryPane;
    private InventoryController inventoryController;
    private Group plantInformationPopup;
    private Group inventoryView;

    public Inventory(TilePane inventoryPane, InventoryController inventoryController, Group plantInformationPopup,
                     Group inventoryView) {
        this.inventoryPane = inventoryPane;
        this.inventoryController = inventoryController;
        this.plantInformationPopup = plantInformationPopup;
        this.inventoryView = inventoryView;
    }

    public ImageView createImageViewForItem(String itemImageFilePath) {
        Image itemImage = new Image(getClass().getClassLoader().getResource(itemImageFilePath).toString());
        return new ImageView(itemImage);
    }

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

            imageView.setLayoutX(230);
            imageView.setLayoutY(60);
        } else if (itemType.equals("Deco")) {
            imageView.setFitHeight(125);
            imageView.setFitWidth(125);

            imageView.setLayoutX(230);
            imageView.setLayoutY(60);
        } else if(itemType.equals("PottedPlant")) {
            imageView.setFitHeight(125);
            imageView.setFitWidth(125);

            imageView.setLayoutX(250);
            imageView.setLayoutY(60);
        }
    }

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
                itemPriceText.setText("Price: " + String.valueOf(item.getPrice()));
                if (itemDescriptionText.getText().isEmpty()) {
                    itemDescriptionText.setText("No description available for this item.");
                }
            }
        }
    }

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

    public Group createItemView(String itemImagePath, String itemImagePath2, double price, String name, String itemType) {
        Group container = new Group();
        if (itemType.equals("PottedPlant")) {
            setUpPottedPlantImages(container, itemImagePath, itemImagePath2, name);
        } else {
            setUpItemsImages(container, itemImagePath, price, name, itemType);
        }

        return container;
    }

    public void addButtonGlow(Node node) {
        if (!node.getStyleClass().contains("purpleGlow")) {
            node.getStyleClass().add("purpleGlow");
        }
    }

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
