package entities;

public class ShopItem extends Item {

    public String imageFilePath;
    public String name;

    public ShopItem(String imageFilePath, String name, int price){
        super(price);
        this.imageFilePath = imageFilePath;
        this.name = name;
    }

    public String getImageFilePath(){
        return this.imageFilePath;
    }

}
