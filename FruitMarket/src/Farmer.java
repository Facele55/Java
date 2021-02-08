import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Farmers are simulation actors that have at least one field, a name.

 */
public class Farmer implements Runnable {

	private final String farmerName;
	public List<Fruit> fruitReady = new ArrayList<Fruit>();
	private Customer customer;

	public Farmer(String farmerName) {
		this.farmerName = farmerName;
	}

	public String toString() {
		return farmerName;
	}


	public void run() {
		try {
			while(true) {
				synchronized(Simulation.orderList){
					while(true){
						if(Simulation.orderList.size() == 0)
							Simulation.orderList.wait();
							else
								break;
					}
					customer = Simulation.orderList.remove(0);
					Simulation.orderList.notifyAll();
				}
				
				for(int i = 0; i < customer.getOrder().size(); i++){
					Fruit orderedFruit = customer.getOrder().get(i);
					String test = orderedFruit.name;
					if(test.equals("apple")){
						synchronized(Simulation.garden.fruitsList){
							Simulation.garden.makeFood(this, customer.getOrderNum());
							Simulation.logEvent(SimulationEvent.FarmerStartedProd(this, FruitType.apple , customer.getOrderNum()));
							Simulation.garden.fruitsList.notifyAll();
						}
					} else if(test.equals("grape")){
						synchronized(Simulation.deliver.fruitsList){
							Simulation.deliver.makeFood(this,customer.getOrderNum());
							Simulation.logEvent(SimulationEvent.FarmerStartedProd(this, FruitType.grape , customer.getOrderNum()));
							Simulation.deliver.fruitsList.notifyAll();
						}
					} else if(test.equals("watermelon")){
						synchronized(Simulation.field.fruitsList){
							Simulation.field.makeFood(this,customer.getOrderNum());
							Simulation.logEvent(SimulationEvent.FarmerStartedProd(this, FruitType.watermelon , customer.getOrderNum()));
							Simulation.field.fruitsList.notifyAll();
						}
					}
				}
				
				synchronized(fruitReady){
					synchronized(Simulation.completedOrder){
						while(!(fruitReady.size() == customer.getOrder().size())){
							fruitReady.wait();
							fruitReady.notifyAll();
						}
						customer.setOrderStatus(true);
						Simulation.completedOrder.add(customer);
						Simulation.logEvent(SimulationEvent.farmerCompletedOrder(this, customer.getOrderNum()));
						Simulation.completedOrder.notifyAll();
					}
				}
				fruitReady = new LinkedList<Fruit>();
			}
		}
		catch(InterruptedException e) {
			Simulation.logEvent(SimulationEvent.FarmerEnding(this));
		}
	}
}