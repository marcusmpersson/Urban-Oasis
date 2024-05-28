package main.java.Application.Boundary;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ItemView {

    public ItemView() {

    }

    public ImageView createImageViewForItem(String itemImageFilePath) {
        Image itemImage = new Image(getClass().getClassLoader().getResource(itemImageFilePath).toString());
        return new ImageView(itemImage);
    }

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
            imageView.setLayoutY(-15);
        }
    }

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

        return container;
    }
}
