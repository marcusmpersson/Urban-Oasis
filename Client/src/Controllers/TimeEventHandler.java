package Controllers;

public class TimeEventHandler {

    private GameHandler gameHandler;
    private boolean threadsAreRunning;
    private OneMinuteThread oneMinuteThread;
    private FiveMinuteThread fiveMinuteThread;
    private Controller controller;

    public TimeEventHandler(GameHandler gameHandler, Controller controller){
        this.gameHandler = gameHandler;
        this.controller = controller;
    }

    public void startThreads(){
        oneMinuteThread = new OneMinuteThread();
        fiveMinuteThread = new FiveMinuteThread();

        threadsAreRunning = true;

        oneMinuteThread.start();
        fiveMinuteThread.start();
    }

    public void stopAllThreads(){
        threadsAreRunning = false;
        try {
            oneMinuteThread.join();
            fiveMinuteThread.join();
        } catch(InterruptedException e){
            System.out.println("TimeEventHandler: joining threads was interrupted.");
        } finally {
            oneMinuteThread = null;
            fiveMinuteThread = null;
        }
    }

    public class OneMinuteThread extends Thread {

        public OneMinuteThread(){
            //TODO: save current time as instance variable
        }

        @Override
        public void run() {
            while (threadsAreRunning) {
                try {
                    Thread.sleep(60000); //sleep thread for 1 min
                    gameHandler.autoIncreaseCurrency();
                    gameHandler.raiseAges();

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

    public class FiveMinuteThread extends Thread {
        @Override
        public void run() {
            while (threadsAreRunning) {
                try {
                    Thread.sleep(300000);
                    controller.saveGame();
                    //TODO: add lastUpdatedTime instance variable for user.
                    // here: update lastUpdatedTime for user

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
