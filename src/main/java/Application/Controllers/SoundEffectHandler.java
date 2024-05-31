package main.java.Application.Controllers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class SoundEffectHandler {

    private Clip backgroundMusic;
    private boolean musicPlaying;

    public SoundEffectHandler() {
        musicPlaying = false;
    }

    public void startBackgroundMusic() {
        try {
            int randomSong = ThreadLocalRandom.current().nextInt(0, 3 + 1);
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

    public void stopBackgroundMusic() {
        musicPlaying = false;
        backgroundMusic.stop();
    }

    public boolean musicIsPlaying() {
        return musicPlaying;
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
