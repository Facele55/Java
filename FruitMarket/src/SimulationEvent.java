import java.util.List;


public class SimulationEvent {
    public enum EventType {
        /* General events */
        SimulationStarting,
        SimulationEnded,
        /* Customer events */
        CustomerEnteredFruitMarket,
        CustomerPlacedOrder,
        CustomerReceivedOrder,
        CustomerLeavingFruitMarket,
        /* Farmer Events */
        FarmerReceivedOrder,
        FarmerStartedProd,
        FarmerFinishedProd,
        FarmerCompletedOrder,
        FarmerEnding,
        /* Warehouse events */
        HouseStarting,
        HouseStartingFruits,
        HouseDoneFood,
        marketEnding
    }

    public final EventType event;
    /* Not all of these fields are relevant for every event; 
       see factory methods below */
    public final Farmer farmer;
    public final Customer customer;
    public final FruitHouse fruitHouse;
    public final Fruit food;
    public final List<Fruit> orderFood;
    public final int orderNumber;
    public final int[] simParams;

    private SimulationEvent(EventType event,
                            Farmer farmer,
                            Customer customer,
                            FruitHouse fruitHouse,
                            Fruit food,
                            List<Fruit> orderFood,
                            int orderNumber,
                            int[] simParams) {
        this.event = event;
        this.farmer = farmer;
        this.customer = customer;
        this.fruitHouse = fruitHouse;
        this.food = food;
        this.orderFood = orderFood;
        this.orderNumber = orderNumber;
        this.simParams = simParams;
    }

    /* Factory methods */

    /* Simulation events */
    public static SimulationEvent startSimulation(int numCustomers,
                                                  int numCooks,
                                                  int numTables,
                                                  int capacity) {
        int[] params = new int[4];
        params[0] = numCustomers;
        params[1] = numCooks;
        params[2] = numTables;
        params[3] = capacity;
        return new SimulationEvent(EventType.SimulationStarting,
                null, null, null, null, null, 0,
                params);
    }

    public static SimulationEvent endSimulation() {
        return new SimulationEvent(EventType.SimulationEnded,
                null, null, null, null, null, 0, null);
    }

    /* Customer events */

    public static SimulationEvent CustomerEnteredFruitMarket(Customer customer) {
        return new SimulationEvent(EventType.CustomerEnteredFruitMarket,
                null,
                customer,
                null, null, null, 0, null);
    }

    public static SimulationEvent customerPlacedOrder(Customer customer,
                                                      List<Fruit> order,
                                                      int orderNumber) {
        return new SimulationEvent(EventType.CustomerPlacedOrder,
                null,
                customer,
                null, null,
                order, orderNumber,
                null);
    }

    public static SimulationEvent customerReceivedOrder(Customer customer,
                                                        List<Fruit> order,
                                                        int orderNumber) {
        return new SimulationEvent(EventType.CustomerReceivedOrder,
                null,
                customer,
                null, null,
                order, orderNumber,
                null);
    }

    public static SimulationEvent CustomerLeavingFruitMarket(Customer customer) {
        return new SimulationEvent(EventType.CustomerLeavingFruitMarket,
                null,
                customer,
                null, null, null, 0, null);
    }


    public static SimulationEvent FarmerReceivedOrder(Farmer farmer,
                                                    List<Fruit> order,
                                                    int orderNumber) {
        return new SimulationEvent(EventType.FarmerReceivedOrder,
                farmer,
                null, null, null,
                order, orderNumber,
                null);
    }

    public static SimulationEvent FarmerStartedProd(Farmer farmer, Fruit food,
                                                  int orderNumber) {
        return new SimulationEvent(EventType.FarmerStartedProd,
                farmer,
                null, null,
                food,
                null,
                orderNumber,
                null);
    }

    public static SimulationEvent FarmerFinishedProd(Farmer farmer, Fruit food,
                                                   int orderNumber) {
        return new SimulationEvent(EventType.FarmerFinishedProd,
                farmer,
                null, null,
                food,
                null,
                orderNumber,
                null);
    }

    public static SimulationEvent farmerCompletedOrder(
            Farmer farmer, int orderNumber) {
        return new SimulationEvent(EventType.FarmerCompletedOrder,
                farmer,
                null, null, null, null,
                orderNumber,
                null);
    }

    public static SimulationEvent FarmerEnding(Farmer farmer) {
        return new SimulationEvent(EventType.FarmerEnding, farmer,
                null, null, null, null, 0, null);
    }

    /* Machine events */
    public static SimulationEvent HouseStarting(FruitHouse fruitHouse,
                                                  Fruit food,
                                                  int capacity) {
        int[] params = new int[1];
        params[0] = capacity;
        return new SimulationEvent(EventType.HouseStarting,
                null, null,
                fruitHouse,
                food, null, 0, params);
    }

    public static SimulationEvent machineCookingFood(FruitHouse fruitHouse,
                                                     Fruit food) {
        return new SimulationEvent(EventType.HouseStartingFruits,
                null, null,
                fruitHouse,
                food,
                null, 0, null);
    }

    public static SimulationEvent machineDoneFood(FruitHouse fruitHouse,
                                                  Fruit food) {
        return new SimulationEvent(EventType.HouseDoneFood,
                null, null,
                fruitHouse,
                food,
                null, 0, null);
    }

    public static SimulationEvent marketEnding(FruitHouse fruitHouse) {
        return new SimulationEvent(EventType.marketEnding,
                null, null,
                fruitHouse,
                null, null, 0, null);
    }

    public String toString() {
        switch (event) {
    /* Simulation events */
            case SimulationStarting:
                int numCustomers = simParams[0];
                int numCooks = simParams[1];
                int numTables = simParams[2];
                int capacity = simParams[3];
                return "Starting simulation: " + numCustomers + " customers; " +
                        numCooks + " farmer(s); " + numTables + " shelves; " +
                        "warehouse capacity " + capacity + ".";

            case SimulationEnded:
                return "Simulation ended.";

    	/* Customer events */
            case CustomerEnteredFruitMarket:
                return customer + " entered Fruit market .";

            case CustomerPlacedOrder:
                return customer + " placing order " + orderNumber + " " + orderFood;

            case CustomerReceivedOrder:
                return customer + " received order " + orderNumber + " " + orderFood;

            case CustomerLeavingFruitMarket:
                return customer + " leaving Fruit market.";

    	/* Farmer Events */
            case FarmerReceivedOrder:
                return farmer + " starting order " + orderNumber + " " + orderFood;

            case FarmerStartedProd:
                return farmer + " producing " + food + " for order " + orderNumber;

            case FarmerFinishedProd:
                return farmer + " finished " + food + " for order " + orderNumber;

            case FarmerCompletedOrder:
                return farmer + " completed order " + orderNumber;

            case FarmerEnding:
                return farmer + " going home for the night.";

    	/* Warehouse events */
            case HouseStarting:
                return fruitHouse + " starting up for producing " +
                        food + "; " + simParams[0] + ".";

            case HouseStartingFruits:
                return fruitHouse + " producing " + food + ".";

            case HouseDoneFood:
                return fruitHouse + " completed " + food + ".";

            case marketEnding:
                return fruitHouse + " shutting down.";

            default:
                throw new Error("Illegal event; can't be stringifies");
        }
    }
}