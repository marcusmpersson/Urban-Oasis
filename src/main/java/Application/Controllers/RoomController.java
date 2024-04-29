package main.java.Application.Controllers;

import Builders.ItemBuilder;
import Builders.PlantTopBuilder;
import Builders.RoomBuilder;
import entities.*;
import enums.PotType;
import enums.Species;
import enums.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomController {

    private ArrayList<PlacementSlot> roomPlants;
    private Group roomView;
    private Room room;
    private WidgetController widgetController;
    private Map<Group, Boolean> widgetStatus;

    public RoomController(Group roomView) {
        this.roomView = roomView;
        this.roomPlants = new ArrayList<>();
        this.room = new RoomBuilder().buildCommonRoom();
        this.widgetController = new WidgetController();
        this.widgetStatus = new HashMap<>();
        createTestWidget();
    }

    public void createTestWidget() {

        for (int i = 25; i > 0; i--) {
            PlacementSlot nextAvailableSlot = room.getNextAvailableSlot();
            if (nextAvailableSlot != null) {
                Pot pot = ItemBuilder.buildPot(PotType.POT_POLKA_PINK);
                PlantTop plantTop = PlantTopBuilder.buildPlantTop(Species.COFFEE_PLANT);
                PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

                plantTop.setStage(Stage.ADULT);
                plantTop.raiseAge(440);

                nextAvailableSlot.setPlacedItem(pottedPlant);

                roomPlants.add(nextAvailableSlot);
            }
        }

        //displayWidgets();
        initiateRoom();
    }

    public void initiateRoom() {
        for (int i = 0; i < roomPlants.size(); i++) {

            Scene scene = roomView.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            }

            int posX = roomPlants.get(i).getX();
            int posY = roomPlants.get(i).getY();
            String stageId = UUID.randomUUID().toString();

            PottedPlant pottedPlant = (PottedPlant) roomPlants.get(i).getPlacedItem();
            String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
            String potFilePath = pottedPlant.getPot().getImageFilePath();

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

            System.out.println(plantContainer.getLayoutX() + ", " + plantContainer.getLayoutY());

            roomView.getChildren().add(plantContainer);

            plantContainer.setOnMouseClicked(event -> {
                if (!"isWidget".equals(plantContainer.getId())) {
                    plantContainer.setId("isWidget");
                    plantContainer.getStyleClass().add("glow");
                    widgetController.setWidget(plantImage, potImage, stageId);
                } else {
                    plantContainer.setId("");
                    plantContainer.getStyleClass().remove("glow");
                    widgetController.removeWidget(stageId);
                }
            });
        }
    }

    /*
    public void displayWidgets() {
        for (int i = 0; i < roomPlants.size(); i++) {

            int posX = roomPlants.get(i).getX();
            int posY = roomPlants.get(i).getY();

            String plantFilePath = roomPlants.get(i).getPlantFilePath();
            String potFilePath = roomPlants.get(i).getPotFilePath();

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
        }
    }
     */

}
