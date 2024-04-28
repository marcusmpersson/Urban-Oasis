package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.Rarity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class User implements Serializable {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("inventory")
    @Expose
    private Inventory inventory;
    @SerializedName("rooms")
    @Expose
    private ArrayList<Room> rooms;
    @SerializedName("currency")
    @Expose
    private int shopCurrency;
    @SerializedName("lastSave")
    @Expose
    private String lastUpdatedTime;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        lastUpdatedTime = formattedDateTime;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }
}
