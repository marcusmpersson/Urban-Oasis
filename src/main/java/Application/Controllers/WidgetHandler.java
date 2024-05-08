package main.java.Application.Controllers;

import Controllers.LocalFileHandler;
import entities.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.java.Application.View.WidgetView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WidgetHandler {
    
    private ArrayList<WidgetEntity> widgets;
    private LocalFileHandler localFileHandler;
    private Map<String, StageData> stages;
    private RoomController roomController;
    private ArrayList<Stage> uiWidgets;

    public WidgetHandler (RoomController roomController) {
        this.widgets = new ArrayList<WidgetEntity>();
        this.localFileHandler = new LocalFileHandler();
        this.stages = new HashMap<>();
        this.uiWidgets = new ArrayList<>();
        this.roomController = roomController;
    }

    public void setWidget(PottedPlant pottedPlant, String stageId) {

        String plantImage = pottedPlant.getPlantTop().getImageFilePath();
        String potImage = pottedPlant.getPot().getImageFilePath();

        WidgetView widgetView = new WidgetView(this);
        StackPane root = widgetView.setWidget(new Image(plantImage),new Image(potImage),stageId);
        Stage stage = widgetView.getStage();

        stage.getProperties().put("PottedPlant", pottedPlant);
        widgetView.mouseEnteredAndExitedEvents();

        WidgetHandler.StageData data = new WidgetHandler.StageData(stage);
        stages.put(stageId, data);

        root.setOnMousePressed(event -> {
            data.xOffset = stage.getX() - event.getScreenX();
            data.yOffset = stage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + data.xOffset);
            stage.setY(event.getScreenY() + data.yOffset);
        });

        uiWidgets.add(stage);
    }

    public void updatePlantImage(PottedPlant pottedPlant) {
        for (int i = 0; i < uiWidgets.size(); i++) {
            Stage stage = uiWidgets.get(i);
            if (stage != null) {
                PottedPlant foundPottedPlant = (PottedPlant) stage.getProperties().get("PottedPlant");

                if (foundPottedPlant != null) {
                    if (foundPottedPlant == pottedPlant) {


                        Image newImage = new Image(pottedPlant.getPlantTop().getImageFilePath());
                        ImageView plantImageView = (ImageView) stage.getProperties().get("PlantImageView");
                        plantImageView.setImage(newImage);
                        Text waterText = (Text) stage.getProperties().get("WaterText");
                        waterText.setText("Water Level: " + String.valueOf(pottedPlant.getPlantTop().getHealthStat().getWaterLevel()) + "/200");
                    }
                }
            }
        }
    }

    public void removeWidget(Stage stage) {
        stage.close();
        PottedPlant pottedPlant = (PottedPlant) stage.getProperties().get("PottedPlant");
        roomController.removeWidget(pottedPlant);
    }

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

    public void removeWidget(String stageId) {
        for (Map.Entry<String, StageData> entry : stages.entrySet()) {
            if (entry.getKey().equals(stageId)) {
                StageData data = entry.getValue();
                Stage stage = data.stage;
                stage.close();
            }
        }
    }



    public void loadWidgets (String username) {
        this.widgets = localFileHandler.readLocalFile(username);
        if (widgets != null) {
            //TODO: load widgets on desktop via GUI controller
        }
    }

    public void updateLocalFile(String username) {
        localFileHandler.updateLocalFile(widgets, username);
    }

    public void addWidget(Placeable item, String stageId) {
        WidgetEntity widget = new WidgetEntity((PottedPlant) item, 200, 200);
        widgets.add(widget);
        setWidget((PottedPlant) item, stageId);
        //TODO: load widget via GUI controller
    }

    public WidgetEntity getWidgetAt(int index){
        return widgets.get(index);
    }

    static class StageData {
        Stage stage;
        double xOffset = 0;
        double yOffset = 0;

        public StageData(Stage stage) {
            this.stage = stage;
        }
    }
    
}