package Client.src.entities;

import java.util.ArrayList;

public class Inventory {

    private ArrayList<PottedPlant> pottedPlants;
    private ArrayList<Pot> pots;
    private ArrayList<Seed> seeds;
    private ArrayList<Deco> decorations;

    /** constructor1 receives all items in one array and places them in correct categories */
    public Inventory(ArrayList<Item> allItems) {
        pottedPlants = new ArrayList<>();
        pots = new ArrayList<>();
        seeds = new ArrayList<>();
        decorations = new ArrayList<>();

        for (Item item : allItems){
            if (item instanceof PottedPlant){

                pottedPlants.add((PottedPlant)item);
            }
            else if (item instanceof Pot){
                pots.add((Pot)item);
            }
            else if (item instanceof Seed){
                seeds.add((Seed)item);
            }
            else if (item instanceof Deco){
                decorations.add((Deco)item);
            }
        }

    }

    /** constructor2 receives items in separate arrays and assigns them */
    public Inventory(ArrayList<PottedPlant> pottedPlants, ArrayList<Pot> pots,
                     ArrayList<Seed> seeds, ArrayList<Deco> decorations) {
        this.pottedPlants = pottedPlants;
        this.pots = pots;
        this.seeds = seeds;
        this.decorations = decorations;
    }
    public Inventory(){};

    /** constructor 3 */
    public Inventory(){
        pottedPlants = new ArrayList<>();
        pots = new ArrayList<>();
        seeds = new ArrayList<>();
        decorations = new ArrayList<>();
    }

    public void addItem(Item item){
        if (item instanceof PottedPlant){
            pottedPlants.add((PottedPlant)item);
        }
        else if (item instanceof Pot){
            pots.add((Pot)item);
        }
        else if (item instanceof Seed){
            seeds.add((Seed)item);
        }
        else if (item instanceof Deco){
            decorations.add((Deco)item);
        }
    }

    public ArrayList<PottedPlant> getPottedPlants() {
        return pottedPlants;
    }
    public PottedPlant getPottedPlantAt(int index) {
        synchronized (pottedPlants) {
            if (index < pottedPlants.size()) {
                return pottedPlants.get(index);
            } else {
                return null;
            }
        }
    }
    public void removePottedPlantAt(int index) {
        synchronized (pottedPlants) {
            pottedPlants.remove(index);
        }
    }

    public ArrayList<Pot> getPots() {
        return pots;
    }
    public Pot getPotAt(int index) {
        synchronized (pots) {
            if (index < pots.size()) {
                return pots.get(index);
            } else {
                return null;
            }
        }
    }
    public void removePotAt(int index) {
        synchronized (pots) {
            pots.remove(index);
        }
    }

    public ArrayList<Seed> getSeeds() {
        return seeds;
    }
    public Seed getSeedAt(int index) {
        synchronized (seeds) {
            if (index < seeds.size()) {
                return seeds.get(index);
            } else {
                return null;
            }
        }
    }
    public void removeSeedAt(int index) {
        synchronized (seeds) {
            seeds.remove(index);
        }
    }

    public ArrayList<Deco> getDecorations() {
        return decorations;
    }
    public Deco getDecorationAt(int index) {
        synchronized (decorations) {
            if (index < decorations.size()) {
                return decorations.get(index);
            } else {
                return null;
            }
        }
    }
    public void removeDecorationAt(int index) {
        synchronized (decorations) {
            decorations.remove(index);
        }
    }

}
