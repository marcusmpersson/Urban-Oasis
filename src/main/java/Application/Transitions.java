package Application;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
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
    public void handleMouseEntered(MouseEvent event) {
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
    public void handleMouseExited(MouseEvent event) {
        ImageView imageview  = (ImageView) event.getSource();
        borderGlow.setColor(getColorFromButton(imageview));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(borderGlow.spreadProperty(), 0.3)),
                new KeyFrame(Duration.millis(95), new KeyValue(borderGlow.spreadProperty(), 0, Interpolator.EASE_OUT))
        );
        timeline.play();
        timeline.setOnFinished(e -> imageview.setEffect(null));
    }
}
