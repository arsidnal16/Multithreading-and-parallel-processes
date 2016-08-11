package p2;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;

import p2.Customer;
import p2.Food;
import p2.FoodType;
import p2.Simulation;
import p2.SimulationEvent;

/**
 * Cooks are simulation actors that have at least one field, a name.
 * When running, a cook attempts to retrieve outstanding orders placed
 * by Eaters and process them.
 */
public class Cook implements Runnable {
	private final String name;
	private final Machine fryer;
	private final Machine grill;
	private final Machine coffeeMaker2000;
	/**
	 * You can feel free modify this constructor.  It must
	 * take at least the name, but may take other parameters
	 ** if you would find adding them useful. 
	 *
	 * @param: the name of the cook
	 */
	public Cook(String name, Machine fryer, Machine grill, Machine coffeeMaker2000){
		this.name = name;
		this.fryer = fryer;
		this.grill = grill;
		this.coffeeMaker2000 = coffeeMaker2000;
	}

	public String toString() {
		return name;
	}

	/**
	 * This method executes as follows.  The cook tries to retrieve
	 * orders placed by Customers.  For each order, a List<Food>, the
	 * cook submits each Food item in the List to an appropriate
	 * Machine, by calling makeFood().  Once all machines have
	 * produced the desired Food, the order is complete, and the Customer
	 * is notified.  The cook can then go to process the next order.
	 * If during its execution the cook is interrupted (i.e., some
	 * other thread calls the interrupt() method on it, which could
	 * raise InterruptedException if the cook is blocking), then it
	 * terminates.
	 */
	public void run() {

		Simulation.logEvent(SimulationEvent.cookStarting(this));
		try {
			while(true) {
				Order myOrder = Simulation.orderQueue.take();
				Simulation.logEvent(SimulationEvent.cookReceivedOrder(this, myOrder.order, myOrder.orderNum));
				int orderSize = myOrder.order.size();
				CountDownLatch cdl = new CountDownLatch(orderSize);
				Iterator<Food> iterator = myOrder.order.iterator();
				while (iterator.hasNext()) {
					switch(iterator.next().cookTimeMS){
						case 500:
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.burger, myOrder.orderNum));
							grill.makeFood(cdl);
							Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.burger, myOrder.orderNum));
							break;
						case 350:
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.fries, myOrder.orderNum));
							fryer.makeFood(cdl);
							Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.fries, myOrder.orderNum));
							break;
						case 100:
							Simulation.logEvent(SimulationEvent.cookStartedFood(this, FoodType.coffee, myOrder.orderNum));
							coffeeMaker2000.makeFood(cdl);
							Simulation.logEvent(SimulationEvent.cookFinishedFood(this, FoodType.coffee, myOrder.orderNum));
							break;
					}
					
				}
				cdl.await();
				myOrder.cdl.countDown();
				Simulation.logEvent(SimulationEvent.cookCompletedOrder(this, myOrder.orderNum));
				}
		}
		catch(InterruptedException e) {
			// This code assumes the provided code in the Simulation class
			// that interrupts each cook thread when all customers are done.
			// You might need to change this if you change how things are
			// done in the Simulation class.
			Simulation.logEvent(SimulationEvent.cookEnding(this));
		}
	}
}