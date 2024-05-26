package main.java.Application.Controllers;

import builder.ItemBuilder;
import controller.Controller;
import entities.Deco;
import entities.Pot;
import entities.Seed;
import entities.ShopItem;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;

public class StoreController {

    private ItemBuilder itemBuilder = new ItemBuilder();
    private Controller controller = Controller.getInstance();
    private TilePane shopPane;
    private ArrayList<Group> seedItems = new ArrayList<>();
    private ArrayList<Group> potItems = new ArrayList<>();
    private ArrayList<Group> decoItems = new ArrayList<>();

    private ArrayList<Seed> shopSeeds = controller.getShopSeeds();
    private ArrayList<Pot> shopPots = controller.getShopPots();
    private ArrayList<Deco> shopDecos = controller.getShopDecos();
    private Group selectedItem;
    private ImageView purchaseItemButton;
    private Text priceText;
    private Group storeView;
    private MainController mainController;

    public StoreController (MainController mainController, Group storeView, TilePane shopPane, Text priceText, ImageView purchaseItemButton) {
        this.mainController = mainController;
        this.storeView = storeView;
        this.shopPane = shopPane;
        this.priceText = priceText;
        this.purchaseItemButton = purchaseItemButton;
        populateShopArrays();
        showCategory("Seeds");
        purchaseButtonClick();
    }

    private void purchaseButtonClick() {
        purchaseItemButton.setOnMouseEntered(event -> {
            if (!purchaseItemButton.getStyleClass().contains("purpleGlow")) {
                purchaseItemButton.getStyleClass().add("purpleGlow");
            }
        });
        purchaseItemButton.setOnMouseExited(event -> {
            purchaseItemButton.getStyleClass().remove("purpleGlow");
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

                purchaseEffect(success);
                mainController.updateUserCoins();
            }

        });
    }

    private void populateShopArrays() {
        for (Seed seed : shopSeeds) {
            Group seedItem = createItemView(seed.imageFilePath, seed.getPrice(), seed.getName());
            seedItems.add(seedItem);
        }
        for (Pot pot : shopPots) {
            Group potItem = createItemView(pot.imageFilePath, pot.getPrice(), pot.getName());
            potItems.add(potItem);
        }
        for (Deco deco : shopDecos) {
            Group decoItem = createItemView(deco.imageFilePath, deco.getPrice(), deco.getName());
            decoItems.add(decoItem);
        }
    }

    private void purchaseEffect(boolean success) {
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
        makeAllCategoriesInvisible();

        for (Group item : items) {
            item.setVisible(true);
            shopPane.getChildren().add(item);
        }
    }

    private void makeAllCategoriesInvisible() {
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

    private Group createItemView(String itemImagePath, double price, String name) {
        Group container = new Group();

        Image backgroundImage = new Image(getClass().getClassLoader().getResource("itemBackground.png").toString());
        ImageView background = new ImageView(backgroundImage);
        background.setFitHeight(192);
        background.setFitWidth(198);

        Image itemImage = new Image(getClass().getClassLoader().getResource(itemImagePath).toString());
        ImageView itemImageView = new ImageView(itemImage);
        itemImageView.setFitHeight(90);
        itemImageView.setFitWidth(90);
        itemImageView.setLayoutX(52);
        itemImageView.setLayoutY(30);

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

        /*
        Image coinImage = new Image(getClass().getClassLoader().getResource("icons/currency.png").toString());
        ImageView coin = new ImageView(coinImage);
        coin.setPreserveRatio(true);
        coin.setScaleX(0.1);
        coin.setScaleY(0.1);
        coin.setLayoutX(-5);
        coin.setLayoutY(-150);
         */

        Image infoImage = new Image(getClass().getClassLoader().getResource("icons/info3.png").toString());
        ImageView info = new ImageView(infoImage);
        info.setScaleX(0.1);
        info.setScaleY(0.1);
        info.setLayoutY(-300);
        info.setLayoutX(-10);

        buttonContainer.getChildren().addAll(buttonImageView, priceText, info);

        container.getChildren().addAll(background, itemImageView, buttonContainer, itemName);
        container.setVisible(false);
        shopPane.getChildren().add(container);

        setupItemClicks(container);

        return container;
    }

    private void setupItemClicks(Group item) {
        item.setOnMouseClicked(event -> {
            removeGlowFromAllItems();
            if (!item.getStyleClass().contains("purpleGlow")) {
                item.getStyleClass().add("purpleGlow");
            }
            selectedItem = item;
            displayPrice(item);
        });
    }

    private int getIndexOfSelectedItem(Group item) {
        ArrayList<Group> array = getItemTypeArrayFromButton(item);
        if (array != null) {
            return array.indexOf(item);
        }
        return  -1;
    }


    private ArrayList<Group> getItemTypeArrayFromButton(Group button) {
        if (seedItems.contains(button)) {
            return seedItems;
        } else if (potItems.contains(button)) {
            return potItems;
        } else {
            return decoItems;
        }
    }

    private String getItemTypeFromButton(Group button) {
        if (potItems.contains(button)) {
            return "Pots";
        } else if (seedItems.contains(button)) {
            return "Seeds";
        } else {
            return "Decos";
        }
    }

    private ShopItem getObjectFromButton(Group button) {
        if (seedItems.contains(button)) {
            return shopSeeds.get(seedItems.indexOf(button));
        } else if (potItems.contains(button)) {
            return shopPots.get(potItems.indexOf(button));
        } else {
            return shopDecos.get(decoItems.indexOf(button));
        }
    }

    private void displayPrice(Group item) {
        ShopItem itemObject = getObjectFromButton(item);
        priceText.setText(String.valueOf(itemObject.getPrice()));
    }

    private void removeGlowFromAllItems() {
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

}
