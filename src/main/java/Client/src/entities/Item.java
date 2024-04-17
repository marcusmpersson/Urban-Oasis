package Client.src.entities;
public abstract class Item {
    public int price;

    public Item(){

    }
    public Item(int price){
        this.price = price;
    }

    public int getPrice(){
        return this.price;
    }


}
