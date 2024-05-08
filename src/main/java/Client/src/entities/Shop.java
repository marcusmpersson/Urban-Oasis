package entities;

import Builders.ItemBuilder;
import enums.DecoType;
import enums.Rarity;

import java.util.ArrayList;

public class Shop {

    private ArrayList<Pot> pots;
    private ArrayList<Seed> seeds;
    private ArrayList<Deco> decos;

    /** constructor, adds all shopItems to different lists based off category (hardcoded) */
    public Shop() {
        pots = new ArrayList<>();
        seeds = new ArrayList<>();
        decos = new ArrayList<>();

        // add seeds to shop
        seeds.add(ItemBuilder.buildSeed(Rarity.COMMON));
        seeds.add(ItemBuilder.buildSeed(Rarity.RARE));
        seeds.add(ItemBuilder.buildSeed(Rarity.EPIC));
        seeds.add(ItemBuilder.buildSeed(Rarity.LEGENDARY));

        // add pots to shop
        pots.addAll(ItemBuilder.buildAllPots());

        // add decorations to shop
        decos.add(ItemBuilder.buildDeco(DecoType.TERRARIUM));
    }

    /** Method creates new instance of the selected Seed type and returns it. */
    public Seed getSeed(int index) {
        return ItemBuilder.buildSeed(seeds.get(index).getRarity());
    }

    /** Method creates new instance of the selected Pot and returns it. */
    public Pot getPot(int index) {
        return ItemBuilder.buildPot(pots.get(index).getPotType());
    }

    /** Method creates new instance of the selected Deco and returns it. */
    public Deco getDeco(int index) {
        return ItemBuilder.buildDeco(decos.get(index).getDecoType());
    }

    /** returns the size of pots arraylist */
    public int getPotsSize() {
        return pots.size();
    }

    /** returns the size of seeds arraylist */
    public int getSeedsSize() {
        return seeds.size();
    }

    /** returns the size of decos arraylist */
    public int getDecosSize() {
        return decos.size();
    }
}
