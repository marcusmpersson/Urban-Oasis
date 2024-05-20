package main.java.Application.Controllers;

import builder.ItemBuilder;
import builder.PlantTopBuilder;
import controller.Controller;
import entities.*;
import enums.PotType;
import enums.Species;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Application.View.PlantInformationPopup;
import main.java.Application.View.RoomPlants;

import java.util.*;
import java.util.List;

public class RoomController {
    private ArrayList<Group> roomPlants;
    private Group roomView;
    private Room room;
    private WidgetHandler widgetController;
    private Map<Group, Boolean> widgetStatus;
    private User user;
    private RoomPlants roomPlantsGuiGenerating;
    private Group plantInformationView;
    private PlantInformationPopup plantInformationClass;
    public Boolean plantDetailsFrameIsOpened = false;
    public Boolean anyOverlap = false;
    public PottedPlant selectedPottedPlant;
    public Group selectedPottedPlantButton;

    public boolean isDragging = false;

    public RoomController(Group roomView, User user) {
        this.roomView = roomView;
        this.user = user;
        this.roomPlants = new ArrayList<>();
        this.room = user.getRoom(0);
        this.widgetController = new WidgetHandler(this);
        this.widgetStatus = new HashMap<>();
        this.roomPlantsGuiGenerating = new RoomPlants(roomView);

        this.plantInformationClass = new PlantInformationPopup(this);
        this.plantInformationView = plantInformationClass.getInformationRectangle();

        spawnUserPottedPlants();
    }

    public PottedPlant getPottedPlantEmptySlot() {
        Pot pot = ItemBuilder.buildPot(PotType.POT_STRIPED_BLUE);
        PlantTop plantTop = PlantTopBuilder.buildPlantTop(Species.COFFEE_PLANT);
        plantTop.raiseAge(440);
        PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

        return pottedPlant;
    }

    public Group addPottedPlantToSlot(PottedPlant pottedPlant, int posX, int posY) {
        String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
        String potFilePath = pottedPlant.getPot().getImageFilePath();

        Group plantContainer = roomPlantsGuiGenerating.generateRoomPlants(plantFilePath, potFilePath, posX,
                posY);

        return plantContainer;
    }

    public Group addEmptySpotPottedPlant(PottedPlant pottedPlant, int posX, int posY) {
        String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
        String potFilePath = pottedPlant.getPot().getImageFilePath();

        Group plantContainer = roomPlantsGuiGenerating.generateEmptySpotsPottedPlants(plantFilePath, potFilePath, posX,
                posY);

        return plantContainer;
    }

    private void spawnUserPottedPlants () {
        List<PlacementSlot> slots = user.getRoom(0).getSlots();

        for (int i = 0; i < slots.size(); i++) {
            int posX = slots.get(i).getX();
            int posY = slots.get(i).getY();

            if (slots.get(i).checkSlotTaken()) {
                PottedPlant pottedPlant = (PottedPlant) slots.get(i).getPlacedItem();

                Group plantContainer = addPottedPlantToSlot(pottedPlant, posX, posY);

                plantContainer.setId("ActivePlant");
                plantContainer.getProperties().put("Slot", slots.get(i));
                plantContainer.getProperties().put("PottedPlant", pottedPlant);
                plantContainer.getProperties().put("SlotIndex", i);
                plantContainer.getProperties().put("StageId", UUID.randomUUID().toString());

                roomPlants.add(plantContainer);

                setupDraggableSlot(plantContainer);
                setupMouseClick(plantContainer);
            } else {

                PottedPlant pottedPlant = getPottedPlantEmptySlot();
                Group plantContainer = addEmptySpotPottedPlant(pottedPlant, posX, posY);
                plantContainer.setId("EmptySlot");
                plantContainer.getProperties().put("Slot", slots.get(i));
                plantContainer.getProperties().put("SlotIndex", i);
                roomPlants.add(plantContainer);

            }
        }
    }

