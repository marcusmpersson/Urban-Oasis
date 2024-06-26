package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.Rarity;
import enums.Species;

import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

/** entity class represents a Seed item in the game.
 * @author Rana Noorzadeh
 * @author Ingrid Merz*/
public class Seed extends ShopItem implements Serializable {

    @SerializedName("PossiblePlants")
    @Expose
    private ArrayList<Species> possiblePlants;
    @SerializedName("Rarity")
    @Expose
    private Rarity rarity;
    @SerializedName("Species")
    @Expose
    private Species species;

    /** constructor creates a Seed with given rarity, list of possibilities, name, image filepath,
     * price, and description. Chooses a random species from the list of possibilities. */
    public Seed(Rarity rarity, ArrayList<Species> possibilities, String name, String imageFilePath, int price, String descriptionText) {
        super(imageFilePath, name, price, descriptionText);
        this.rarity = rarity;
        this.possiblePlants = possibilities;
        this.species = speciesAtRandom();
    }

    /** method returns a random species from the list of possible species. */
    private Species speciesAtRandom() {
        Random random = new Random();
        return this.possiblePlants.get(random.nextInt(possiblePlants.size()));
    }

    /** method returns the rarity of the seed. */
    public Rarity getRarity() {
        return rarity;
    }

    /** method returns the randomly selected species of the seed. */
    public Species getSpecies() {
        return species;
    }
}
