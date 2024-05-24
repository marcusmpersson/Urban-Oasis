package main.java.Application.Animations;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;



public class Transitions {
    private DropShadow borderGlow;
    @FXML
    public void initialize() {
        borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.rgb(255,255,255));
        borderGlow.setWidth(30);
    }

    public Color getColorFromButton(ImageView imageView) {
        if (imageView.getId().equals("signIn")) {
            return Color.rgb(255,99,71);
        }
        if (imageView.getId().equals("register")) {
            return Color.rgb(51, 125, 255);
        }
        if (imageView.getId().equals("reset")) {
            return Color.rgb(175, 51, 255);
        }
        if (imageView.getId().contains("ButtonList")) {
            return Color.rgb(0,181,255);
        }
        return Color.rgb(255,255,255);
    }

    @FXML
    public void mouseEnteredButtonGlow(MouseEvent event) {
        initialize();
        ImageView imageview = (ImageView) event.getSource();
        borderGlow.setColor(getColorFromButton(imageview));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(borderGlow.spreadProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(Duration.millis(95), new KeyValue(borderGlow.spreadProperty(), 0.3, Interpolator.EASE_OUT))
        );
        timeline.play();
        imageview.setEffect(borderGlow);
    }

    @FXML
    public void mouseExitedButtonGlow(MouseEvent event) {
        ImageView imageview  = (ImageView) event.getSource();
        borderGlow.setColor(getColorFromButton(imageview));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(borderGlow.spreadProperty(), 0.3)),
                new KeyFrame(Duration.millis(95), new KeyValue(borderGlow.spreadProperty(), 0, Interpolator.EASE_OUT))
        );
        timeline.play();
        timeline.setOnFinished(e -> imageview.setEffect(null));
    }

    public ScaleTransition sizeUpOrDownAnimation(boolean up, Object image) {
        if (up) {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), (Node) image);
            scaleUp.setToX(1.2);
            scaleUp.setToY(1.2);
            return scaleUp;
        } else {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), (Node) image);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            return scaleDown;
        }
    }

    private void addHoverEffect(Group plantContainer, ImageView canIcon, ImageView infoIcon, ImageView circleImage) {
        plantContainer.setOnMouseEntered(event -> {
            sizeUpOrDownAnimation(true, circleImage).play();
            sizeUpOrDownAnimation(true, canIcon).play();
            sizeUpOrDownAnimation(true, infoIcon).play();
            circleImage.setVisible(true);
            canIcon.setVisible(true);
            infoIcon.setVisible(true);
        });

        plantContainer.setOnMouseExited(event -> {
            sizeUpOrDownAnimation(false, circleImage).play();
            sizeUpOrDownAnimation(false, canIcon).play();
            sizeUpOrDownAnimation(false, infoIcon).play();
            circleImage.setVisible(false);
            canIcon.setVisible(false);
            infoIcon.setVisible(false);
        });
    }

    public void frameFlyUpTransition(double val, Group group, double seconds) {
        TranslateTransition flyUpAnimation = new TranslateTransition(Duration.seconds(seconds), group);
        flyUpAnimation.setByY(val);
        flyUpAnimation.setCycleCount(1);
        flyUpAnimation.play();
    }
}