    private void updateAllSlots(boolean reset) {
        if (reset) {
            roomPlants = new ArrayList<>();

            roomView.getChildren().removeIf(node -> !(node instanceof ImageView));
            spawnUserPottedPlants();
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

    public void removeWidget(PottedPlant pottedPlant) {
        for (int i = 0; i < roomPlants.size(); i++) {
            Group plant = roomPlants.get(i);
            PlacementSlot slot = (PlacementSlot) plant.getProperties().get("Slot");
            PottedPlant pottedSlot = (PottedPlant) slot.getPlacedItem();
            if (pottedPlant == pottedSlot) {
                plant.setId("");
                plant.getStyleClass().remove("glow");
            }

        }

    }

    private void setupMouseClick(Group group) {

        group.setOnMouseClicked(event -> {
            if (!plantDetailsFrameIsOpened && !isDragging) {
                Group infoRectangleGroup = plantInformationClass.generateInformationRectangle();
                roomView.getChildren().add(infoRectangleGroup);
                plantInformationClass.animateToPosition(infoRectangleGroup, 668, 458);
                setMenuOpened(true);
                setSelectedPottedPlant(group);
            }
        });
    }

    private void setSelectedPottedPlant(Group container) {
        PlacementSlot slot = (PlacementSlot) container.getProperties().get("Slot");
        if (slot != null) {
            PottedPlant pottedPlant = (PottedPlant) slot.getPlacedItem();
            if (pottedPlant != null) {
                this.selectedPottedPlant = pottedPlant;
                this.selectedPottedPlantButton = container;
            }
        }

    }

    private void updatePlantImage(Group button, PottedPlant pottedPlant) {
        ImageView plantImageView = null;
        for (Node node : button.getChildren()) {
            if (node instanceof ImageView && node.getId() != null && node.getId().equals("PlantImage")) {
                plantImageView = (ImageView) node;
            }
        }
        if (plantImageView != null) {
            Image newImage = new Image(pottedPlant.getPlantTop().getImageFilePath());
            plantImageView.setImage(newImage);
        }
    }

    public String waterPottedPlant () {
        if (plantDetailsFrameIsOpened && selectedPottedPlant != null) {
            selectedPottedPlant.getPlantTop().water();
            String currentWaterLevel = String.valueOf(selectedPottedPlant.getPlantTop().getHealthStat().getWaterLevel());

            updatePlantImage(selectedPottedPlantButton, selectedPottedPlant);
            widgetController.updatePlantImage(selectedPottedPlant);
            return currentWaterLevel;
        }
        return null;
    }

    public void setPottedPlantToDead(PottedPlant selectedPottedPlant) {
        for (int i = 0; i < roomPlants.size(); i++) {
            Group plant = roomPlants.get(i);
            PottedPlant foundPottedPlant = (PottedPlant) plant.getProperties().get("PottedPlant");
            if (foundPottedPlant != null) {
                if (foundPottedPlant == selectedPottedPlant) {
                    updatePlantImage(plant, selectedPottedPlant);
                }
            }
        }
    }

    public void takeOutPottedPlant() {
        if (plantDetailsFrameIsOpened && selectedPottedPlant != null) {
            if (selectedPottedPlantButton != null) {
                if (!selectedPottedPlantButton.getId().equals("IsWidget")) {
                    selectedPottedPlantButton.setId("isWidget");
                    selectedPottedPlantButton.getStyleClass().add("glow");

                    String stageId = (String) selectedPottedPlantButton.getProperties().get("StageId");

                    widgetController.addWidget(selectedPottedPlant, stageId);
                }
            }
        }
    }

    public void setMenuOpened(boolean bool) {
        plantDetailsFrameIsOpened = bool;
    }

    private void setupDraggableSlot(Group group) {
        final double[] initialX = new double[1];
        final double[] initialY = new double[1];

        group.setOnMousePressed(event -> { // get the positions for the plants when touching with mouse
            if (!plantDetailsFrameIsOpened) {
                initialX[0] = group.getTranslateX() - event.getScreenX();
                initialY[0] = group.getTranslateY() - event.getScreenY();
            }
        });

        group.setOnMouseDragged(event -> {
            if (!plantDetailsFrameIsOpened) {
                if (!group.getStyleClass().contains("glow")) {
                    group.getStyleClass().add("glow");
                }
                group.setTranslateX(event.getScreenX() + initialX[0]);
                group.setTranslateY(event.getScreenY() + initialY[0]);

                this.isDragging = true;

                for (int i = 0; i < roomPlants.size(); i++) {
                    Group plant = roomPlants.get(i);
                    if (!plant.equals(group)) {
                        if (group.getBoundsInParent().intersects(plant.getBoundsInParent())) {
                            if (!anyOverlap) {  // Only the first detected overlap is handled
                                plant.getStyleClass().add("glow");
                                plant.setId("Overlapped");
                                anyOverlap = true;
                            }
                        } else {
                            if (plant.getStyleClass().contains("glow")) { // detect if you were previously moving from a
                                // glowing plant
                                anyOverlap = false;
                            }
                            plant.getStyleClass().remove("glow");
                            plant.setId("");

                        }
                    }
                }
            }
        });

        group.setOnMouseReleased(event -> {

            group.getStyleClass().remove("glow");

            for (int i = 0; i < roomPlants.size(); i++) {
                Group plant = roomPlants.get(i);
                if (plant.getStyleClass().contains("glow")) {
                    plant.getStyleClass().remove("glow");
                }
                if (plant.getId() != null && plant.getId().equals("Overlapped")) {

                    int draggingIndex = (int) group.getProperties().get("SlotIndex");
                    int droppingIndex = (int) plant.getProperties().get("SlotIndex");

                    Controller clientController = Controller.getInstance();
                    clientController.swapItems(draggingIndex, droppingIndex);
                    updateAllSlots(true);
                }
            }
            updateAllSlots(false);
            this.isDragging = false;
            this.anyOverlap = false;
        });
    }
}