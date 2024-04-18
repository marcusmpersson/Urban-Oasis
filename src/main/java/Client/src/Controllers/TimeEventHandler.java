package Controllers;

public class TimeEventHandler {

    private GameHandler gameHandler;
    private boolean threadsAreRunning;
    private GameThread gameThread;
    private GUIThread guiThread;
    private Controller controller;

    public TimeEventHandler(GameHandler gameHandler, Controller controller) {
        this.gameHandler = gameHandler;
        this.controller = controller;
    }

    public void startThreads() {
        gameThread = new GameThread();
        guiThread = new GUIThread();

        threadsAreRunning = true;

        gameThread.start();
        guiThread.start();
    }

    public void stopAllThreads() {
        threadsAreRunning = false;
        try {
            gameThread.join();
            guiThread.join();

        } catch(InterruptedException e) {
            System.out.println("TimeEventHandler: joining threads was interrupted.");

        } finally {
            gameThread = null;
            guiThread = null;

        }
    }

    public class GameThread extends Thread {

        public GameThread() {
            //TODO: save current time as instance variable
        }

        @Override
        public void run() {
            while (threadsAreRunning) {
                try {
                    Thread.sleep(60000); //sleep thread for 1 min
                    gameHandler.autoIncreaseCurrency();
                    gameHandler.raiseAges();

                    Thread.sleep(2400000); //sleep thread for 4 more mins
                    //controller.saveGame();
                    //TODO: add lastUpdatedTime instance variable for user.
                    // here: update lastUpdatedTime for user

                    //TODO: if (its been over an hour)
                    // save new time
                    gameHandler.lowerAllWaterLevels();
                    gameHandler.updateEnvSatisfactions();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }

    public class GUIThread extends Thread {
        @Override
        public void run() {

            while (threadsAreRunning) {

                try {
                    Thread.sleep(1000);
                    controller.updateGUI();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }
}
