import java.util.List;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the fruit market , place its order, and then leave the
 * fruit market when the order is complete.
 */
public class Customer implements Runnable {
    private final String name;
    private final List<Fruit> order;
    private final int orderNum;
    private boolean orderStatus;
    private static int runningCounter = 0;


    public Customer(String name, List<Fruit> order) {
        this.name = name;
        this.order = order;
        this.orderNum = ++runningCounter;
    }

    public String toString() {
        return name;
    }

    public List<Fruit> getOrder() {
        return this.order;
    }

    public int getOrderNum() {
        return this.orderNum;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * This method defines what an Customer does: The customer attempts to
     * enter the fruit market, place its order, and then leave the fruit market
     * when the order is complete.
     */
    public void run() {
        synchronized (Simulation.currentCapacity) {
            Simulation.currentCapacity.add(this);
            Simulation.logEvent(SimulationEvent.CustomerEnteredFruitMarket(this));
            Simulation.currentCapacity.notifyAll();
            synchronized (Simulation.orderList) {
                Simulation.customersWithOrders.put(this.name, new Integer(this.orderNum));
                Simulation.orderList.add(this);
                Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, this.order, this.orderNum));
                Simulation.orderList.notifyAll();
            }
            synchronized (Simulation.completedOrder) {
                this.setOrderStatus(false);
                Simulation.completedOrder.add(this);
                System.out.println(this.isOrderStatus());
                while (!this.isOrderStatus()) {
                    try {
                        Simulation.completedOrder.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
                Simulation.completedOrder.notifyAll();
            }
            Simulation.customersWithOrders.remove(this.name, new Integer(this.orderNum));
            Simulation.currentCapacity.remove(this);
            Simulation.logEvent(SimulationEvent.CustomerLeavingFruitMarket(this));
            Simulation.currentCapacity.notifyAll();
        }
    }
}