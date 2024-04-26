package entities;

import enums.PotType;

public class Pot extends Deco {

    private PotType type;

    public Pot(String name, String imageFilePath, int price, PotType type){
        super(name, imageFilePath, price);
        this.type = type;
    }

    public PotType getPotType(){
        return type;
    }

}
