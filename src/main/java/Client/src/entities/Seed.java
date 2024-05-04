package entities;

import Builders.PlantTopBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.Rarity;
import enums.Species;

import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

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

    public Seed(Rarity rarity, ArrayList<Species> possibilities,String name, String imageFilePath, int price, String descriptionText){
        super(imageFilePath, name, price, descriptionText);
        this.rarity = rarity;
        this.possiblePlants = possibilities;
        this.species = speciesAtRandom();
    }

    public Species speciesAtRandom(){
        Random random= new Random();
        int index =  random.nextInt(possiblePlants.size());
        return this.possiblePlants.get(index);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Species getSpecies() {
        return species;
    }
}
