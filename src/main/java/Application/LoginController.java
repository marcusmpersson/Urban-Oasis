package main.java.Application;

import Controllers.Controller;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Controller clientController;
    private Transitions transitions;

    private Stage stage;

    private Scene scene;

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
    private Group storeView;

    @FXML
    private Group roomView;

    @FXML
    private ImageView roomBackground;

    private Image currentBackground;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createFlyUpAnimation(1400, registerFrame, 0.1);
        this.clientController = new Controller();
        this.transitions = new Transitions();

        passwordSignup.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                setTextColor(newValue.matches(".*[A-Z].*"), capitalletter); // capital letter
                setTextColor(newValue.matches(".*[^a-zA-Z0-9].*"), specialchar); // has special char
                setTextColor(newValue.matches(".*[0-9].*"), onedigit); // has digit
                setTextColor(newValue.length() >= 8, min8chars); // has at least 8 chars
            }
        });
    }

    public void setTextColor(boolean valid, Text text) {
        if (valid) {
            text.setFill(Color.rgb(32, 208, 67));
        } else {
            text.setFill(Color.rgb(68,60,151));
        }
    }

    private void createFlyUpAnimation(double val, Group group, double seconds) {
        TranslateTransition flyUpAnimation = new TranslateTransition(Duration.seconds(seconds), group);
        flyUpAnimation.setByY(val);
        flyUpAnimation.setCycleCount(1);
        flyUpAnimation.play();
    }


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

    public void detectPasswordEligibiltiy() {

    }

    public void switchToLoggedInScene(MouseEvent event) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        System.out.println(classLoader);
        URL mainClass = classLoader.getResource("fxml/Main.fxml");
        System.out.println(mainClass);
        root = FXMLLoader.load(mainClass);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void signIn(MouseEvent mouseEvent) throws InterruptedException, IOException {
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

    public void registerAccount(MouseEvent mouseEvent) {
        String response = clientController.registerAccountAttempt(emailSignup.getText(), usernameSignup.getText(), passwordSignup.getText());
        System.out.println(response);
    }

    public void switchToMainFrame(MouseEvent mouseEvent) throws IOException, InterruptedException {
        createFlyUpAnimation(1400, registerFrame, 1);
        createFlyUpAnimation(1500, mainLoginFrame, 1);
    }

    public void switchToSignupScene(MouseEvent mouseEvent) throws IOException {
        registerFrame.setOpacity(1);
        createFlyUpAnimation(-1500, mainLoginFrame, 1);
        createFlyUpAnimation(-1400, registerFrame, 1);
    }

    public void handleMouseEntered(MouseEvent mouseEvent) {
        transitions.handleMouseEntered(mouseEvent);
    }

    public void handleMouseExited(MouseEvent mouseEvent) {
        transitions.handleMouseExited(mouseEvent);
    }

}
