package Controllers;

import entities.WidgetEntity;
import main.java.Application.MainController;

import java.util.ArrayList;

public class WidgetHandler {

    private MainController guiController;
    private LocalFileHandler localFileHandler;
    private ArrayList<WidgetEntity> widgets;

    public WidgetHandler(MainController controller) {
        this.guiController = controller;
        this.localFileHandler = new LocalFileHandler();
    }

    public void loadWidgets (String username) {
        this.widgets = localFileHandler.readLocalFile(username);

        if (widgets != null) {
            for (WidgetEntity widget : widgets) {
                //TODO: load widgets on desktop
            }
        }

        /*
        Thread widgetUpdate = new Thread(() -> guiController.reloadWidgets());
        widgetUpdate.start();
         */
    }

    public void updateLocalFile(String username) {
        localFileHandler.updateLocalFile(widgets, username);
    }


}
