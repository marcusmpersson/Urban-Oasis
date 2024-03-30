package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;



public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    //1461x1024
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));

        stage.setScene(new Scene(root));

        Image image = new Image("logo.png");
        stage.getIcons().add(image);
        stage.setTitle("Urban Oasis");
        stage.setResizable(false);

        stage.show();
    }
}
