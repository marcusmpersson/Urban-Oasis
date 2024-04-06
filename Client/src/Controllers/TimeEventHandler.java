package Controllers;

public class TimeEventHandler {

    private GameHandler gameHandler;
    private boolean threadsAreRunning;

    public TimeEventHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    public void startThreads(){
        OneMinuteThread oneMinuteThread = new OneMinuteThread();
        OneHourThread oneHourThread = new OneHourThread();
        FiveMinuteThread fiveMinuteThread = new FiveMinuteThread();

        threadsAreRunning = true;

        oneMinuteThread.start();
        oneHourThread.start();
        fiveMinuteThread.start();
    }

    public void stopAllThreads(){
        threadsAreRunning = false;
    }

    public class OneMinuteThread extends Thread {

        public OneMinuteThread(){
            //save current time as instance variable
        }


        @Override
        public void run() {
            while (threadsAreRunning) {
                try {
                    //sleep thread for 1 min
                    Thread.sleep(60000);
                    //increase coins
                    //raise age

                    //check if its been over an hour
                    // save new time
                    //lower water level thread (every hour)
                    //update env satisfaction (every hour)

                } catch (Exception e) {
                    //TODO: separate catch clause based on exception types
                }
            }
        }
    }

    public class FiveMinuteThread extends Thread {
        @Override
        public void run() {
            while (threadsAreRunning) {
                try {
                    Thread.sleep();
                    //auto saves every 5 mins

                } catch (Exception e) {
                    //TODO: separate catch clause based on exception types
                }
            }
        }
    }
}
