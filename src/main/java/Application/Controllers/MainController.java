package main.java.Application.Controllers;

import controller.Controller;
import entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.Application.Animations.Transitions;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * MainController handles the main application interface and its transitions.
 *
 * Author: Mouhammed Fakhro
 */
public class MainController implements Initializable {

    public ImageView resetButton;

    @FXML
    public Text priceText;
    @FXML
    public ImageView purchaseItemButton;
    @FXML
    public Text userCoins;

    @FXML
    public Group plantInformationPopup;

    @FXML
    public ImageView closePopupButton;

    @FXML
    public Group inventoryView;

    @FXML
    public ImageView inventoryButtonList;
    private Stage stage;
    private Scene scene;
    private boolean isLoggedIn;
    private Transitions transitions = new Transitions();
    private StoreController storeController;
    private WidgetHandler widgetHandler;
    @FXML
    private Group storeView;
    @FXML
    private Group roomView;
    @FXML
    private ImageView roomBackground;
    @FXML
    private TilePane shopPane;
    @FXML
    private ImageView returnButton;
    @FXML
    private ImageView soundButton;
    private Controller clientController;
    private User user;
    private RoomController roomController;
    private InventoryController inventoryController;
    @FXML
    private ScrollPane scrollPane;


    /**
     * This method runs as soon as the Main view opens up.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isLoggedIn = true;
        clientController = Controller.getInstance();
        user = clientController.getTestUser();
        roomController = new RoomController(roomView, user);
        storeController = new StoreController(this, storeView, shopPane, priceText, purchaseItemButton, plantInformationPopup, closePopupButton);
        inventoryController = new InventoryController();

        roomView.toFront();

        switchToRoomView();
        startBackgroundTimeChecker();
        updateUserCoins();

    }

    /**
     * Cleans up resources when the application is closed or user logs out.
     */
    public void cleanup() {
        isLoggedIn = false;
    }

    /**
     * Switches to the room view.
     *
     */
    public void switchToRoomView() {
        storeView.setOpacity(0);
        inventoryView.setOpacity(0);
        roomView.setOpacity(1);
        roomView.toFront();
        storeView.toBack();
        inventoryView.toBack();
        storeController.closeStoreRunningContent();
    }

    public void switchToInventoryView() {
        storeView.setOpacity(0);
        roomView.setOpacity(0);
        inventoryView.setOpacity(1);
        inventoryView.toFront();
        roomView.toBack();
        storeView.toBack();
    }



    /**
     * Switches to the store view.
     *
     */
    public void switchToStoreView() {
        roomView.setOpacity(0);
        inventoryView.setOpacity(0);
        storeView.setOpacity(1);
        storeView.toFront();
        inventoryView.toBack();
        roomView.toBack();
    }

    /**
     * Handles mouse entered event to apply glow effect on buttons.
     *
     * @param mouseEvent the mouse event
     */
    public void handleMouseEntered(MouseEvent mouseEvent) {
        transitions.mouseEnteredButtonGlow(mouseEvent);
    }

    /**
     * Handles mouse exited event to remove glow effect from buttons.
     *
     * @param mouseEvent the mouse event
     */
    public void handleMouseExited(MouseEvent mouseEvent) {
        transitions.mouseExitedButtonGlow(mouseEvent);
    }

    /**
     * Updates the room background image based on the time of day.
     *
     * @param timeOfDay the current time of day
     */
    public void updateRoomBackground(String timeOfDay) {
        String imagePath;
        try {
            switch (timeOfDay) {
                case "Night":
                    imagePath = String.valueOf(getClass().getClassLoader().getResource(user.getRoom(0).getNightFilepath()));
                    break;
                case "Sunrise":
                    imagePath = String.valueOf(getClass().getClassLoader().getResource(user.getRoom(0).getSunriseFilepath()));
                    break;
                case "Sunset":
                    imagePath = String.valueOf(getClass().getClassLoader().getResource(user.getRoom(0).getSunsetFilepath()));
                    break;
                default:
                    imagePath = String.valueOf(getClass().getClassLoader().getResource(user.getRoom(0).getDaytimeFilepath()));
                    break;
            }
            roomBackground.setImage(new Image(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBackgroundTasks(String timeOfDay) {
        updateRoomBackground(timeOfDay);
        roomController.updateAllPottedPlantImages();
    }

    /**
     * Determines the time of day based on the given time.
     *
     * @param time the current time
     * @return the time of day as a string
     */
    public String getTimeOfDay(LocalTime time) {
        String timeOfDay = "Night";
        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(9, 0))) {
            timeOfDay = "Sunrise";
        } else if (time.isAfter(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(16, 0))) {
            timeOfDay = "Day";
        } else if (time.isAfter(LocalTime.of(16, 0)) && time.isBefore(LocalTime.of(19, 0))) {
            timeOfDay = "Sunset";
        }
        return timeOfDay;
    }

    /**
     * Starts a background thread to check the current time and update the room background accordingly.
     */
    public void startBackgroundTimeChecker() {
        Thread timeCheckerThread = new Thread(() -> {
            while (isLoggedIn) {
                LocalTime currentTime = LocalTime.now();
                String timeOfDay = getTimeOfDay(currentTime);
                Platform.runLater(() -> updateBackgroundTasks(timeOfDay));

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

    /**
     * Switches to the login scene.
     *
     * @param event the mouse event triggering the switch
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToLoginScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        cleanup();
    }

    public void handleMusicButton(MouseEvent event) {
        if (clientController.musicIsPlaying()) {
            clientController.pauseMusic();
            soundButton.setImage(getImageFromImageName("icons/soundOFF.png"));
        } else {
            clientController.playMusic();
            soundButton.setImage(getImageFromImageName("icons/soundON.png"));
        }
    }

    private Image getImageFromImageName(String name) {
        URL resourceUrl = getClass().getClassLoader().getResource(name);
        if (resourceUrl == null) {
            System.out.println("Resource not found: " + name);
            return null;
        }
        return new Image(resourceUrl.toString());
    }

    public void resetRoom (MouseEvent event) {
        roomController.resetRoom();
    }

    public void showStoreSeeds () {
        storeController.showCategory("Seeds");
    }

    public void showStorePots () {
        storeController.showCategory("Pots");
    }

    public void showStoreDecos () {
        storeController.showCategory("Decos");
    }

    public void updateUserCoins() {
        userCoins.setText(String.valueOf(user.getShopCurrency()));
    }
}
