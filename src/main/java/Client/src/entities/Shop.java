package entities;

import java.util.ArrayList;

public class Shop {
    private ArrayList<ShopItem> shopItems;

    public Shop(){
        shopItems = new ArrayList<>();
        //TODO: generate and add shop items!
        //4 seeds, pots
    }

    public ShopItem getShopItem(int index){
        return shopItems.get(index);
    }
}
