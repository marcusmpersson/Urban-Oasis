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

    @FXML
    private Text priceText;
    @FXML
    private Text userCoins;
    @FXML
    private ImageView purchaseItemButton;
    @FXML
    private ImageView closePopupButton;
    @FXML
    private ImageView plantSeedButton;
    @FXML
    private ImageView cancelPlantSeed;
    @FXML
    private ImageView disposeItem;
    @FXML
    private ImageView putInRoomButton;

    private ImageView soundButton;
    @FXML
    private Group plantInformationPopup;
    @FXML
    private Group inventoryView;
    @FXML
    private Group storeView;
    @FXML
    private Group roomView;
    @FXML
    private ImageView roomBackground;
    @FXML
    private TilePane shopPane;
    @FXML
    private TilePane inventoryPane;

    private Stage stage;
    private Scene scene;
    private boolean isLoggedIn;
    private final Transitions transitions = new Transitions();
    private StoreController storeController;
    private Controller clientController;
    private User user;
    private RoomController roomController;
    private InventoryController inventoryController;

    /**
     * Initializes the MainController and sets up the initial view.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isLoggedIn = true;
        clientController = Controller.getInstance();
        user = clientController.getCurrentUser();
        storeController = new StoreController(this, user, storeView, shopPane, priceText, purchaseItemButton,
                plantInformationPopup, closePopupButton);
        inventoryController = new InventoryController(this, clientController, inventoryPane, inventoryView,
                plantInformationPopup, plantSeedButton, cancelPlantSeed, disposeItem, putInRoomButton);
        roomController = new RoomController(roomView, user, clientController, inventoryController);

        roomView.toFront();
        switchToRoomView();
        startBackgroundTimeChecker();
        updateUserCoins();
    }

    /**
     * Cleans up resources when the application is closed or the user logs out.
     */
    public void cleanup() {
        isLoggedIn = false;
        clientController.logoutAttempt();
    }

    /**
     * Switches to the room view.
     */
    public void switchToRoomView() {
        setViewOpacity(storeView, 0);
        setViewOpacity(inventoryView, 0);
        setViewOpacity(roomView, 1);
        roomView.toFront();
        storeView.toBack();
        inventoryView.toBack();
        storeController.closeStoreRunningContent();
        roomController.updateAvailablePlants();
    }

    /**
     * Switches to the inventory view.
     * @author Mouhammed Fakhro
     * @author Christian Storck
     */
    public void switchToInventoryView() {
        setViewOpacity(storeView, 0);
        setViewOpacity(roomView, 0);
        setViewOpacity(inventoryView, 1);
        inventoryView.toFront();
        roomView.toBack();
        storeView.toBack();
        inventoryController.openInventoryView();
    }

    /**
     * Switches to the store view.
     * @author Mouhammed Fakhro
     * @author Christian Storck
     */
    public void switchToStoreView() {
        setViewOpacity(roomView, 0);
        setViewOpacity(inventoryView, 0);
        setViewOpacity(storeView, 1);
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
                    imagePath = user.getRoom(0).getNightFilepath();
                    break;
                case "Sunrise":
                    imagePath = user.getRoom(0).getSunriseFilepath();
                    break;
                case "Sunset":
                    imagePath = user.getRoom(0).getSunsetFilepath();
                    break;
                default:
                    imagePath = user.getRoom(0).getDaytimeFilepath();
                    break;
            }
            roomBackground.setImage(new Image(getClass().getClassLoader().getResource(imagePath).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates background tasks such as room background and user coins.
     *
     * @param timeOfDay the current time of day
     */
    private void updateBackgroundTasks(String timeOfDay) {
        updateRoomBackground(timeOfDay);
        roomController.updateAllPottedPlantImages();
        updateUserCoins();
    }

    /**
     * Determines the time of day based on the given time.
     *
     * @param time the current time
     * @return the time of day as a string
     */
    public String getTimeOfDay(LocalTime time) {
        if (time.isAfter(LocalTime.of(5, 0)) && time.isBefore(LocalTime.of(9, 0))) {
            return "Sunrise";
        } else if (time.isAfter(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(16, 0))) {
            return "Day";
        } else if (time.isAfter(LocalTime.of(16, 0)) && time.isBefore(LocalTime.of(19, 0))) {
            return "Sunset";
        }
        return "Night";
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

    /**
     * Handles the music button to toggle music on and off.
     *
     * @param event the mouse event triggering the toggle
     */
    public void handleMusicButton(MouseEvent event) {
        if (clientController.musicIsPlaying()) {
            clientController.pauseMusic();
            soundButton.setImage(getImageFromImageName("icons/soundOFF.png"));
        } else {
            clientController.playMusic();
            soundButton.setImage(getImageFromImageName("icons/soundON.png"));
        }
    }

    /**
     * Retrieves an Image object from an image file name.
     *
     * @param name the name of the image file
     * @return the Image object
     */
    private Image getImageFromImageName(String name) {
        URL resourceUrl = getClass().getClassLoader().getResource(name);
        if (resourceUrl == null) {
            System.out.println("Resource not found: " + name);
            return null;
        }
        return new Image(resourceUrl.toString());
    }

    /**
     * Resets the room.
     *
     * @param event the mouse event triggering the reset
     */
    public void resetRoom(MouseEvent event) {
        roomController.resetRoom();
    }

    /**
     * Displays the seed items in the store.
     */
    public void showStoreSeeds() {
        storeController.showCategory("Seeds");
    }

    /**
     * Displays the pot items in the store.
     */
    public void showStorePots() {
        storeController.showCategory("Pots");
    }

    /**
     * Displays the deco items in the store.
     */
    public void showStoreDecos() {
        storeController.showCategory("Decos");
    }

    /**
     * Updates the user's coin display.
     */
    public void updateUserCoins() {
        userCoins.setText(String.valueOf(user.getShopCurrency()));
    }

    /**
     * Displays the seed items in the inventory.
     */
    public void showInventorySeeds() {
        inventoryController.showCategory("Seeds");
    }

    /**
     * Displays the deco items in the inventory.
     */
    public void showInventoryDecos() {
        inventoryController.showCategory("Decos");
    }

    /**
     * Displays the pot items in the inventory.
     */
    public void showInventoryPots() {
        inventoryController.showCategory("Pots");
    }

    /**
     * Displays the plant items in the inventory.
     */
    public void showInventoryPlants() {
        inventoryController.showCategory("Plants");
    }

    /**
     * Closes the popup frame.
     */
    public void closePopupFrame() {
        inventoryController.animatePopupFrame(false);
        storeController.animatePopupFrame(false);
    }

    /**
     * Sets the opacity of the given view.
     *
     * @param view    the view to set opacity for
     * @param opacity the opacity value
     */
    private void setViewOpacity(Node view, double opacity) {
        view.setOpacity(opacity);
    }
}
