package Controllers;

import entities.Placeable;
import entities.PottedPlant;
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
            //TODO: load widgets on desktop via GUI controller
        }
    }

    public void updateLocalFile(String username) {
        localFileHandler.updateLocalFile(widgets, username);
    }

    public void addWidget(Placeable item) {
        WidgetEntity widget = new WidgetEntity((PottedPlant) item, 200, 200);
        widgets.add(widget);

        //TODO: load widget via GUI controller
    }

    public WidgetEntity getWidgetAt(int index){
        return widgets.get(index);
    }
}
