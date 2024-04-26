package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.Rarity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("inventory")
    @Expose
    public Inventory inventory;
    @SerializedName("room")
    @Expose
    public ArrayList<Room> rooms;
    @SerializedName("shopcurrency")
    @Expose
    public int shopCurrency;
    public LocalDateTime lastUpdatedTime;

    /** constructor. assigning pre-made attributes.
     * (purpose: creating new User instance with info received from server) */
    public User(String username, String email, Inventory inventory,
                ArrayList<Room> rooms, int shopCurrency){
        this.username = username;
        this.email = email;
        this.inventory = inventory;
        this.rooms = rooms;
        this.shopCurrency = shopCurrency;
    }

    /** returns username of user*/
    public String getUsername(){
        return username;
    }

    /** returns reference to the user inventory */
    public Inventory getInventory() {
        return inventory;
    }

    /** returns an ArrayList of user's rooms */
    public ArrayList<Room> getRoomsArray() {
        return rooms;
    }

    /** returns the currency of user */
    public int getShopCurrency() {
        return shopCurrency;
    }

    /** sets the email of user (in current session) */
    public void setEmail (String email){
        this.email = email;
    }

    /** returns the email of user */
    public String getEmail(){
        return email;
    }

    /** returns player room at certain index */
    public Room getRoom(int index) {
        return rooms.get(index);
    }

    /** subtracts given amount from the user currency */
    public void subtractCurrency(int price) {
        shopCurrency -= price;
    }

    /** increases user currency by given amount */
    public void increaseCurrency(int amount){
        shopCurrency += amount;
    }

    public void setLastUpdatedTime(LocalDateTime now) {
        lastUpdatedTime = now;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }
}
