package main.java.Application.Controllers;

import controller.Controller;
import controller.GameHandler;
import entities.*;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.java.Application.Boundary.PlantInformationPopup;
import main.java.Application.Boundary.RoomPlants;

import java.util.*;
import java.util.List;

/**
 * RoomController handles the interactions and management of plants within a room.
 *
 * @Author Mouhammed Fakhro
 */
public class RoomController {
    private final Controller clientController;
    private ArrayList<Group> roomPlants = new ArrayList<>();
    private Group roomView;
    private Room room;
    private WidgetHandler widgetHandler = new WidgetHandler(this);
    private User user;
    private GameHandler gameHandler;
    private RoomPlants roomGuiContent;
    private Group plantInformationView;
    private PlantInformationPopup plantInformationPopup;
    private Boolean isPlantDetailsFrameOpened = false;
    private PottedPlant selectedPottedPlant;
    private Group selectedPottedPlantButton;
    private ArrayList<Rectangle> slotZones = new ArrayList<>();
    private ArrayList<ImageView> arrowIndicators = new ArrayList<>();
    private boolean isDragging = false;

    private InventoryController inventoryController;

    /**
     * Constructor for RoomController.
     *
     * @param roomView            the view for the room
     * @param user                the user associated with the room
     * @param inventoryController
     */
    public RoomController(Group roomView, User user, Controller clientController, InventoryController inventoryController) {
        this.clientController = clientController;
        this.roomView = roomView;
        this.user = user;
        this.room = user.getRoom(0);
        this.roomGuiContent = new RoomPlants(roomView);
        this.inventoryController = inventoryController;
        gameHandler = new GameHandler(this.user);
        spawnUserPottedPlants();
        plantInformationPopup = new PlantInformationPopup(this);
        this.plantInformationView = plantInformationPopup.getInformationRectangle();
        roomView.getChildren().add(plantInformationView);
    }

    /**
     * Adds a potted plant to a specified slot.
     *
     * @param pottedPlant the potted plant to add
     * @param posX the x-coordinate of the slot
     * @param posY the y-coordinate of the slot
     * @return the group representing the plant container
     */
    public Group addPottedPlantToSlot(PottedPlant pottedPlant, int posX, int posY) {
        String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
        String potFilePath = pottedPlant.getPot().getImageFilePath();

        return roomGuiContent.generateRoomPlants(plantFilePath, potFilePath, posX, posY);
    }

    private void updatePlantAndPotImages(Group plantButton, PottedPlant pottedPlant) {
        String plantFilePath = pottedPlant.getPlantTop().getImageFilePath();
        String potFilePath = pottedPlant.getPot().getImageFilePath();

        roomGuiContent.updatePlantandPotImages(plantButton, plantFilePath, potFilePath);
    }

    /**
     * Spawns user potted plants in their respective slots.
     */
    private void spawnUserPottedPlants() {
        List<PlacementSlot> slots = this.room.getSlots();

        for (int i = 0; i < slots.size(); i++) {

            int posX = slots.get(i).getX();
            int posY = slots.get(i).getY();

            ImageView arrow = roomGuiContent.addLocationArrowsToRoom(posX, posY, i);
            arrowIndicators.add(arrow);

            Rectangle zone = roomGuiContent.addSlotZonesToRoom(posX, posY);
            zone.getProperties().put("Slot", slots.get(i));
            zone.getProperties().put("SlotIndex", i);
            zone.getProperties().put("IsEmpty", !slots.get(i).checkSlotTaken());
            slotZones.add(zone);

            roomView.getChildren().addAll(zone, arrow);

            if (slots.get(i).checkSlotTaken()) {
                PottedPlant pottedPlant = (PottedPlant) slots.get(i).getPlacedItem();

                Group plantContainer = addPottedPlantToSlot(pottedPlant, posX, posY);
                plantContainer.getProperties().put("Slot", slots.get(i));
                plantContainer.getProperties().put("PottedPlant", pottedPlant);
                plantContainer.getProperties().put("SlotIndex", i);
                plantContainer.getProperties().put("StageId", UUID.randomUUID().toString());
                plantContainer.toFront();

                System.out.println("SLOT: " + i);
                roomPlants.add(plantContainer);
                setupMouseEvents(plantContainer);
            }
        }
    }

