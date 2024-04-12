package entities;

import enums.Rarity;

import java.util.ArrayList;

public class User {

    private String username;
    private String email;
    private Inventory inventory;
    private ArrayList<Room> rooms;
    private int shopCurrency;

    /** constructor. assigning pre-made attributes.
     * (purpose: creating new User item with info retrieved from server) */
    public User(String username, String email, Inventory inventory,
                ArrayList<Room> rooms, int shopCurrency){
        this.username = username;
        this.email = email;
        this.inventory = inventory;
        this.rooms = rooms;
        this.shopCurrency = shopCurrency;
    }

    public String getUsername(){
        return username;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Room> getRoomsArray() {
        return rooms;
    }

    public Room getMainRoom(){
        return rooms.get(0);
    }

    public int getShopCurrency() {
        return shopCurrency;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public Room getRoom(int index) {
        return rooms.get(index);
    }

    public void subtractCurrency(int price) {
        shopCurrency -= price;
    }

    public void increaseCurrency(int amount){
        shopCurrency += amount;
    }
}
