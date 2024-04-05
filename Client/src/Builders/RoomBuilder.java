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

        slots.add(new PlacementSlot(Environment.SHADE)); //index 0
        slots.add(new PlacementSlot(Environment.SHADE)); //index 1
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 2
        slots.add(new PlacementSlot(Environment.SHADE)); //index 3
        slots.add(new PlacementSlot(Environment.SHADE)); //index 4
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 5
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 6
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 7
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 8
        slots.add(new PlacementSlot(Environment.HALF_SHADE)); //index 9
        slots.add(new PlacementSlot(Environment.HUMID)); //index 10
        slots.add(new PlacementSlot(Environment.HUMID)); //index 11
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 12
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 13
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 14
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 15
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 16
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 17
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 18
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 19
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 20
        slots.add(new PlacementSlot(Environment.SHADE)); //index 21
        slots.add(new PlacementSlot(Environment.SUNNY)); //index 22
        slots.add(new PlacementSlot(Environment.SHADE)); //index 23
        slots.add(new PlacementSlot(Environment.SHADE)); //index 24

        ArrayList<String> imageFilePaths = new ArrayList<>();

        //TODO: fill image file paths:
        imageFilePaths.add(""); // daytime (index 0)
        imageFilePaths.add(""); // sunset (index 2)
        imageFilePaths.add(""); // night (index 3)
        imageFilePaths.add(""); // sunrise (index 4)

        return new Room(slots, imageFilePaths);
    }

}
