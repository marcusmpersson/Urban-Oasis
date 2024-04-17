package main.java.Application;

import entities.Plant;
import entities.PlantTop;
import entities.Pot;
import entities.PottedPlant;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    Transitions transitions = new Transitions();

    @FXML
    private Group mainLoginFrame;

    @FXML
    private Group registerFrame;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @FXML
    private ImageView wrongLogin;

    @FXML
    private ImageView successfulLogin;

    @FXML
    private Group storeView;

    @FXML
    private Group roomView;

    @FXML
    private ImageView roomBackground;

    private Image currentBackground;

    @FXML
    private TilePane shopPane;

    private ArrayList<Group> plantGroups = new ArrayList<>();

    String[][] slots = {
            {"1", "87", "258", "Shade"},
            {"2", "87", "429", "Shade"},
            {"3", "88", "656", "Half Shade"},
            {"4", "264", "220", "Shade"},
            {"5", "387", "220", "Shade"},
            {"6", "265", "568", "Half Shade"},
            {"7", "386", "568", "Half Shade"},
            {"8", "265", "744", "Half Shade"},
            {"9", "386", "744", "Half Shade"},
            {"10", "265", "923", "Half Shade"},
            {"11", "570", "246", "Half Shade"},
            {"12", "705", "246", "Half Shade"},
            {"13", "570", "439", "Sunny"},
            {"14", "705", "439", "Sunny"},
            {"15", "906", "229", "Sunny"},
            {"16", "1038", "229", "Sunny"},
            {"17", "1169", "229", "Sunny"},
            {"18", "906", "435", "Sunny"},
            {"19", "1038", "435", "Sunny"},
            {"20", "1169", "435", "Sunny"},
            {"21", "813", "656", "Sunny"},
            {"22", "802", "931", "Shade"},
            {"23", "1020", "756", "Sunny"},
            {"24", "1200", "660", "Shade"},
            {"25", "1200", "879", "Shade"}
    };

    /**
     * private PlantTop plantTop;
     *     private Pot pot;
     *
     *     double xOffset = 0;
     *     double yOffset = 0;
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchToRoomView(null);
        startBackgroundTimeChecker();
        /**
         *   ArrayList<String> imageArraysPlants = new ArrayList<>();
         *         imageArraysPlants.add(String.valueOf(getClass().getClassLoader().getResource("plants/plantTop2.png")));
         *         plantTop = new PlantTop(imageArraysPlants, null, null, 0, null);
         *
         *         pot = new Pot("Pot", String.valueOf(getClass().getClassLoader().getResource("pots/pot1.png")), 0);
         *
         *         PottedPlant pottedPlant = new PottedPlant(pot, plantTop);
         *
         *
         *         Stage widgetStage = new Stage();
         *         widgetStage.initStyle(StageStyle.TRANSPARENT);
         *
         *         Circle circle = new Circle(50, Color.BLUE);
         *         Pane root = new Pane(circle);
         *
         *
         *         root.setOnMousePressed(event -> {
         *             xOffset = widgetStage.getX() - event.getScreenX();
         *             yOffset = widgetStage.getY() - event.getScreenY();
         *         });
         *
         *         root.setOnMouseDragged(event -> {
         *             widgetStage.setX(event.getScreenX() + xOffset);
         *             widgetStage.setY(event.getScreenY() + yOffset);
         *         });
         *
         *         Scene scene = new Scene(root, 100, 100);
         *         scene.setFill(Color.TRANSPARENT);
         *
         *         widgetStage.setScene(scene);
         *         widgetStage.show();
         */


    }

    private void createPlantGroups() {
        for (int i = 0; i < slots.length; i++) {
            Group plantContainer = new Group();
            plantContainer.setLayoutX(87);
            plantContainer.setLayoutY(258);

            String imagePath = String.valueOf(getClass().getClassLoader().getResource("plants/plantTop2.png"));
            Image plantImage = new Image(imagePath);
            ImageView imageView = new ImageView(plantImage);
            imageView.setId("Plant");

            String imagePathCircle = String.valueOf(getClass().getClassLoader().getResource("plants/plantCircle.png"));
            Image circleImage = new Image(imagePathCircle);
            ImageView circleImageView = new ImageView(circleImage);
            circleImageView.setId("Circle");
            circleImageView.setVisible(false); // Initially hidden

            imageView.setFitHeight(95);
            imageView.setFitWidth(77);

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


            plantContainer.getChildren().addAll(circleImageView, imageView, canIcon, infoIcon);
            plantContainer.setLayoutX(Double.parseDouble(slots[i][1]));
            plantContainer.setLayoutY(Double.parseDouble(slots[i][2]));

            roomView.getChildren().add(plantContainer);
            plantGroups.add(plantContainer);

            addHoverEffect(plantContainer, canIcon, infoIcon, circleImageView); // Add hover effect logic
        }
    }

    public ScaleTransition sizeUpOrDownAnimation(boolean up, Object image) {
        if (up) {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), (Node) image);
            scaleUp.setToX(1.2);
            scaleUp.setToY(1.2);
            return scaleUp;
        } else {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), (Node) image);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            return scaleDown;
        }
    }

    private void addHoverEffect(Group plantContainer, ImageView canIcon, ImageView infoIcon, ImageView circleImage) {
        plantContainer.setOnMouseEntered(event -> {
            System.out.println("e");
            sizeUpOrDownAnimation(true, circleImage).play();
            sizeUpOrDownAnimation(true, canIcon).play();
            sizeUpOrDownAnimation(true, infoIcon).play();
            circleImage.setVisible(true);
            canIcon.setVisible(true);
            infoIcon.setVisible(true);
        });

        plantContainer.setOnMouseExited(event -> {
            System.out.println("e");
            sizeUpOrDownAnimation(false, circleImage).play();
            sizeUpOrDownAnimation(false, canIcon).play();
            sizeUpOrDownAnimation(false, infoIcon).play();
            circleImage.setVisible(false);
            canIcon.setVisible(false);
            infoIcon.setVisible(false);
        });
    }

    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




    public void switchToRoomView(MouseEvent mouseEvent) {
        storeView.setOpacity(0);
        roomView.setOpacity(1);
    }

    public void switchToStoreView(MouseEvent mouseEvent) {
        roomView.setOpacity(0);
        storeView.setOpacity(1);
    }

    public void handleMouseEntered(MouseEvent mouseEvent) {
        transitions.handleMouseEntered(mouseEvent);
    }

    public void handleMouseExited(MouseEvent mouseEvent) {
        transitions.handleMouseExited(mouseEvent);
    }

    public void updateRoomBackground(String timeOfDay) {
        System.out.println(timeOfDay);
        String imagePath;

        try {
            if (timeOfDay.equals("Daytime")) {
                imagePath = String.valueOf(getClass().getClassLoader().getResource("rooms/roomDaytime.jpg"));
            } else {
                imagePath = String.valueOf(getClass().getClassLoader().getResource("rooms/roomNighttime.jpg"));
            }
            currentBackground = new Image(imagePath);
            //roomBackground.setImage(currentBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startBackgroundTimeChecker() {
        Thread timeCheckerThread = new Thread(() -> {
            while (true) {
                LocalTime currentTime = LocalTime.now();

                if (isNightTime(currentTime)) {
                   // Platform.runLater(() -> updateRoomBackground("Nighttime"));
                } else {
                    Platform.runLater(() -> updateRoomBackground("Daytime"));
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeCheckerThread.setDaemon(true);
        timeCheckerThread.start();
    }

    public boolean isNightTime(LocalTime time) {
        return (time.isAfter(LocalTime.of(15, 50)) && time.isBefore(LocalTime.of(16,07)));
    }

}
