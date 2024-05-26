package main.java.Application.Controllers;

import builder.ItemBuilder;
import controller.Controller;
import entities.Item;
import entities.Pot;
import entities.Seed;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class StoreController {

    private ItemBuilder itemBuilder = new ItemBuilder();
    private Controller controller = Controller.getInstance();
    private TilePane shopPane;

    public StoreController (Group storeView, TilePane shopPane) {
        this.shopPane = shopPane;
        showAllItems("Pots");
    }

    private void showAllItems(String category) {
        if (category.equals("Seeds")) {
            ArrayList<Seed> seeds = controller.getShopSeeds();
            for (Seed seed : seeds) {
                createItemView(seed.imageFilePath, seed.getPrice(), seed.getName());
            }
        } else if (category.equals("Pots")) {
            ArrayList<Pot> pots = controller.getShopPots();
            for (Pot pot : pots) {
                createItemView(pot.imageFilePath, pot.getPrice(), pot.getName());
            }
        }

    }

    private void createItemView(String itemImagePath, double price, String name) {
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

        buttonContainer.getChildren().addAll(buttonImageView, priceText);

        container.getChildren().addAll(background, itemImageView, buttonContainer, itemName);


        shopPane.getChildren().add(container);

    }

}
