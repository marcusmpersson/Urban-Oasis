package main.java.Application.Boundary;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import main.java.Application.Controllers.RoomController;

/**
 * PlantInformationPopup handles the graphical representation and interactions
 * for the plant information popup in the room.
 *
 * Author: Mouhammed Fakhro
 */
public class PlantInformationPopup {

    private static final double RECTANGLE_WIDTH = 300;
    private static final double RECTANGLE_HEIGHT = 300;
    private static final double IMAGE_BUTTON_SIZE = 20;
    private Button waterLevelButton;
    private Button healthStatButton;
    private RoomController roomController;
    private Group informationRectangle;

    /**
     * Constructor for PlantInformationPopup.
     *
     * @param roomController the room controller
     */
    public PlantInformationPopup(RoomController roomController) {
        this.roomController = roomController;
        this.informationRectangle = generateInformationRectangle();
        closeInfoPopupFrame();
    }

    /**
     * Gets the information rectangle group.
     *
     * @return the information rectangle group
     */
    public Group getInformationRectangle() {
        return informationRectangle;
    }

    /**
     * Generates the information rectangle group with all UI elements.
     *
     * @return the group representing the information rectangle
     */
    public Group generateInformationRectangle() {
        Group informationRectangle = new Group();

        // Existing gradient and rectangle setup
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#41295a")), new Stop(1, Color.web("#2f0743")) };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        rectangle.setFill(gradient);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        StackPane textPane = new StackPane();
        textPane.setPrefSize(RECTANGLE_WIDTH, RECTANGLE_HEIGHT * 0.2);

        // Buttons
        Button waterButton = createButton("Water", 0.1, true);
        Button takeOutButton = createButton("Place Desktop Widget", 0.25, true);
        Button removeFromRoomButton = createButton("Remove from Room", 0.4, true);
        waterLevelButton = createButton("Water Level: 0/100", 0.6, false);
        healthStatButton = createButton("Health: 0/100" , 0.8, false);

        waterButton.setOnMouseClicked(event -> {
            String level = roomController.waterPottedPlant();
            String health = roomController.getCurrentHealthLevel();
            updateWaterLevelText(level);
            updateHealthLevelText(health);
        });

        takeOutButton.setOnMouseClicked(event -> {
            roomController.takeOutPottedPlant();
        });

        removeFromRoomButton.setOnMouseClicked(event -> {
            roomController.removeItemFromSlot();
            closeInfoPopupFrame();
            roomController.setMenuOpened(false);
        });

        // Close button setup with larger clickable area
        Image closeButtonImage = new Image(PlantInformationPopup.class.getClassLoader().getResource("icons/x.png").toString());
        ImageView closeButton = new ImageView(closeButtonImage);
        closeButton.setFitWidth(IMAGE_BUTTON_SIZE);
        closeButton.setFitHeight(IMAGE_BUTTON_SIZE);

        // Create a larger transparent rectangle for better click detection
        Rectangle clickableArea = new Rectangle(IMAGE_BUTTON_SIZE + 10, IMAGE_BUTTON_SIZE + 10);
        clickableArea.setFill(Color.TRANSPARENT); // Make sure the rectangle is invisible

        // Positioning the clickable area and close button
        Group closeButtonGroup = new Group(clickableArea, closeButton);
        closeButtonGroup.setLayoutX(RECTANGLE_WIDTH - IMAGE_BUTTON_SIZE - 10);
        closeButtonGroup.setLayoutY(0);

        informationRectangle.getChildren().addAll(rectangle, textPane, waterButton, takeOutButton, removeFromRoomButton, waterLevelButton,healthStatButton,
                closeButtonGroup);

        informationRectangle.setTranslateX(1000);
        informationRectangle.setTranslateY(458);

        // Set click handler on the group, not just the image
        closeButtonGroup.setOnMouseClicked(event -> {
            if (!roomController.isDragging()) {
                closeInfoPopupFrame();
                roomController.setMenuOpened(false);
            }
        });

        return informationRectangle;
    }

    /**
     * Updates the water level text on the button.
     *
     * @param level the current water level
     */
    public void updateWaterLevelText(String level) {
        waterLevelButton.setText("Water Level: " + level + "/100");
    }

    public void updateHealthLevelText(String level){
        healthStatButton.setText("Health: " + level + "/100");
    }

    /**
     * Creates a button with specified text and layout position.
     *
     * @param text the text for the button
     * @param layoutYPercent the vertical position as a percentage of the rectangle height
     * @return the created button
     */
    private Button createButton(String text, double layoutYPercent, boolean background) {
        Button button = new Button(text);
        if (background) {
            button.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white;");
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-background-insets: 0; -fx-background-radius: 0;");
        }
        button.setPrefWidth(RECTANGLE_WIDTH * 0.7);
        button.setLayoutX((RECTANGLE_WIDTH - button.getPrefWidth()) / 2); // Center horizontally
        button.setLayoutY(RECTANGLE_HEIGHT * layoutYPercent); // Set layout Y according to percent
        return button;
    }


    public void openInfoPopupFrame() {
        double x = 668;
        double y = 458;
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), informationRectangle);
        transition.setToX(x);
        transition.setToY(y);
        transition.play();
    }

    public void closeInfoPopupFrame() {
        double x = 1200;
        double y = informationRectangle.getTranslateY();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), informationRectangle);
        transition.setToX(x);
        transition.setToY(y);
        transition.play();
    }

}
