package entities;

import enums.Rarity;
import enums.Species;
import java.util.Random;
import java.util.ArrayList;

public class Seed extends ShopItem{

    private ArrayList<Species> possiblePlants;
    private Rarity rarity;
    private Species species;

    public Seed(Rarity rarity, ArrayList<Species> possibilities,String name, String imageFilePath, int price){
        super(imageFilePath, name, price);
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
