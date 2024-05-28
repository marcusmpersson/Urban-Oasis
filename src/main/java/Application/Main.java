package main.java.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.URL;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {

        try {

            ClassLoader classLoader = getClass().getClassLoader();
            URL loginClass = classLoader.getResource("fxml/Login.fxml");
            if (loginClass == null) {
                throw new FileNotFoundException("FXML file not found.");
            }
            Font.loadFont(classLoader.getResource("fonts/PixeloidSans-mLxMm.ttf").toExternalForm(), 12);

            Parent root = FXMLLoader.load(loginClass);
            Scene scene = new Scene(root, 1161, 824);

            stage.setScene(scene);
            stage.setResizable(false);

            Image image = new Image("logo.png");
            stage.getIcons().add(image);
            stage.setTitle("Urban Oasis");

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