    public void updateAllPottedPlantImages() {
        for (Group plantButton : roomPlants) {
            PottedPlant pottedPlant = (PottedPlant) plantButton.getProperties().get("PottedPlant");
            if (pottedPlant != null) {
                updatePlantAndPotImages(plantButton, pottedPlant);
            }
        }
    }

    public void updateAvailablePlants() {
        List<PlacementSlot> slots = user.getRoom(0).getSlots();
        for (PlacementSlot slot : slots) {
            if (slot.checkSlotTaken()) {
                System.out.println(slot.getX() + " " + slot.getY());
            }

        }

    }

    /**
     * Sets up mouse events for a given plant group.
     *
     * @param group the plant group (button/image)
     */
    private void setupMouseEvents(Group group) {
        // Mouse click for the plant. It should open up the menu and update the content with correct water level
        // information.
        group.setOnMouseClicked(event -> {
            if (!isPlantDetailsFrameOpened && !isDragging) {
                plantInformationPopup.openInfoPopupFrame();

                setMenuOpened(true);
                setSelectedPottedPlant(group);

                PottedPlant currentPlant = getPottedPlantFromGroup(group);
                plantInformationPopup.updateWaterLevelText(getCurrentWaterLevel(currentPlant));
                plantInformationPopup.updateHealthLevelText(getCurrentHealthLevel());
            }
        });


        // this is used to track the initial X and Y position when dragging.
        final double[] initialX = new double[1];
        final double[] initialY = new double[1];

        // this is used to ensure that the arrows appear when holding a plant.
        group.setOnMousePressed(event -> {
            if (!isPlantDetailsFrameOpened) {
                initialX[0] = group.getTranslateX() - event.getScreenX();
                initialY[0] = group.getTranslateY() - event.getScreenY();
            }

            for (Rectangle rectangle : slotZones) {
                if (rectangle.getProperties().get("IsEmpty").equals(true)) {
                    arrowIndicators.get((Integer) rectangle.getProperties().get("SlotIndex")).setVisible(true);
                }
            }
        });

        // this is used to detect mouse drag for the plants.
        group.setOnMouseDragged(event -> {
            this.isDragging = true;
            roomGuiContent.addGlowToPlant(group);
            group.setTranslateX(event.getScreenX() + initialX[0]);
            group.setTranslateY(event.getScreenY() + initialY[0]);

            for (Rectangle emptySlot : slotZones) {
                if (group.getBoundsInParent().intersects(emptySlot.getBoundsInParent())) {
                    if (!group.getProperties().get("SlotIndex").equals(emptySlot.getProperties().get("SlotIndex"))) {
                        Group droppingPlant = getPlantFromRectangleDetection(emptySlot);
                        if (droppingPlant != null) {
                            roomGuiContent.addGlowToPlant(droppingPlant);
                        } else {
                            roomGuiContent.removeAllPlantGlows(roomPlants, group);
                        }
                        ImageView arrow = arrowIndicators.get((Integer) emptySlot.getProperties().get("SlotIndex"));
                        if (arrow.getEffect() == null) {
                            roomGuiContent.addColorEffectToRoomArrow(arrow);

                            for (ImageView gif : arrowIndicators) {
                                if (gif != arrow) {
                                    roomGuiContent.removeColorEffectFromArrow(gif);
                                }
                            }
                        }

                        group.getProperties().put("SlotDrop", emptySlot.getProperties().get("SlotIndex"));
                    }
                } else {
                    ImageView arrow = arrowIndicators.get((Integer) emptySlot.getProperties().get("SlotIndex"));
                    roomGuiContent.removeColorEffectFromArrow(arrow);
                }
            }
        });

        group.setOnMouseReleased(event -> {
            for (ImageView gif : arrowIndicators) {
                gif.setVisible(false);
                roomGuiContent.removeColorEffectFromArrow(gif);
            }

            for (Group roomPlant : roomPlants) {
                if (roomPlant.getProperties().get("SlotDrop") != null) {
                    int draggingIndex = (int) group.getProperties().get("SlotIndex");
                    int droppingIndex = (int) roomPlant.getProperties().get("SlotDrop");

                    Rectangle droppingSlot = slotZones.get(droppingIndex);

                    if (droppingSlot.getProperties().get("IsEmpty").equals(false)) {
                        Group switchingPlant;

                        for (Group roomPlant2 : roomPlants) {
                            if (roomPlant2.getProperties().get("SlotIndex").equals(droppingIndex)) {
                                PlacementSlot slot = (PlacementSlot) roomPlant2.getProperties().get("Slot");
                                switchingPlant = getRoomPlantFromPlacementSlot(slot);
                                switchPlantLocations(group, switchingPlant);
                                break;
                            }
                        }
                    } else {
                        updatePlantLocation(group, droppingIndex);
                    }

                    clientController.swapItems(draggingIndex, droppingIndex);
                } else {
                    updatePlantLocation(group, (Integer) group.getProperties().get("SlotIndex"));
                }
            }


            group.getProperties().put("SlotDrop", null);

            roomGuiContent.removeAllPlantGlows(roomPlants, null);
            checkForPottedPlantMisplacement();



            Thread isDraggingBooleanTask = new Thread(() -> { // we use a thread wait here in order to ensure that
                // the popup menu does not appear when dropping a plant after dragging it.
                try {
                    Thread.sleep(500);
                    isDragging = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            isDraggingBooleanTask.start();
        });
    }

    private void checkForPottedPlantMisplacement() {
        for (Group roomPlant : roomPlants) {
            PlacementSlot slot = null;
            if (roomPlant.getProperties().get("Slot") instanceof PlacementSlot) {
                slot = (PlacementSlot) roomPlant.getProperties().get("Slot");
            }
            if (slot != null) {
                if (roomPlant.getLayoutX() != slot.getX()) {
                    roomPlant.setLayoutX(slot.getX());
                }
                if (roomPlant.getLayoutY() != slot.getY()) {
                    roomPlant.setLayoutY(slot.getY());
                }
            }
        }
    }

    public void removeItemFromSlot(){
        if (isPlantDetailsFrameOpened && selectedPottedPlant != null) {
            if (selectedPottedPlantButton != null && !"IsWidget".equals(selectedPottedPlantButton.getId())) {
                int placementIndex = (int) selectedPottedPlantButton.getProperties().get("SlotIndex");

                clientController.removeItemFromSlot(placementIndex);
                roomView.getChildren().remove(selectedPottedPlantButton);
                roomPlants.remove(selectedPottedPlantButton);
            }
        }
    }

    public void resetRoom() {
        ParallelTransition parallelTransition = new ParallelTransition();

        for (ImageView arrowIndicator : arrowIndicators) {
            roomView.getChildren().remove(arrowIndicator);
        }

        for (Group roomPlant : roomPlants) {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), roomPlant);
            translateTransition.setByY(800);

            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), roomPlant);
            rotateTransition.setByAngle(360);

            ParallelTransition plantTransition = new ParallelTransition(translateTransition, rotateTransition);
            parallelTransition.getChildren().add(plantTransition);
        }

