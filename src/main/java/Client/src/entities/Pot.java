package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import enums.PotType;

import java.io.Serializable;

public class Pot extends Deco implements Serializable {

    @SerializedName("PotType")
    @Expose
    private PotType type;

    public Pot(String name, String imageFilePath, int price, PotType type, String descriptionText){
        super(name, imageFilePath, price, descriptionText);
        this.type = type;
    }

    public PotType getPotType(){
        return type;
    }

}
