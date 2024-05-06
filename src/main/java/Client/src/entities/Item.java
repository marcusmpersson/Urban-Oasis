package entities;
public abstract class Item {
    public int price;
    public String descriptionText;

    public Item (int price, String descriptionText){
        this.price = price;
        this.descriptionText = descriptionText;
    }

    public int getPrice(){
        return this.price;
    }

    public String getDescriptionText() {
        return descriptionText;
    }


}
