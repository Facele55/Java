import java.util.ArrayList;
import java.util.List;


public class FruitHouse {
    public final String warehouseName;
    public final Fruit houseFruitType;

    int houseCap;
    List<Fruit> fruitsList;


    public FruitHouse(String warehouseName, Fruit food, int capacity) {
        this.houseFruitType = food;
        this.houseCap = capacity;
        this.fruitsList = new ArrayList<Fruit>();
        this.warehouseName = warehouseName;
    }


    public void makeFood(Farmer name, int orderNum) throws InterruptedException {
        fruitsList.add(houseFruitType);
        Thread curr = new Thread(new CookAnItem(name, orderNum));
        curr.start();
    }

    private class CookAnItem implements Runnable {
        Farmer currentFarmer;

        public CookAnItem(Farmer currentFarmer, int orderNum) {
            this.currentFarmer = currentFarmer;

        }

        public void run() {
            Simulation.logEvent(SimulationEvent.machineCookingFood(FruitHouse.this, houseFruitType));
            try {
                Thread.sleep(houseFruitType.prodTimeMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Simulation.logEvent(SimulationEvent.machineDoneFood(FruitHouse.this, houseFruitType));
            synchronized (fruitsList) {
                synchronized (currentFarmer.fruitReady) {
                    int l = fruitsList.size();
                    fruitsList.remove(l - 1);
                    fruitsList.notifyAll();
                    currentFarmer.fruitReady.add(houseFruitType);
                    currentFarmer.fruitReady.notifyAll();
                }
            }
        }
    }

    public String toString() {
        return warehouseName;
    }
}