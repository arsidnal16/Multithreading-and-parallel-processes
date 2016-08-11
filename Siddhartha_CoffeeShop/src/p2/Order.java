package p2;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Order {
	public final CountDownLatch cdl;
	public final LinkedList<Food> order;
	public final int orderNum;
	

	public Order(CountDownLatch cdl, LinkedList<Food> order, int orderNum) {
	
		this.cdl = cdl;
		this.order = order;
		this.orderNum = orderNum;
	}
}
