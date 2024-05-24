package Application.Controllers;

import controller.Controller;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.Application.Animations.Transitions;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * LoginController handles the login and registration processes.
 *
 * Author: Mouhammed Fakhro
 */
public class LoginController implements Initializable {

    private Controller clientController;
    private Transitions transitions;
    private Parent root;

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
    private ImageView signUpButton;

    @FXML
    private TextField emailSignup;

    @FXML
    private TextField usernameSignup;

    @FXML
    private TextField passwordSignup;

    @FXML
    private Text min8chars;

    @FXML
    private Text specialchar;

    @FXML
    private Text capitalletter;

    @FXML
    private Text onedigit;

    @FXML
    private ImageView mainBackground;

    @FXML
    private AnchorPane mainAnchor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.transitions = new Transitions();
        this.clientController = Controller.getInstance();
        transitions.frameFlyUpTransition(1400, registerFrame, 0.1);

        passwordSignup.textProperty().addListener((observableValue, oldValue, newValue) -> {
            setTextColor(newValue.matches(".*[A-Z].*"), capitalletter); // capital letter
            setTextColor(newValue.matches(".*[^a-zA-Z0-9].*"), specialchar); // has special char
            setTextColor(newValue.matches(".*[0-9].*"), onedigit); // has digit
            setTextColor(newValue.length() >= 8, min8chars); // has at least 8 chars
        });

        mainBackground.fitWidthProperty().bind(mainAnchor.widthProperty());
        mainBackground.fitHeightProperty().bind(mainAnchor.heightProperty());
    }

    /**
     * Sets the text color of the specified text element based on the validity.
     *
     * @param valid the validity of the condition
     * @param text the text element to update
     */
    public void setTextColor(boolean valid, Text text) {
        if (valid) {
            text.setFill(Color.rgb(32, 208, 67));
        } else {
            text.setFill(Color.rgb(68, 60, 151));
        }
    }

    /**
     * Displays a message using a fade-out transition.
     *
     * @param image the image view to display
     */
    public void displayMessage(ImageView image) {
        Task<Void> fadeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                Platform.runLater(() -> image.setOpacity(0));
                return null;
            }
        };
        new Thread(fadeTask).start();
    }

    /**
     * Switches to the main scene after a successful login.
     *
     * @param event the mouse event triggering the switch
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToLoggedInScene(MouseEvent event) throws IOException {
        URL mainClass = getClass().getClassLoader().getResource("fxml/Main.fxml");
        root = FXMLLoader.load(mainClass);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the sign-in process.
     *
     * @param mouseEvent the mouse event triggering the sign-in
     * @throws IOException if the FXML file cannot be loaded
     */
    public void signIn(MouseEvent mouseEvent) throws IOException {
        boolean loginAttemptSuccessful = clientController.loginAttempt(email.getText(), password.getText());

        if (!loginAttemptSuccessful) {
            wrongLogin.setOpacity(1);
            displayMessage(wrongLogin);
        } else {
            successfulLogin.setOpacity(1);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), successfulLogin);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                try {
                    switchToLoggedInScene(mouseEvent);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            fadeOut.play();
        }
    }

    /**
     * Handles the registration process.
     *
     * @param mouseEvent the mouse event triggering the registration
     */
    public void registerAccount(MouseEvent mouseEvent) {
        Boolean registerAttempt = clientController.registerAccountAttempt(emailSignup.getText(), usernameSignup.getText(), passwordSignup.getText());
        System.out.println(registerAttempt);
    }

    /**
     * Switches to the main frame view.
     *
     * @param mouseEvent the mouse event triggering the switch
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToMainFrame(MouseEvent mouseEvent) throws IOException {
        transitions.frameFlyUpTransition(1400, registerFrame, 1);
        transitions.frameFlyUpTransition(1500, mainLoginFrame, 1);
    }

    /**
     * Switches to the signup scene view.
     *
     * @param mouseEvent the mouse event triggering the switch
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToSignupScene(MouseEvent mouseEvent) throws IOException {
        registerFrame.setOpacity(1);
        transitions.frameFlyUpTransition(-1500, mainLoginFrame, 1);
        transitions.frameFlyUpTransition(-1400, registerFrame, 1);
    }

    /**
     * Handles mouse enter event for button glow effect.
     *
     * @param mouseEvent the mouse event
     */
    public void handleMouseEnteredButtonGlow(MouseEvent mouseEvent) {
        transitions.mouseEnteredButtonGlow(mouseEvent);
    }

    /**
     * Handles mouse exit event for button glow effect.
     *
     * @param mouseEvent the mouse event
     */
    public void handleMouseExitedButtonGlow(MouseEvent mouseEvent) {
        transitions.mouseExitedButtonGlow(mouseEvent);
    }
}
