package main.java.Application.Controllers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
public class SoundEffectHandler {

    private Clip backgroundMusic;
    private String songPath;
    private boolean musicPlaying;

    public SoundEffectHandler() {
        musicPlaying = false;
        updateBackgroundMusic();
    }

    public void startBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(songPath));
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

    public void stopBackgroundMusic() {
        musicPlaying = false;
        backgroundMusic.stop();
    }

    public void updateBackgroundMusic() {
        songPath = "src/main/resources/sounds/defaultMusic.wav";

        /*
        if (sunrise time) {
            songPath = "resources/sounds/sunrise.mp3";
        } else if (night time){
            songPath = "resources/sounds/night.mp3";
        } else if (sunset time){
            songPath = "resources/sounds/sunset.mp3";
        }
         */

        if (musicPlaying) {
            stopBackgroundMusic();
            startBackgroundMusic();
        }
    }

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

    public void playWaterSound() {
        playSound("src/main/resources/sounds/water.wav");
    }

    public void playSwappingSound() {
        playSound("src/main/resources/sounds/swap.wav");
    }

    public void playPopupSound() {
        playSound("src/main/resources/sounds/popup.wav");
    }

    public void playDeathSound() {
        playSound("src/main/resources/sounds/churchBell.wav");
        //playSound("src/main/resources/sounds/death.wav");
    }

    public void playClickSound() {
        playSound("src/main/resources/sounds/click.wav");
    }

    public void playPurchaseSound() {
        playSound("src/main/resources/sounds/purchase.wav");
    }

    public void playPlantingSound() {
        playSound("src/main/resources/sounds/planting.wav");
    }

}
