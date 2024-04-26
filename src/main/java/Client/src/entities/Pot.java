package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.PotType;

public class Pot extends Deco {

    @SerializedName("PotType")
    @Expose
    private PotType type;

    public Pot(String name, String imageFilePath, int price, PotType type){
        super(name, imageFilePath, price);
        this.type = type;
    }

    public PotType getPotType(){
        return type;
    }

}
