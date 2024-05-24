package Application.Boundary;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Application.Controllers.WidgetHandler;

/**
 * WidgetView handles the graphical representation and interactions for the plant widget.
 *
 * Author: Mouhammed Fakhro
 */
public class WidgetView {

    private Rectangle roundedRectangle;
    private HBox menuBar;
    private StackPane root;
    private Stage stage;
    private WidgetHandler widgetHandler;
    private Text waterLevelText;
    private ImageView plantImageView;

    /**
     * Constructor for WidgetView.
     *
     * @param widgetHandler the widget handler
     */
    public WidgetView(WidgetHandler widgetHandler) {
        this.widgetHandler = widgetHandler;
    }

    /**
     * Sets up the widget view for a potted plant.
     *
     * @param plantImage the image of the plant
     * @param potImage the image of the pot
     * @param currentWaterLevel the current water level of the plant
     * @return the stack pane representing the widget
     */
    public StackPane setWidget(Image plantImage, Image potImage, String currentWaterLevel) {
        stage = new Stage(StageStyle.TRANSPARENT);

        waterLevelText = new Text("Water Level: " + currentWaterLevel + "/100");
        waterLevelText.setFill(Color.WHITE);
        waterLevelText.setFont(Font.font("Pixeloid Sans", 16));

        plantImageView = new ImageView(plantImage);
        plantImageView.setFitHeight(100);
        plantImageView.setPreserveRatio(true);

        ImageView potImageView = new ImageView(potImage);
        potImageView.setFitHeight(60);
        potImageView.setPreserveRatio(true);

        VBox layout = new VBox(0);
        layout.getChildren().addAll(waterLevelText, plantImageView, potImageView);
        layout.setAlignment(Pos.CENTER);

        menuBar = new HBox(10);
        menuBar.setAlignment(Pos.CENTER);

        // Style for the buttons
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;";

        // Water button
        Button btnWater = new Button("Water");
        btnWater.setStyle(buttonStyle);
        btnWater.setOnMouseClicked(event -> {
            String waterLevel = widgetHandler.water(stage, plantImageView);
            waterLevelText.setText("Water Level: " + waterLevel + "/100");
        });

        // Remove widget button
        Button btnRemove = new Button("Remove Widget");
        btnRemove.setStyle(buttonStyle);
        btnRemove.setOnMouseClicked(event -> {
            widgetHandler.removeWidget(stage);
        });

        stage.getProperties().put("PlantImageView", plantImageView);
        stage.getProperties().put("WaterText", waterLevelText);

        // Adding buttons to the menu bar
        menuBar.getChildren().addAll(btnWater, btnRemove);

        layout.getChildren().add(menuBar); // Add the menu bar to the VBox

        roundedRectangle = new Rectangle();
        roundedRectangle.setFill(Color.BLACK);
        roundedRectangle.setStroke(Color.BLACK);
        roundedRectangle.setStrokeWidth(2);
        roundedRectangle.setArcWidth(20);
        roundedRectangle.setArcHeight(20);
        roundedRectangle.widthProperty().bind(layout.widthProperty().add(20));
        roundedRectangle.heightProperty().bind(layout.heightProperty().add(20));

        root = new StackPane(roundedRectangle, layout);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root, 220, 250); // Adjusted size to fit the rounded rectangle
        scene.setFill(null); // Make scene transparent

        stage.setScene(scene);
        stage.show();

        return root;
    }

    /**
     * Sets up mouse entered and exited events for the root pane.
     */
    public void mouseEnteredAndExitedEvents() {
        root.setOnMouseEntered(event -> {
            roundedRectangle.setVisible(true);
            menuBar.setVisible(true);
            waterLevelText.setVisible(true);
        });
        root.setOnMouseExited(event -> {
            roundedRectangle.setVisible(false);
            menuBar.setVisible(false);
            waterLevelText.setVisible(false);
        });
    }

    /**
     * Gets the stage associated with the widget view.
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }
}
