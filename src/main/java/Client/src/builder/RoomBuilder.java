package builder;

import entities.PlacementSlot;
import entities.Room;
import enums.Environment;
import java.util.ArrayList;
/** Static builder/director class used to build Room items.
 * @author Rana Noorzadeh
 * @author Mouhammed Fakhro */
public class RoomBuilder {

    /** creates and returns an instance of common/default room */
    public static Room buildCommonRoom(){
        ArrayList<PlacementSlot> slots = new ArrayList<>();

        slots.add(new PlacementSlot(Environment.SHADE, 22, 363)); //index 0
        slots.add(new PlacementSlot(Environment.HUMID, 22, 221)); //index 1
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 22, 546)); //index 2
        slots.add(new PlacementSlot(Environment.SHADE, 162, 195)); //index 3
        slots.add(new PlacementSlot(Environment.SHADE, 264, 194)); //index 4
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 162, 474)); //index 5
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 264, 474)); //index 6
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 164, 617)); //index 7
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 264, 618)); //index 8
        slots.add(new PlacementSlot(Environment.HALF_SHADE, 165, 762)); //index 9
        slots.add(new PlacementSlot(Environment.HUMID, 393, 212)); //index 10
        slots.add(new PlacementSlot(Environment.HUMID, 519, 214)); //index 11
        slots.add(new PlacementSlot(Environment.SUNNY, 393, 370)); //index 12
        slots.add(new PlacementSlot(Environment.SUNNY, 521, 370)); //index 13
        slots.add(new PlacementSlot(Environment.SUNNY, 660, 200)); //index 14
        slots.add(new PlacementSlot(Environment.SUNNY, 760, 200)); //index 15
        slots.add(new PlacementSlot(Environment.SUNNY, 858, 200)); //index 16
        slots.add(new PlacementSlot(Environment.SUNNY, 660, 369)); //index 17
        slots.add(new PlacementSlot(Environment.SUNNY, 760, 369)); //index 18
        slots.add(new PlacementSlot(Environment.SUNNY, 858, 369)); //index 19
        slots.add(new PlacementSlot(Environment.SUNNY, 608, 543)); //index 20
        slots.add(new PlacementSlot(Environment.SHADE, 603, 770)); //index 21
        slots.add(new PlacementSlot(Environment.SUNNY, 751, 625)); //index 22
        slots.add(new PlacementSlot(Environment.SHADE, 880, 548)); //index 23
        slots.add(new PlacementSlot(Environment.SHADE, 880, 727)); //index 24

        ArrayList<String> imageFilePaths = new ArrayList<>();

        imageFilePaths.add("rooms/commonRoom/daytime.png"); // daytime (index 0)
        imageFilePaths.add("rooms/commonRoom/sunset.png"); // sunset (index 1)
        imageFilePaths.add("rooms/commonRoom/night.png"); // night (index 2)
        imageFilePaths.add("rooms/commonRoom/sunrise.png"); // sunrise (index 3)

        return new Room(slots, imageFilePaths);
    }

}
