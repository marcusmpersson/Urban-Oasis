package main.java.Application.Controllers;

import Controllers.LocalFileHandler;
import entities.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WidgetHandler {
    
    private ArrayList<WidgetEntity> widgets;
    private LocalFileHandler localFileHandler;

    private Map<String, StageData> stages;

    public WidgetHandler () {
        this.widgets = new ArrayList<WidgetEntity>();
        this.localFileHandler = new LocalFileHandler();
        this.stages = new HashMap<>();

    }

    public void setWidget(Image plantImage, Image potImage, String stageId) {
        Stage stage = new Stage(StageStyle.TRANSPARENT);

        ImageView plantImageView = new ImageView(plantImage);
        ImageView potImageView = new ImageView(potImage);

        plantImageView.setFitHeight(100);
        plantImageView.setPreserveRatio(true);
        potImageView.setFitHeight(60);
        potImageView.setPreserveRatio(true);

        VBox layout = new VBox(5);
        layout.getChildren().addAll(plantImageView, potImageView);
        layout.setAlignment(Pos.CENTER);

        HBox menuBar = new HBox(10);
        Button btnWater = new Button("Water");
        menuBar.getChildren().addAll(btnWater);
        menuBar.setAlignment(Pos.CENTER);

        layout.getChildren().add(menuBar); // add the menu bar to the VBox

        Rectangle roundedRectangle = new Rectangle();
        roundedRectangle.setFill(Color.BLACK);
        roundedRectangle.setStroke(Color.BLACK);
        roundedRectangle.setStrokeWidth(2);
        roundedRectangle.setArcWidth(20);
        roundedRectangle.setArcHeight(20);
        roundedRectangle.widthProperty().bind(layout.widthProperty().add(20));
        roundedRectangle.heightProperty().bind(layout.heightProperty().add(20));
        roundedRectangle.setVisible(false);
        menuBar.setVisible(false);

        StackPane root = new StackPane(roundedRectangle, layout);
        root.setPadding(new Insets(10)); // padding to ensure the rectangle does not clip content
        root.setStyle("-fx-background-color: transparent;"); // make root transparent

        root.setOnMouseEntered(event -> {
            roundedRectangle.setVisible(true);
            menuBar.setVisible(true);
        });
        root.setOnMouseExited(event -> {
            roundedRectangle.setVisible(false);
            menuBar.setVisible(false);
        });

        StageData data = new StageData(stage);
        stages.put(stageId, data);

        root.setOnMousePressed(event -> {
            data.xOffset = stage.getX() - event.getScreenX();
            data.yOffset = stage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + data.xOffset);
            stage.setY(event.getScreenY() + data.yOffset);
        });

        Scene scene = new Scene(root, 220, 250); // Adjusted size to fit the rounded rectangle
        scene.setFill(null); // Make scene transparent

        stage.setScene(scene);
        stage.show();
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

    public void addWidget(Placeable item) {
        WidgetEntity widget = new WidgetEntity((PottedPlant) item, 200, 200);
        widgets.add(widget);

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