import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Simulation is the main class used to run the simulation.  You may
 * add any fields (static or instance) or any methods you wish.
 */
public class Simulation {

    public static List<SimulationEvent> events;
    public static Map<String, Integer> customersWithOrders = new HashMap<String, Integer>();
    public static List<Customer> currentCapacity = new ArrayList<Customer>();
    public static List<Customer> completedOrder = new ArrayList<Customer>();
    public static List<Customer> orderList = new ArrayList<Customer>();

    public static int warehouseCapacity = 4;


    //create machines
    public static FruitHouse field;
    public static FruitHouse garden;
    public static FruitHouse deliver;


    /**
     * Used by other classes in the simulation to log events
     *
     */
    public static void logEvent(SimulationEvent event) {
        events.add(event);
        System.out.println(event);
    }


    /**
     * Function responsible for performing the simulation. Returns a List of
     * SimulationEvent objects, constructed any way you see fit. This List will
     * be validated by a call to Validate.validateSimulation. This method is
     * called from Simulation.main().
     */
    public static List<SimulationEvent> runSimulation(
            int numCustomers, int numFarmers,
            int numShelves,
            int warehouseCapacity,
            boolean randomOrders) {

        events = Collections.synchronizedList(new ArrayList<SimulationEvent>());


        // Start the simulation
        logEvent(SimulationEvent.startSimulation(numCustomers,
                numFarmers,
                numShelves,
                warehouseCapacity));


        // Set things up you might need


        // Start up
        field = new FruitHouse("Field ", new Fruit("watermelon", 100), warehouseCapacity);

        garden = new FruitHouse("Garden", new Fruit("apple", 500), warehouseCapacity);

        deliver = new FruitHouse("Warehouse", new Fruit("grape", 350), warehouseCapacity);


        // Let get ready
        Thread[] cooks = new Thread[numFarmers];
        for (int i = 0; i < numFarmers; i++) {
            cooks[i] = new Thread(
                    new Farmer("Farmer " + i));
        }
        for (int i = 0; i < numFarmers; i++) {
            cooks[i].start();
        }


        // Build the customers.
        Thread[] customers = new Thread[numCustomers];
        LinkedList<Fruit> order;


        //if random order is not set them fixed order
        if (!randomOrders) {
            order = new LinkedList<Fruit>();

            order.add(FruitType.apple);
            order.add(FruitType.grape);
            order.add(FruitType.grape);
            order.add(FruitType.watermelon);

            for (int i = 0; i < customers.length; i++) {
                customers[i] = new Thread(
                        new Customer("Customer " + i, order)
                );
            }
        }


        //else random order
        else {
            for (int i = 0; i < customers.length; i++) {
                Random rnd = new Random(27);
                int applesCount = rnd.nextInt(3);
                int grapesCount = rnd.nextInt(3);
                int wmelonsCount = rnd.nextInt(3);
                order = new LinkedList<Fruit>();
                for (int b = 0; b < applesCount; b++) {
                    order.add(FruitType.apple);
                }
                for (int f = 0; f < grapesCount; f++) {
                    order.add(FruitType.grape);
                }
                for (int c = 0; c < wmelonsCount; c++) {
                    order.add(FruitType.watermelon);
                }
                customers[i] = new Thread(
                        new Customer("Customer " + (i), order)
                );
            }
        }


        // Now "let the customers know the shop is open" by
        //    starting them running in their own thread.
        for (int i = 0; i < customers.length; i++) {
            customers[i].start();

        }


        try {
            // Wait for customers to finish
            //   -- you need to add some code here...

            //waits for the customer threads to end
            for (int i = 0; i < customers.length; i++) {
                customers[i].join();
            }

            // The easiest way to do this might be the following, where
            // we interrupt their threads.  There are other approaches
            // though, so you can change this if you want to.
            for (int i = 0; i < cooks.length; i++)
                cooks[i].interrupt();
            for (int i = 0; i < cooks.length; i++)
                cooks[i].join();

        } catch (InterruptedException e) {
            System.out.println("Simulation thread interrupted.");
        }


        // Shut down
        logEvent(SimulationEvent.marketEnding(garden));
        logEvent(SimulationEvent.marketEnding(deliver));
        logEvent(SimulationEvent.marketEnding(field));


        // Done with simulation
        logEvent(SimulationEvent.endSimulation());

        return events;
    }


    public static void main(String args[]) throws InterruptedException {
        int numCustomers = 5;
        int numFarmers = 1;
        int numShelves = 5;
        boolean randomOrders = true;

        // Run the simulation and then
        //   feed the result into the method to validate simulation.
        System.out.println("Did it finished work?\n " + Validate.validateSimulation(
                        runSimulation(
                                numCustomers, numFarmers,
                                numShelves, warehouseCapacity,
                                randomOrders
                        )
                )
        );
    }
}