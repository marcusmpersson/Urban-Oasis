package main.java.Application.View;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.UUID;

public class RoomPlants {

    private Group roomView;

    public RoomPlants(Group roomView) {
        this.roomView = roomView;
    }


    public Group generateRoomPlants(String plantFilePath, String potFilePath, int posX, int posY) {
        Group plantContainer = new Group();

        Image plantImage = new Image(getClass().getClassLoader().getResource(plantFilePath).toString());
        ImageView plantImageView = new ImageView(plantImage);
        plantImageView.setId("PlantImage");

        Image potImage = new Image(getClass().getClassLoader().getResource(potFilePath).toString());
        ImageView potImageView = new ImageView(potImage);
        potImageView.setId("PotImage");

        plantImageView.setFitHeight(100);
        plantImageView.setFitWidth(100);
        potImageView.setFitHeight(60);
        potImageView.setFitWidth(100);

        potImageView.toBack();
        plantImageView.toFront();

        plantContainer.getChildren().addAll(potImageView, plantImageView);

        plantImageView.setLayoutY(potImageView.getLayoutY() - plantImageView.getFitHeight());

        plantContainer.setLayoutX(posX);
        plantContainer.setLayoutY(posY);

        roomView.getChildren().add(plantContainer);

        Image waterImage = new Image(getClass().getClassLoader().getResource("icons/watercan1.png").toString());
        ImageView waterImageView = new ImageView(waterImage);

        return plantContainer;
    }

    public Group generateEmptySpotsPottedPlants(String plantFilePath, String potFilePath, int posX, int posY) {

        Group plantContainer = new Group();

        Image plantImage = new Image(getClass().getClassLoader().getResource(plantFilePath).toString());
        ImageView plantImageView = new ImageView(plantImage);
        plantImageView.setId("PlantImage");
        plantImageView.setOpacity(0.1);

        Image potImage = new Image(getClass().getClassLoader().getResource(potFilePath).toString());
        ImageView potImageView = new ImageView(potImage);
        potImageView.setId("PotImage");
        potImageView.setOpacity(0.1);

        plantImageView.setFitHeight(100);
        plantImageView.setFitWidth(100);
        potImageView.setFitHeight(60);
        potImageView.setFitWidth(100);

        potImageView.toBack();
        plantImageView.toFront();

        plantContainer.getChildren().addAll(potImageView, plantImageView);

        plantImageView.setLayoutY(potImageView.getLayoutY() - plantImageView.getFitHeight());

        plantContainer.setLayoutX(posX);
        plantContainer.setLayoutY(posY);

        roomView.getChildren().add(plantContainer);

        return plantContainer;
    }

}
