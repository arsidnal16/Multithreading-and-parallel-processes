package p2;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import p2.Food;
import p2.Simulation;
import p2.SimulationEvent;

//import java.util.concurrent;
import java.awt.EventQueue;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order.  When running, an
 * customer attempts to enter the coffee shop (only successful if the
 * coffee shop has a free table), place its order, and then leave the 
 * coffee shop when the order is complete.
 */
public class Customer implements Runnable {
	//JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final LinkedList<Food> order;
	private final int orderNum;    
	private static int runningCounter = 0;
	
	

	/**
	 * You can feel free modify this constructor.  It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	
	public Customer(String name, LinkedList<Food> order) {
		this.name = name;
		this.order = order;
		this.orderNum = runningCounter++;
		
		
	}
	

	public String toString() {
		return name;
	}

	/** 
	 * This method defines what a Customer does: The customer attempts to
	 * enter the coffee shop (only successful when the coffee shop has a
	 * free table), place its order, and then leave the coffee shop
	 * when the order is complete.
	 */
	public synchronized void run() {
		//YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
		
		try{
			Simulation.freeTable.acquire();
			CountDownLatch cdl = new CountDownLatch(1);
			Simulation.logEvent(SimulationEvent.customerEnteredCoffeeShop(this));
			Order myOrder = new Order(cdl, this.order,this.orderNum);
			Simulation.orderQueue.put(myOrder);
			Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, this.order, this.orderNum));
			cdl.await();
			Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, this.order, this.orderNum));
			Simulation.freeTable.release();
			System.out.println("Order Number " +orderNum +" is complete");
			}
		
		
		catch(InterruptedException e){
			Simulation.logEvent(SimulationEvent.customerLeavingCoffeeShop(this));
			
			}
		
	}

}