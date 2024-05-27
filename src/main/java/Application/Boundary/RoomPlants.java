package main.java.Application.Boundary;

import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * RoomPlants handles the graphical representation and manipulation of plants within a room.
 *
 * Author: Mouhammed Fakhro
 */
public class RoomPlants {

    private Group roomView;

    /**
     * Constructor for RoomPlants.
     *
     * @param roomView the view for the room
     */
    public RoomPlants(Group roomView) {
        this.roomView = roomView;
    }

    /**
     * Generates a graphical representation of a potted plant in the room.
     *
     * @param plantFilePath the file path for the plant image
     * @param potFilePath the file path for the pot image
     * @param posX the x-coordinate of the plant's position
     * @param posY the y-coordinate of the plant's position
     * @return the group representing the plant container
     */
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

        plantContainer.getChildren().addAll(potImageView, plantImageView);

        plantImageView.toBack();
        potImageView.toFront();

        plantImageView.setLayoutY(potImageView.getLayoutY() - plantImageView.getFitHeight());

        plantContainer.setLayoutX(posX);
        plantContainer.setLayoutY(posY);

        roomView.getChildren().add(plantContainer);

        return plantContainer;
    }

    public void updatePlantandPotImages(Group plantButton, String plantFilePath, String potFilePath) {

        ImageView plantImageView = (ImageView) plantButton.lookup("#PlantImage");
        ImageView potImageView = (ImageView) plantButton.lookup("#PotImage");

        if (plantImageView != null) {
            Image newPlantImage = new Image(getClass().getClassLoader().getResource(plantFilePath).toString());
            plantImageView.setImage(newPlantImage);
        }

        if (potImageView != null) {
            Image newPotImage = new Image(getClass().getClassLoader().getResource(potFilePath).toString());
            potImageView.setImage(newPotImage);
        }
    }

    /**
     * Adds location arrows to the room at specified positions.
     *
     * @param posX the x-coordinate of the arrow's position
     * @param posY the y-coordinate of the arrow's position
     * @param slotIndex the index of the slot
     * @return the image view of the arrow
     */
    public ImageView addLocationArrowsToRoom(double posX, double posY, int slotIndex) {
        Image arrowImage = new Image(getClass().getClassLoader().getResource("gifs/arrow_down.gif").toString());
        ImageView arrowImageView = new ImageView(arrowImage);
        arrowImageView.setFitHeight(50);
        arrowImageView.setFitWidth(50);
        arrowImageView.setPreserveRatio(true);
        arrowImageView.setLayoutX(posX + 30);
        arrowImageView.setLayoutY(posY + 20);
        arrowImageView.setVisible(false);
        arrowImageView.getProperties().put("SlotIndex", slotIndex);

        return arrowImageView;
    }

    /**
     * Adds slot zones to the room at specified positions.
     *
     * @param posX the x-coordinate of the slot zone's position
     * @param posY the y-coordinate of the slot zone's position
     * @return the rectangle representing the slot zone
     */
    public Rectangle addSlotZonesToRoom(double posX, double posY) {
        Rectangle rectangle = new Rectangle(posX + 10, posY - 50, 80, 100);
        rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }

    /**
     * Removes the glow effect from all plants except the specified exception plant.
     *
     * @param roomPlants the list of room plants
     * @param exceptionPlant the plant to be excluded from removing the glow effect
     */
    public void removeAllPlantGlows(ArrayList<Group> roomPlants, Group exceptionPlant) {
        for (Group plant : roomPlants) {
            if (!plant.equals(exceptionPlant)) {
                plant.getStyleClass().remove("glow");
            }
        }
    }

    /**
     * Updates the locations of two plants.
     *
     * @param plant1 the first plant group
     * @param plant2 the second plant group
     * @param slot1X the x-coordinate of the first slot
     * @param slot1Y the y-coordinate of the first slot
     * @param slot2X the x-coordinate of the second slot
     * @param slot2Y the y-coordinate of the second slot
     */
    public void updatePlantLocations(Group plant1, Group plant2, double slot1X, double slot1Y, double slot2X, double slot2Y) {
        plant1.setTranslateX(0);
        plant1.setTranslateY(0);

        plant1.setLayoutX(slot2X);
        plant1.setLayoutY(slot2Y);

        plant2.setTranslateX(0);
        plant2.setTranslateY(0);

        plant2.setLayoutX(slot1X);
        plant2.setLayoutY(slot1Y);
    }

    /**
     * Adds a glow effect to the specified plant.
     *
     * @param plant the plant group to add the glow effect to
     */
    public void addGlowToPlant(Group plant) {
        if (!plant.getStyleClass().contains("glow")) {
            plant.getStyleClass().add("glow");
        }
    }

    /**
     * Adds a color effect to the specified arrow.
     *
     * @param arrow the arrow image view to add the color effect to
     */
    public void addColorEffectToRoomArrow(ImageView arrow) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(0.5);
        colorAdjust.setSaturation(0.8);
        colorAdjust.setBrightness(0.2);
        arrow.setEffect(colorAdjust);
    }

    /**
     * Removes the color effect from the specified arrow.
     *
     * @param arrow the arrow image view to remove the color effect from
     */
    public void removeColorEffectFromArrow(ImageView arrow) {
        arrow.setEffect(null);
    }
}
