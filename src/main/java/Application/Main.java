package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;

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

            Font.loadFont(classLoader.getResource("fonts/PixeloidSans-mLxMm.ttf").toExternalForm(), 12);

            Parent root = FXMLLoader.load(loginClass);
            stage.setScene(new Scene(root));

            Image image = new Image("logo.png");
            stage.getIcons().add(image);
            stage.setTitle("Urban Oasis");
            stage.setResizable(false);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
