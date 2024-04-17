package Client.src.entities;

import Client.src.Controllers.GameHandler;

public class TimeEventHandler extends Thread {

    private GameHandler gameHandler;

    public TimeEventHandler(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void run(){

    }
}
