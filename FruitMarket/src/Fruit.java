public class Fruit {
    public final String name;
   // public final int cookTimeMS;
    public final int prodTimeMS;


    public Fruit(String name, int prodTimeMS) {
        this.name = name;
        this.prodTimeMS = prodTimeMS;
    }

    public String toString() {
        return name;
    }
}