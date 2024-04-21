package main.java.Application.Controllers;

import Builders.ItemBuilder;
import Builders.PlantTopBuilder;
import Controllers.WidgetHandler;
import entities.PlantTop;
import entities.Pot;
import entities.PottedPlant;
import entities.WidgetEntity;
import enums.PotType;
import enums.Rarity;
import enums.Species;
import enums.Stage;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class WidgetController {
    
    private WidgetHandler widgetHandler;
    private ArrayList<WidgetEntity> activeWidgets;

    private Group roomView;


    public void createTestWidget() {

        Pot pot = ItemBuilder.buildPot(PotType.POT_STRIPED_BLUE);
        PlantTop plantTop = PlantTopBuilder.buildPlantTop(Species.CACTUS);
        PottedPlant pottedPlant = new PottedPlant(pot, plantTop);

        WidgetEntity widget = new WidgetEntity(pottedPlant, 87, 258);
        activeWidgets.add(widget);

        displayWidgets();
    }

    public void displayWidgets() {
        for (int i = 0; i < activeWidgets.size(); i++) {

            int posX = activeWidgets.get(i).getX();
            int posY = activeWidgets.get(i).getY();
            String plantFilePath = activeWidgets.get(i).getPlantFilePath();
            String potFilePath = activeWidgets.get(i).getPotFilePath();


            Group plantContainer = new Group();
            plantContainer.setLayoutX(87);
            plantContainer.setLayoutY(258);

            String imagePath = String.valueOf(getClass().getClassLoader().getResource(plantFilePath));
            Image plantImage = new Image(imagePath);
            ImageView imageView = new ImageView(plantImage);
            imageView.setId("Plant");

            /*
            String imagePathCircle = String.valueOf(getClass().getClassLoader().getResource("plants/plantCircle.png"));
            Image circleImage = new Image(imagePathCircle);
            ImageView circleImageView = new ImageView(circleImage);
            circleImageView.setId("Circle");
            circleImageView.setVisible(false); // Initially hidden
             */

            imageView.setFitHeight(95);
            imageView.setFitWidth(77);

            /*
            circleImageView.setFitWidth(141);
            circleImageView.setFitHeight(138);
            circleImageView.setLayoutX(-35);
            circleImageView.setLayoutY(-21);

            ImageView infoIcon = new ImageView(new Image(String.valueOf(getClass().getClassLoader().getResource("infoIcon.png"))));
            infoIcon.setFitHeight(25);
            infoIcon.setFitWidth(25);
            infoIcon.setLayoutX(-35);
            infoIcon.setLayoutY(35);
            infoIcon.setVisible(false);


            ImageView canIcon = new ImageView(new Image(String.valueOf(getClass().getClassLoader().getResource("canIcon.png"))));
            canIcon.setFitHeight(35);
            canIcon.setFitWidth(30);
            canIcon.setLayoutX(77);
            canIcon.setLayoutY(33);
            canIcon.setVisible(false);
             */


            plantContainer.getChildren().addAll(imageView);
            plantContainer.setLayoutX(posX);
            plantContainer.setLayoutY(posY);

            roomView.getChildren().add(plantContainer);

            //addHoverEffect(plantContainer, canIcon, infoIcon, circleImageView); // Add hover effect logic
        }
    }
    
    
    public WidgetController(Group roomView) {
        this.widgetHandler = new WidgetHandler(this);
        this.roomView = roomView;
        this.activeWidgets = new ArrayList<WidgetEntity>();
        createTestWidget();
    }
    
}