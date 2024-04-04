package Application;

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
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startBackgroundTimeChecker();
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
        String imagePath;

        try {
            if (timeOfDay.equals("Daytime")) {
                imagePath = String.valueOf(getClass().getClassLoader().getResource("rooms/roomDaytime.jpg"));
            } else {
                imagePath = String.valueOf(getClass().getClassLoader().getResource("rooms/roomNighttime.jpg"));
            }
            currentBackground = new Image(imagePath);
            roomBackground.setImage(currentBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startBackgroundTimeChecker() {
        Thread timeCheckerThread = new Thread(() -> {
            while (true) {
                LocalTime currentTime = LocalTime.now();

                System.out.println(isNightTime(currentTime));
                System.out.println("TIME: " + currentTime);
                if (isNightTime(currentTime)) {
                    Platform.runLater(() -> updateRoomBackground("Nighttime"));
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
