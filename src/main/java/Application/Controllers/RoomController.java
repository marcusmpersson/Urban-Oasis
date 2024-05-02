package main.java.Application.Controllers;

import Builders.ItemBuilder;
import Builders.PlantTopBuilder;
import Builders.RoomBuilder;
import Controllers.Controller;
import entities.*;
import enums.PotType;
import enums.Species;
import enums.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Application.View.RoomPlants;

import java.util.*;

public class RoomController {
    private ArrayList<Group> roomPlants;
    private Group roomView;
    private Room room;
    private WidgetHandler widgetController;
    private Map<Group, Boolean> widgetStatus;
    private User user;
    private RoomPlants roomPlantsGuiGenerating;
    public RoomController(Group roomView, User user) {
        this.roomView = roomView;
        this.user = user;
        this.roomPlants = new ArrayList<>();
        this.room = user.getRoom(0);
        this.widgetController = new WidgetHandler();
        this.widgetStatus = new HashMap<>();
        this.roomPlantsGuiGenerating = new RoomPlants(roomView);

        getUserPottedPlants();
    }

    private void getUserPottedPlants() {
        List<PlacementSlot> slots = user.getRoom(0).getSlots();

        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).checkSlotTaken()) {
                PottedPlant pottedPlant = (PottedPlant) slots.get(i).getPlacedItem();
                int posX = slots.get(i).getX();
                int posY = slots.get(i).getY();
                String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
                String potFilePath = pottedPlant.getPot().getImageFilePath();
                String stageId = UUID.randomUUID().toString();

                Group plantContainer = roomPlantsGuiGenerating.generateRoomPlants(plantFilePath, potFilePath, posX,
                        posY);

                plantContainer.setId("ActivePlant");
                plantContainer.getProperties().put("Slot", slots.get(i));
                plantContainer.getProperties().put("SlotIndex", i);

                roomPlants.add(plantContainer);

                setupDraggableSlot(plantContainer);

               /*
                plantContainer.setOnMouseClicked(event -> {
                    if (!"isWidget".equals(plantContainer.getId())) {
                        plantContainer.setId("isWidget");
                        plantContainer.getStyleClass().add("glow");
                      //  widgetController.setWidget(plantImage, potImage, stageId);
                    } else {
                        plantContainer.setId("");
                        plantContainer.getStyleClass().remove("glow");
                        //widgetController.removeWidget(stageId);
                    }
                });
                */
            }
        }
    }

    private void updateAllSlots(boolean reset) {
        if (reset) {
            roomPlants = new ArrayList<>();

            roomView.getChildren().removeIf(node -> !(node instanceof ImageView));
            getUserPottedPlants();
        } else {
            for (int i = 0; i < roomPlants.size(); i++) {
                Group plant = roomPlants.get(i);
                PlacementSlot slot = (PlacementSlot) plant.getProperties().get("Slot");

                plant.setTranslateX(0);
                plant.setTranslateY(0);

                plant.setLayoutX(slot.getX());
                plant.setLayoutY(slot.getY());
            }
        }
    }


    private void setupDraggableSlot(Group group) {
        final double[] initialX = new double[1];
        final double[] initialY = new double[1];

        group.setOnMousePressed(event -> {

            initialX[0] = group.getTranslateX() - event.getScreenX();
            initialY[0] = group.getTranslateY() - event.getScreenY();
        });

        group.setOnMouseDragged(event -> {
            if (!group.getStyleClass().contains("glow")) {
                group.getStyleClass().add("glow");
            }
            group.setTranslateX(event.getScreenX() + initialX[0]);
            group.setTranslateY(event.getScreenY() + initialY[0]);

            for (int i = 0; i < roomPlants.size(); i++) {
                Group plant = roomPlants.get(i);
                if (!plant.equals(group)) {
                    if (group.getBoundsInParent().intersects(plant.getBoundsInParent())) {
                        if (!plant.getStyleClass().contains("glow")) {
                            plant.getStyleClass().add("glow");
                            plant.setId("Overlapped");
                        }
                    } else {
                        plant.getStyleClass().remove("glow");
                        plant.setId("");
                    }
                }
            }
        });

        group.setOnMouseReleased(event -> {
            System.out.println("Released.");
            group.getStyleClass().remove("glow");

            for (int i = 0; i < roomPlants.size(); i++) {
                Group plant = roomPlants.get(i);
                if (plant.getStyleClass().contains("glow")) {
                    plant.getStyleClass().remove("glow");
                }
                if (plant.getId() != null && plant.getId().equals("Overlapped")) {
                    System.out.println("Overlapped.");


                    int draggingIndex = (int) group.getProperties().get("SlotIndex");
                    int droppingIndex = (int) plant.getProperties().get("SlotIndex");

                    System.out.println(draggingIndex);
                    System.out.println(droppingIndex);

                    Controller clientController = Controller.getInstance();
                    clientController.swapItems(draggingIndex, droppingIndex);
                    updateAllSlots(true);
                }
            }
            updateAllSlots(false);



        });
    }
}
