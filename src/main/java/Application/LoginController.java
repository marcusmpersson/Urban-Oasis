package main.java.Application;

import Controllers.Controller;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;



import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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
    public void initialize(URL location, ResourceBundle resources) {
        createFlyUpAnimation(1400, registerFrame, 0.1);

        Controller controller = new Controller();
        //controller.registerAccountAttempt("Test@gmail.com", "Hello", "123456789F");

    }
    private void createFlyUpAnimation(double val, Group group, double seconds) {
        TranslateTransition flyUpAnimation = new TranslateTransition(Duration.seconds(seconds), group);
        flyUpAnimation.setByY(val);
        flyUpAnimation.setCycleCount(1);
        flyUpAnimation.play();
    }

    public boolean emailExists() {
        if (email.getText().equalsIgnoreCase("Test")) {
            return true;
        }
        return false;
    }

    public boolean passwordIsCorrect() {
        if (password.getText().equalsIgnoreCase("Test")) {
            return true;
        }
        return false;
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
        if ((!emailExists()) || (!passwordIsCorrect())) {
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
