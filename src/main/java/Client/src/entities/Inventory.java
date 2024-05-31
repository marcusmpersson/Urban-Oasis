package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/** entity class represents the inventory of a User in the game.
 * @author Rana Noorzadeh
 * */
public class Inventory implements Serializable {

    @SerializedName("pottedplant")
    @Expose
    private ArrayList<PottedPlant> pottedPlants = new ArrayList<>();;
    @SerializedName("pots")
    @Expose
    private ArrayList<Pot> pots = new ArrayList<>();;
    @SerializedName("seeds")
    @Expose
    private ArrayList<Seed> seeds = new ArrayList<>();;
    @SerializedName("Decorations")
    @Expose
    private ArrayList<Deco> decorations = new ArrayList<>();;

    /** method receives an item and places it in the correct category in the inventory */
    public void addItem(Item item) {
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

    // ------------------------------------------
    // CATEGORY: PottedPlant
    // ------------------------------------------

    /** returns an arraylist containing all pottedPlants in the inventory */
    public ArrayList<PottedPlant> getPottedPlants() {
        return pottedPlants;
    }

    /** returns the PottedPlant at given index.
     * returns null if index is out of bounds */
    public PottedPlant getPottedPlantAt(int index) {
        if (index < pottedPlants.size()) {
            return pottedPlants.get(index);
        }
        return null;
    }

    /** removes the PottedPlant at given index */
    public void removePottedPlantAt(int index) {
        synchronized (pottedPlants) {
            if (index < pottedPlants.size()) {
                pottedPlants.remove(index);
            }
        }
    }

    // ------------------------------------------
    // CATEGORY: Pot
    // ------------------------------------------

    /** returns an arraylist containing all pots in the inventory */
    public ArrayList<Pot> getPots() {
        return pots;
    }

    /** returns the Pot at given index.
     * returns null if index is out of bounds */
    public Pot getPotAt(int index) {
        if (index < pots.size()) {
            return pots.get(index);
        }
        return null;
    }

    /** removes the Pot at given index */
    public void removePotAt(int index) {
        synchronized (pots) {
            pots.remove(index);
        }
    }

    // ------------------------------------------
    // CATEGORY: Seed
    // ------------------------------------------

    /** returns an arraylist containing all seeds in the inventory */
    public ArrayList<Seed> getSeeds() {
        return seeds;
    }

    /** returns the Seed at given index.
     * returns null if index is out of bounds */
    public Seed getSeedAt(int index) {
        synchronized (seeds) {
            if (index < seeds.size()) {
                return seeds.get(index);
            }
            return null;
        }
    }

    /** removes the Seed at given index */
    public void removeSeedAt(int index) {
        synchronized (seeds) {
            seeds.remove(index);
        }
    }

    // ------------------------------------------
    // CATEGORY: Deco
    // ------------------------------------------

    /** returns an arraylist containing all decorations in the inventory */
    public ArrayList<Deco> getDecorations() {
        return decorations;
    }

    /** returns the Deco at given index.
     * returns null if index is out of bounds */
    public Deco getDecorationAt(int index) {
        synchronized (decorations) {
            if (index < decorations.size()) {
                return decorations.get(index);
            }
            return null;
        }
    }

    /** removes the Deco at given index */
    public void removeDecorationAt(int index) {
        synchronized (decorations) {
            decorations.remove(index);
        }
    }
}
