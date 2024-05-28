package main.java.Application.Controllers;

import controller.LocalFileHandler;
import entities.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Application.Boundary.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * WidgetHandler manages the widgets representing potted plants in the application.
 *
 * Author: Mouhammed Fakhro
 * Author: Rana Noorzadeh
 */
public class WidgetHandler {

    private ArrayList<WidgetEntity> widgets = new ArrayList<>();
    private LocalFileHandler localFileHandler = new LocalFileHandler();
    private Map<String, StageData> stageDataMap = new HashMap<>();
    private ArrayList<Stage> uiStages = new ArrayList<>();
    private RoomController roomController;

    /**
     * Constructor for WidgetHandler.
     *
     * @param roomController the room controller
     */
    public WidgetHandler(RoomController roomController) {
        this.roomController = roomController;
    }

    /**
     * Sets up a widget for a given potted plant.
     *
     * @param pottedPlant the potted plant
     * @param stageId the stage ID
     */
    public void setWidget(PottedPlant pottedPlant, String stageId) {
        String plantImageFilePath = pottedPlant.getPlantTop().getImageFilePath();
        String potImageFilePath = pottedPlant.getPot().getImageFilePath();
        String currentWaterLevel = roomController.getCurrentWaterLevel(pottedPlant);

        Widget widgetView = new Widget(this);
        StackPane root = widgetView.setWidget(new Image(plantImageFilePath), new Image(potImageFilePath), currentWaterLevel);
        Stage stage = widgetView.getStage();

        stage.getProperties().put("PottedPlant", pottedPlant);
        widgetView.mouseEnteredAndExitedEvents();

        StageData stageData = new StageData(stage);
        stageDataMap.put(stageId, stageData);

        root.setOnMousePressed(event -> {
            stageData.xOffset = stage.getX() - event.getScreenX();
            stageData.yOffset = stage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + stageData.xOffset);
            stage.setY(event.getScreenY() + stageData.yOffset);
        });

        uiStages.add(stage);
    }

    /**
     * Updates the plant image for the specified potted plant.
     *
     * @param pottedPlant the potted plant
     */
    public void updatePlantImage(PottedPlant pottedPlant) {
        for (Stage stage : uiStages) {
            if (stage != null) {
                PottedPlant foundPottedPlant = (PottedPlant) stage.getProperties().get("PottedPlant");

                if (foundPottedPlant != null && foundPottedPlant.equals(pottedPlant)) {
                    Image newImage = new Image(pottedPlant.getPlantTop().getImageFilePath());
                    ImageView plantImageView = (ImageView) stage.getProperties().get("PlantImageView");
                    plantImageView.setImage(newImage);
                    Text waterText = (Text) stage.getProperties().get("WaterText");
                    waterText.setText("Water Level: " + pottedPlant.getPlantTop().getHealthStat().getWaterLevel() + "/200");
                }
            }
        }
    }

    /**
     * Removes the widget for the specified stage.
     *
     * @param stage the stage
     */
    public void removeWidget(Stage stage) {
        stage.close();
        PottedPlant pottedPlant = (PottedPlant) stage.getProperties().get("PottedPlant");
        roomController.removeRoomPlantGlow(pottedPlant);
    }

    /**
     * Waters the potted plant associated with the specified stage and updates the image view.
     *
     * @param stage the stage
     * @param imageView the image view to update
     * @return the current water level
     */
    public String water(Stage stage, ImageView imageView) {
        PottedPlant selectedPottedPlant = (PottedPlant) stage.getProperties().get("PottedPlant");

        selectedPottedPlant.getPlantTop().water();
        int waterLevel = selectedPottedPlant.getPlantTop().getHealthStat().getWaterLevel();
        String currentWaterLevel = String.valueOf(waterLevel);

        if (imageView != null) {
            Image newImage = new Image(selectedPottedPlant.getPlantTop().getImageFilePath());
            imageView.setImage(newImage);
        }
        if (waterLevel >= 200) {
            roomController.setPottedPlantToDead(selectedPottedPlant);
        }
        return currentWaterLevel;
    }

    /**
     * Loads the widgets from the local file associated with the given username.
     *
     * @param username the username
     */
    public void loadWidgets(String username) {
        this.widgets = localFileHandler.readLocalFile(username);
        if (widgets != null) {
            // TODO: load widgets on desktop via GUI controller
        }
    }

    /**
     * Updates the local file with the current widgets.
     *
     * @param username the username
     */
    public void updateLocalFile(String username) {
        localFileHandler.updateLocalFile(widgets, username);
    }

    /**
     * Adds a widget for the given placeable item.
     *
     * @param item the placeable item
     * @param stageId the stage ID
     */
    public void addWidget(Placeable item, String stageId) {
        WidgetEntity widget = new WidgetEntity((PottedPlant) item, 200, 200);
        widgets.add(widget);
        setWidget((PottedPlant) item, stageId);
        // TODO: load widget via GUI controller
    }

    /**
     * Gets the widget entity at the specified index.
     *
     * @param index the index
     * @return the widget entity
     */
    public WidgetEntity getWidgetAt(int index) {
        return widgets.get(index);
    }

    /**
     * Inner static class to hold stage data.
     */
    static class StageData {
        Stage stage;
        double xOffset = 0;
        double yOffset = 0;

        /**
         * Constructor for StageData.
         *
         * @param stage the stage
         */
        public StageData(Stage stage) {
            this.stage = stage;
        }
    }
}
