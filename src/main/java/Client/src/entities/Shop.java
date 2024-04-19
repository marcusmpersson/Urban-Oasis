package entities;

import Builders.ItemBuilder;
import enums.Rarity;

import java.util.ArrayList;

public class Shop {

    private ArrayList<ShopItem> shopItems;

    /** constructor adds all shopItems to an ArrayList */
    public Shop() {
        shopItems = new ArrayList<>();

        // add seeds to shop
        shopItems.add(ItemBuilder.buildSeed(Rarity.COMMON));
        shopItems.add(ItemBuilder.buildSeed(Rarity.RARE));
        shopItems.add(ItemBuilder.buildSeed(Rarity.EPIC));
        shopItems.add(ItemBuilder.buildSeed(Rarity.LEGENDARY));

        // add pots to shop
        shopItems.addAll(ItemBuilder.buildAllPots());
    }

    /** Method creates new identical instance of the selected shop item and returns it.
     * This is because, ideally, the same reference of the shop item shouldn't be returned.
     * */
    public ShopItem getShopItem(int index) {

        // if item is an instance of pot, create a new instance of the pot and return
        if (shopItems.get(index) instanceof Pot){
            return ItemBuilder.buildPot(((Pot) shopItems.get(index)).getPotType());
        }

        // if item is an instance of seed, create a new instance of seed and return
        else if (shopItems.get(index) instanceof Seed){
            return ItemBuilder.buildSeed(((Seed) shopItems.get(index)).getRarity());
        }

        return null;
    }
}
