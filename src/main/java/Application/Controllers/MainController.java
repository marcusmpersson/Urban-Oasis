package main.java.Application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import main.java.Application.Animations.Transitions;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;

    private boolean isLoggedIn;
    private Transitions transitions = new Transitions();
    private WidgetController widgetHandler;
    @FXML
    private Group storeView;

    @FXML
    private Group roomView;

    @FXML
    private ImageView roomBackground;

    private Image currentBackground;

    @FXML
    private TilePane shopPane;

    @FXML
    public ImageView returnButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isLoggedIn = true;
        switchToRoomView(null);
        startBackgroundTimeChecker();
        new RoomController(roomView);
      //  new WidgetController(roomView);



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

  /*
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
   */

    public void cleanup() {
        isLoggedIn = false;
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
        System.out.println("hello");
        transitions.handleMouseEnteredButtonEffect(mouseEvent);
    }

    public void handleMouseExited(MouseEvent mouseEvent) {
        transitions.handleMouseExitedButtonEffect(mouseEvent);
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
            while (isLoggedIn) {
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

    public void switchToLoginScene (MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        cleanup();
    }
}
