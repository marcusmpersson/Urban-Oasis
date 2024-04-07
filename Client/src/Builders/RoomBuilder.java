package Builders;

import entities.PlacementSlot;
import entities.Room;
import enums.Environment;
import java.util.ArrayList;

public class RoomBuilder {

    //this class is lowkey more Director than Builder so may change name later :)

    /** creates and returns the common/default room */
    public Room buildCommonRoom(){
        ArrayList<PlacementSlot> slots = new ArrayList<>();

        slots.add(new PlacementSlot(Environment.SHADE, 87, 258)); //index 0
        slots.add(new PlacementSlot(Environment.HUMID, 87, 430)); //index 1
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 87, 656)); //index 2
        slots.add(new PlacementSlot(Environment.SHADE, 265, 220)); //index 3
        slots.add(new PlacementSlot(Environment.SHADE, 387, 220)); //index 4
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 265, 568)); //index 5
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 387, 568)); //index 6
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 265, 744)); //index 7
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 386, 744)); //index 8
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 265, 923)); //index 9
        slots.add(new PlacementSlot(Environment.HUMID, 570, 246)); //index 10
        slots.add(new PlacementSlot(Environment.HUMID, 705, 246)); //index 11
        slots.add(new PlacementSlot(Environment.SUNNY, 570, 439)); //index 12
        slots.add(new PlacementSlot(Environment.SUNNY, 705, 439)); //index 13
        slots.add(new PlacementSlot(Environment.SUNNY, 906, 229)); //index 14
        slots.add(new PlacementSlot(Environment.SUNNY, 1038, 229)); //index 15
        slots.add(new PlacementSlot(Environment.SUNNY, 1169, 229)); //index 16
        slots.add(new PlacementSlot(Environment.SUNNY, 906, 435)); //index 17
        slots.add(new PlacementSlot(Environment.SUNNY, 1038, 435)); //index 18
        slots.add(new PlacementSlot(Environment.SUNNY, 1169, 435)); //index 19
        slots.add(new PlacementSlot(Environment.SUNNY, 813, 656)); //index 20
        slots.add(new PlacementSlot(Environment.SHADE, 802, 931)); //index 21
        slots.add(new PlacementSlot(Environment.SUNNY, 1020, 756)); //index 22
        slots.add(new PlacementSlot(Environment.SHADE, 1200, 660)); //index 23
        slots.add(new PlacementSlot(Environment.SHADE, 1200, 879)); //index 24

        ArrayList<String> imageFilePaths = new ArrayList<>();

        imageFilePaths.add("images/commonRoom/daytime.png"); // daytime (index 0)
        imageFilePaths.add("images/commonRoom/sunset.png"); // sunset (index 1)
        imageFilePaths.add("images/commonRoom/night.png"); // night (index 2)
        imageFilePaths.add("images/commonRoom/sunrise.png"); // sunrise (index 3)

        return new Room(slots, imageFilePaths);
    }

}
