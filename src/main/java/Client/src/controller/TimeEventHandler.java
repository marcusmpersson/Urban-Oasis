package controller;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeEventHandler {

    private GameHandler gameHandler;
    private boolean threadsAreRunning;
    private GameThread gameThread;
    private AutoSaveThread autoSaveThread;

    /** constructor assigns reference to GameHandler.*/
    public TimeEventHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        threadsAreRunning = false;
    }

    /** method sets thread running condition to true.
     * initiates and starts all thread.*/
    public void startThreads() {
        gameThread = new GameThread();
        autoSaveThread = new AutoSaveThread();
        threadsAreRunning = true;
        gameThread.start();
        autoSaveThread.start();
    }

    /** method sets thread running condition to false.
     * joins all running threads, finally nulls them */
    public void stopAllThreads() {
        threadsAreRunning = false;
        gameHandler.updateUserLastUpdatedTime(LocalDateTime.now());
        Controller.getInstance().saveGame();

        try {
            gameThread.interrupt();
            autoSaveThread.interrupt();

        } catch(Exception e) {
            System.out.println("TimeEventHandler: interruption unsuccessful ");

        } finally {
            gameThread = null;
            autoSaveThread = null;
        }
    }

    /** inner Thread class GameThread handles game event related time tracking. */
    public class GameThread extends Thread {
        LocalDateTime lastCheckedTime;

        @Override
        public void run() {
            // save current time
            lastCheckedTime = LocalDateTime.now();

            while (threadsAreRunning) {
                try {
                    //sleep thread for 1 min, then increase age and currency
                    Thread.sleep(60000);
                    gameHandler.increaseCurrency(1);
                    gameHandler.raiseAges(1);
                    
                    // check current time
                    // if it's been an hour, update water and environment levels
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isAfter(lastCheckedTime) &&
                            Duration.between(lastCheckedTime, now).toHours() >= 1) {

                        gameHandler.lowerAllWaterLevels(1, false);
                        gameHandler.updateEnvSatisfactions(1, false);

                        // save current time
                        lastCheckedTime = now;
                        gameHandler.updateUserLastUpdatedTime(now);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    /** inner Thread class AutoSaveThread handles auto saving */
    public class AutoSaveThread extends Thread {

        public void run() {
            while (threadsAreRunning) {
                try {
                    // sleep thread for 5 minutes
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // save the game
                Controller.getInstance().saveGame();
            }
        }
    }
}