        for (Rectangle slotZone : slotZones) {
            roomView.getChildren().remove(slotZone);
        }

        parallelTransition.setOnFinished(event -> {
            for (Group roomPlant : roomPlants) {
                roomView.getChildren().remove(roomPlant);
            }
            arrowIndicators.clear();
            roomPlants.clear();
            slotZones.clear();

            spawnUserPottedPlants();
        });

        parallelTransition.play();
    }

    private void resetAllMouseEvents() {

        for (Group roomPlant : roomPlants) {
            roomPlant.setOnMouseClicked(null);
            roomPlant.setOnMousePressed(null);
            roomPlant.setOnMouseDragged(null);
            roomPlant.setOnMouseReleased(null);

            setupMouseEvents(roomPlant);
        }


    }

    /**
     * Removes widget glow from the specified potted plant.
     * This is only used when we remove widgets as it should update the corresponding plant in the room.
     * @param pottedPlant the potted plant
     */
    public void removeRoomPlantGlow(PottedPlant pottedPlant) {
        for (Group plant : roomPlants) {
            PlacementSlot slot = (PlacementSlot) plant.getProperties().get("Slot");
            PottedPlant pottedSlot = (PottedPlant) slot.getPlacedItem();
            if (pottedPlant == pottedSlot) {
                plant.setId("");
                plant.getStyleClass().remove("glow");
            }
        }
    }

    /**
     * Gets the potted plant associated with a given group.
     *
     * @param group the group
     * @return the potted plant
     */
    private PottedPlant getPottedPlantFromGroup(Group group) {
        Object pottedPlant = group.getProperties().get("PottedPlant");
        if (pottedPlant instanceof PottedPlant){
            return (PottedPlant) pottedPlant;
        }
        return null;
    }

    /**
     * Sets the selected potted plant and its corresponding button.
     *
     * @param container the group containing the plant
     */
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

    /**
     * Updates the plant image in the specified group.
     *
     * @param button the group containing the plant image
     * @param pottedPlant the potted plant
     */
    private void updatePlantImage(Group button, PottedPlant pottedPlant) {
        for (Node node : button.getChildren()) {
            if (node instanceof ImageView && "PlantImage".equals(node.getId())) {
                ImageView plantImageView = (ImageView) node;
                Image newImage = new Image(pottedPlant.getPlantTop().getImageFilePath());
                plantImageView.setImage(newImage);
                break;
            }
        }
    }

    /**
     * Gets the current water level of the specified potted plant.
     *
     * @param pottedPlant the potted plant
     * @return the water level
     */
    public String getCurrentWaterLevel(PottedPlant pottedPlant) {
        return String.valueOf(selectedPottedPlant.getPlantTop().getHealthStat().getWaterLevel());
    }

    public String getCurrentHealthLevel(){
        return String.valueOf(selectedPottedPlant.getPlantTop().getHealthStat().getOverallMood());
    }

    /**
     * Waters the selected potted plant and updates the image.
     *
     * @return the current water level after watering
     */
    public String waterPottedPlant() {
        if (isPlantDetailsFrameOpened && selectedPottedPlant != null) {
            selectedPottedPlant.getPlantTop().water();
            String currentWaterLevel = getCurrentWaterLevel(selectedPottedPlant);
            updatePlantImage(selectedPottedPlantButton, selectedPottedPlant);
            widgetHandler.updatePlantImage(selectedPottedPlant);
            return currentWaterLevel;
        }
        return null;
    }

    /**
     * Switches the locations of two plants.
     *
     * @param plant1 the first plant group
     * @param plant2 the second plant group
     */
    public void switchPlantLocations(Group plant1, Group plant2) {
        if (plant1 != null && plant2 != null) {
            PlacementSlot slot1 = (PlacementSlot) plant1.getProperties().get("Slot");
            PlacementSlot slot2 = (PlacementSlot) plant2.getProperties().get("Slot");

            int plant1SlotIndex = (int) plant1.getProperties().get("SlotIndex");
            int plant2SlotIndex = (int) plant2.getProperties().get("SlotIndex");

            double slot1X = slot1.getX();
            double slot1Y = slot1.getY();
            double slot2X = slot2.getX();
            double slot2Y = slot2.getY();

            roomGuiContent.updatePlantLocations(plant1, plant2, slot1X, slot1Y, slot2X, slot2Y);

            plant1.getProperties().put("Slot", slot2);
            plant2.getProperties().put("Slot", slot1);

            plant1.getProperties().put("SlotIndex", plant2SlotIndex);
            plant2.getProperties().put("SlotIndex", plant1SlotIndex);

            setupMouseEvents(plant1);
            setupMouseEvents(plant2);
            System.out.println("Switched plant locations.");
        }
    }

    /**
     * Updates the location of the specified plant to a new slot.
     *
     * @param plant the plant group
     * @param newLocationIndex the index of the new slot
     */
    public void updatePlantLocation(Group plant, int newLocationIndex) {
        if (roomPlants.contains(plant)) {

            List<PlacementSlot> slots = user.getRoom(0).getSlots();
            PlacementSlot newSlot = slots.get(newLocationIndex);
            PlacementSlot oldSlot = (PlacementSlot) plant.getProperties().get("Slot");

            for (Rectangle detectionPart : slotZones) {
                if (detectionPart.getProperties().get("Slot").equals(oldSlot)) {
                    detectionPart.getProperties().put("IsEmpty", true);
                }
                if (detectionPart.getProperties().get("Slot").equals(newSlot)) {
                    detectionPart.getProperties().put("IsEmpty", false);
                }
            }

            plant.setTranslateX(0);
            plant.setTranslateY(0);
            plant.setLayoutX(newSlot.getX());
            plant.setLayoutY(newSlot.getY());
            plant.getProperties().put("SlotIndex", newLocationIndex);
            plant.getProperties().put("Slot", newSlot);

        }
    }

    /**
     * Sets the specified potted plant to dead status and updates its image.
     *
     * @param selectedPottedPlant the potted plant to set as dead
     */
    public void setPottedPlantToDead(PottedPlant selectedPottedPlant) { // remove this later. Backend already handles
        // water level
        for (Group plant : roomPlants) {
            PottedPlant foundPottedPlant = (PottedPlant) plant.getProperties().get("PottedPlant");
            if (foundPottedPlant == selectedPottedPlant) {
                updatePlantImage(plant, selectedPottedPlant);
            }
        }
    }

    /**
     * Takes out the selected potted plant and updates its widget status.
     */
    public void takeOutPottedPlant() {
        if (isPlantDetailsFrameOpened && selectedPottedPlant != null) {
            if (selectedPottedPlantButton != null && !"IsWidget".equals(selectedPottedPlantButton.getId())) {
                selectedPottedPlantButton.setId("IsWidget");
                selectedPottedPlantButton.getStyleClass().add("glow");

                String stageId = (String) selectedPottedPlantButton.getProperties().get("StageId");
                widgetHandler.addWidget(selectedPottedPlant, stageId);
            }
        }
    }

    /**
     * Sets the menu opened status.
     *
     * @param isOpened the status of the menu
     */
    public void setMenuOpened(boolean isOpened) {
        isPlantDetailsFrameOpened = isOpened;
    }

    /**
     * Checks if dragging is in progress.
     *
     * @return the dragging status
     */
    public boolean isDragging() {
        return this.isDragging;
    }

    /**
     * Gets the plant group from the given rectangle detection.
     *
     * @param rectangle the rectangle
     * @return the plant group
     */
    public Group getPlantFromRectangleDetection(Rectangle rectangle) {
        PlacementSlot slot = (PlacementSlot) rectangle.getProperties().get("Slot");
        if (slot.getPlacedItem() != null) {
            PottedPlant pottedPlant = (PottedPlant) slot.getPlacedItem();
            for (Group roomPlant : roomPlants) {
                if (roomPlant.getProperties().get("PottedPlant").equals(pottedPlant)) {
                    return roomPlant;
                }
            }
        }
        return null;
    }

    /**
     * Gets the plant group associated with the given placement slot.
     *
     * @param slot the placement slot
     * @return the plant group
     */
    public Group getRoomPlantFromPlacementSlot(PlacementSlot slot) {
        if (slot.getPlacedItem() != null) {
            for (Group plantInRoom : roomPlants) {
                if (plantInRoom.getProperties().get("PottedPlant").equals(slot.getPlacedItem())) {
                    return plantInRoom;
                }
            }
        }
        return null;
    }
    public PottedPlant getSelectedPottedPlant() {
        return selectedPottedPlant;
    }
}
