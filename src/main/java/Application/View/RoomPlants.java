package main.java.Application.View;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

        return plantContainer;
    }

}
