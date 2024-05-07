package Controllers;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeEventHandler {

    private GameHandler gameHandler;
    private boolean threadsAreRunning;
    private GameThread gameThread;
    private AutoSaveThread autoSaveThread;
    private Controller controller;

    public TimeEventHandler(GameHandler gameHandler, Controller controller) {
        this.gameHandler = gameHandler;
        this.controller = controller;
    }

    public void startThreads() {
        gameThread = new GameThread();
        autoSaveThread = new AutoSaveThread();
        threadsAreRunning = true;
        gameThread.start();
        autoSaveThread.start();
    }

    public void stopAllThreads() {
        threadsAreRunning = false;
        try {
            gameThread.join();
            autoSaveThread.join();

        } catch(InterruptedException e) {
            System.out.println("TimeEventHandler: joining threads was interrupted.");

        } finally {
            gameThread = null;
            autoSaveThread = null;
        }
    }

    public class GameThread extends Thread {
        LocalDateTime lastCheckedTime;

        @Override
        public void run() {
            lastCheckedTime = LocalDateTime.now();

            while (threadsAreRunning) {
                try {
                    Thread.sleep(60000); //sleep thread for 1 min
                    gameHandler.increaseCurrency(1);
                    gameHandler.raiseAges(1);

                    LocalDateTime now = LocalDateTime.now();
                    // if it's been an hour
                    if (now.isAfter(lastCheckedTime) &&
                            Duration.between(lastCheckedTime, now).toHours() >= 1) {

                        gameHandler.lowerAllWaterLevels(1);
                        gameHandler.updateEnvSatisfactions(1);

                        lastCheckedTime = now;
                        gameHandler.updateUserLastUpdatedTime(now);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    public class AutoSaveThread extends Thread {

        public void run() {
            while (threadsAreRunning) {
                try {
                    Thread.sleep(300000); //sleep thread for 5 minutes
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
               // controller.saveGame();
            }
        }
    }
}
