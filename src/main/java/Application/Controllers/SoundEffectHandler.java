package main.java.Application.Controllers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
/** class plays all sounds in the game, including background music and sound effects.
 * @author Rana Noorzadeh */
public class SoundEffectHandler {

    private Clip backgroundMusic;
    private boolean musicPlaying;

    /** constructor sets musicPlaying check to false*/
    public SoundEffectHandler() {
        musicPlaying = false;
    }

    /** method chooses one of three songs in the resources and starts playing it on loop*/
    public void startBackgroundMusic() {
        try {
            int randomSong = ThreadLocalRandom.current().nextInt(1, 3 + 1);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/sounds/defaultMusic"+randomSong+".wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            musicPlaying = true;
            FloatControl audioControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            audioControl.setValue(-10.0f);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** method stop the currently playing song */
    public void stopBackgroundMusic() {
        musicPlaying = false;
        backgroundMusic.stop();
    }

    /** method checks if music is currently playing */
    public boolean musicIsPlaying() {
        return musicPlaying;
    }

    /** method receives a filepath and plays the sound once */
    private void playSound(String soundPath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundPath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** method plays the watering sound */
    public void playWaterSound() {
        playSound("src/main/resources/sounds/water.wav");
    }

    /** method plays the swapping items sound */
    public void playSwappingSound() {
        playSound("src/main/resources/sounds/swap.wav");
    }

    /** method plays the Pop-up sound */
    public void playPopupSound() {
        playSound("src/main/resources/sounds/popup.wav");
    }

    /** method plays the plant dying sound */
    public void playDeathSound() {
        playSound("src/main/resources/sounds/churchBell.wav");
        //playSound("src/main/resources/sounds/death.wav");
    }

    /** method plays a clicking sound */
    public void playClickSound() {
        playSound("src/main/resources/sounds/click.wav");
    }

    /** method plays cash register sound */
    public void playPurchaseSound() {
        playSound("src/main/resources/sounds/purchase.wav");
    }

    /** method plays the planting seed sound */
    public void playPlantingSound() {
        playSound("src/main/resources/sounds/planting.wav");
    }

}
