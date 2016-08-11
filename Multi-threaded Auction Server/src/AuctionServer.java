

/*  @author Siddhartha Singh
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class AuctionServer
{
	/**
	 * Singleton: the following code makes the server a Singleton. You should
	 * not edit the code in the following noted section.
	 * 
	 * For test purposes, we made the constructor protected. 
	 */

	/* Singleton: Begin code that you SHOULD NOT CHANGE! */
	protected AuctionServer()
	{
	}

	private static AuctionServer instance = new AuctionServer();

	public static AuctionServer getInstance()
	{
		return instance;
	}

	/* Singleton: End code that you SHOULD NOT CHANGE! */





	/* Statistic variables and server constants: Begin code you should likely leave alone. */


	/**
	 * Server statistic variables and access methods:
	 */
	private int soldItemsCount = 0;
	private int revenue = 0;

	public int soldItemsCount()
	{
		int count = 0;
		for(int i : itemsAndIDs.keySet()){
			if(itemsAndIDs.get(i).biddingOpen() == false && itemUnbid(i) == false ){
				System.out.println("The number of Items sold in the autcion " +highestBids.size() +"\n Total number of Items in the auction " + itemsAndIDs.size());
			count++;
			System.out.println("Items up for bidding are" +itemsUpForBidding.size());
			}
		}
		
		return count;
	}

	public int revenue()
	{
		int revenue = 0;
		for(int i : itemsAndIDs.keySet()){
			if(itemsAndIDs.get(i).biddingOpen() == false && highestBids.containsKey(i)){
				revenue = revenue + highestBids.get(i);
			}
		}
		return revenue;
	}
	
	public int bidderMoneySpent(String name){
		int amount = 0;
		for(int i : highestBidders.keySet()){
			if(highestBidders.get(i).equalsIgnoreCase(name)){
				amount = amount + highestBids.get(i);
				
			}
		}
		return amount;
	}
	
	
	public int totalSubmittedItems() {
        return lastListingID + 1; 
   }

	
	public int totalParticiptedBidders() {
        return itemsPerBuyer.size();
    }
	
	public int totalSellers(){
		return itemsPerSeller.size();
	}
	 public void highestBidderBids(){
		 for(int i : highestBids.keySet()){
			 System.out.println("item " + i + "was bid at $" + highestBids.get(i) );
			 
		 }
	 }
	
	



	/**
	 * Server restriction constants:
	 */
	public static final int maxBidCount = 100; // The maximum number of bids at any given time for a buyer.
	public static final int maxSellerItems = 200; // The maximum number of items that a seller can submit at any given time.
	public static final int serverCapacity = 80; // The maximum number of active items at a given time.


	/* Statistic variables and server constants: End code you should likely leave alone. */



	/**
	 * Some variables we think will be of potential use as you implement the server...
	 */

	// List of items currently up for bidding (will eventually remove things that have expired).
	private  List<Item> itemsUpForBidding = new ArrayList<Item>();




	// The last value used as a listing ID.  We'll assume the first thing added gets a listing ID of 0.
	private int lastListingID = -1; 
	Object Lock = new Object();
	Object Lock1 = new Object();
	Object Lock2 = new Object();
	Object Lock3 = new Object();
	Object Lock4 = new Object();
	Object Lock5 = new Object();
	
	// List of item IDs and actual items.  This is a running list with everything ever added to the auction.
	private HashMap<Integer, Item> itemsAndIDs = new HashMap<Integer, Item>();

	// List of itemIDs and the highest bid for each item.  This is a running list with everything ever added to the auction.
	private HashMap<Integer, Integer> highestBids = new HashMap<Integer, Integer>();

	// List of itemIDs and the person who made the highest bid for each item.   This is a running list with everything ever bid upon.
	private HashMap<Integer, String> highestBidders = new HashMap<Integer, String>(); 




	// List of sellers and how many items they have currently up for bidding.
	private HashMap<String, Integer> itemsPerSeller = new HashMap<String, Integer>();

	// List of buyers and how many items on which they are currently bidding.
	private HashMap<String, Integer> itemsPerBuyer = new HashMap<String, Integer>();



	// Object used for instance synchronization if you need to do it at some point 
	// since as a good practice we don't use synchronized (this) if we are doing internal
	// synchronization.
	//
	// private Object instanceLock = new Object(); 









	/*
	 *  The code from this point forward can and should be changed to correctly and safely 
	 *  implement the methods as needed to create a working multi-threaded server for the 
	 *  system.  If you need to add Object instances here to use for locking, place a comment
	 *  with them saying what they represent.  Note that if they just represent one structure
	 *  then you should probably be using that structure's intrinsic lock.
	 */



	/**
	 * Attempt to submit an <code>Item</code> to the auction
	 * @param sellerName Name of the <code>Seller</code>
	 * @param itemName Name of the <code>Item</code>
	 * @param lowestBiddingPrice Opening price
	 * @param biddingDurationMs Bidding duration in milliseconds
	 * @return A positive, unique listing ID if the <code>Item</code> listed successfully, otherwise -1
	 */
	public int submitItem(String sellerName, String itemName, int lowestBiddingPrice, int biddingDurationMs)
	{
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   Make sure there's room in the auction site. //
		
			
		
		if (itemsUpForBidding.size() < serverCapacity ){
			if(itemsPerSeller.containsKey(sellerName)){
				if(itemsPerSeller.get(sellerName) == maxSellerItems || itemsPerSeller == null){
					return -1;
				} else {

					synchronized (Lock) {
						lastListingID = lastListingID + 1;
					}
					Item i = new Item(sellerName, itemName, lastListingID, lowestBiddingPrice, biddingDurationMs);
					int count = itemsPerSeller.get(sellerName) + 1;
					synchronized (Lock1) {
					
					itemsUpForBidding.add(i);
					}
					synchronized (Lock2) {
						
					
					itemsAndIDs.put(lastListingID, i);
					}
					synchronized (Lock3) {
						
					
					itemsPerSeller.put(sellerName, count);
					}
					return lastListingID;
					
					}
				

			} else{
				
				
					
				

				synchronized (Lock) {
					lastListingID = lastListingID + 1;
				}
				Item i = new Item(sellerName, itemName, lastListingID, lowestBiddingPrice, biddingDurationMs);
				
				synchronized (Lock1) {
					
				
				itemsUpForBidding.add(i);
				}
				synchronized (Lock2) {
					
				
				itemsAndIDs.put(lastListingID, i);
				}
				synchronized (Lock3) {
					
				
				itemsPerSeller.put(sellerName, 1);
				}
				return lastListingID;
				
			}
		}
		return -1;

		//   If the seller is a new one, add them to the list of sellers. // if !seller = Arraylist <sellers> , add seller to arraylist of sellers 
		//   If the seller has too many items up for bidding, don't let them add this one. // if seller items > maxSellerItems, deny 
		//   Don't forget to increment the number of things the seller has currently listed.// increment seller items

		}
	



	/**
	 * Get all <code>Items</code> active in the auction
	 * @return A copy of the <code>List</code> of <code>Items</code>
	 */
	public synchronized List<Item> getItems()
	{
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//    Don't forget that whatever you return is now outside of your control.
		//  
		List<Item> getItems = new ArrayList<Item>();
		synchronized (Lock1) {
		for(Item i : itemsUpForBidding){
			getItems.add(i);}
		return getItems;
		}
	}


	/**
	 * Attempt to submit a bid for an <code>Item</code>
	 * @param bidderName Name of the <code>Bidder</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @param biddingAmount Total amount to bid
	 * @return True if successfully bid, false otherwise
	 */
	public synchronized boolean submitBid(String bidderName, int listingID, int biddingAmount)
	{
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   See if the item exists. // check if item exists in itemsForBidding
		


		for (Item i : getItems()){
			if(i.listingID() == listingID && i.biddingOpen() == true){
				if(itemsPerBuyer.containsKey(bidderName)){
					if(itemsPerBuyer.get(bidderName) < maxBidCount){
						if(highestBids.containsKey(listingID)){
							int highestBid = highestBids.get(listingID);

							if(highestBidders.containsKey(listingID) && 
									highestBidders.get(listingID).equalsIgnoreCase(bidderName)){
								return false;
							} else {

								if(highestBids.get(listingID) < biddingAmount ){
									highestBids.put(listingID, biddingAmount);
									String prevHighBidder = highestBidders.get(listingID);
									int bid_count = itemsPerBuyer.get(prevHighBidder);
									highestBidders.put(listingID, bidderName);
									itemsPerBuyer.put(prevHighBidder, bid_count - 1);
									int win_bid_count = itemsPerBuyer.get(bidderName);
									itemsPerBuyer.put(bidderName, win_bid_count + 1);
								//  itemsAndIDs.put(listingID, i);     This was adding duplicate values to itemsAndIds
									return true;

								} else {
									return false;
								}

							}
						} else {
							highestBids.put(listingID, biddingAmount);
							highestBidders.put(listingID, bidderName);
							int win_bid_count = itemsPerBuyer.get(bidderName);
							itemsPerBuyer.put(bidderName, win_bid_count + 1);
							return true;
						}
					} else {
						return false;
					}
				} else { // implement to add new bidder to array itemsPerBuyer
					if(highestBids.containsKey(listingID)){
						int highestBid = highestBids.get(listingID);
						if(highestBids.get(listingID) < biddingAmount ){
							highestBids.put(listingID, biddingAmount);
							String prevHighBidder = highestBidders.get(listingID);
							int bid_count = itemsPerBuyer.get(prevHighBidder);
							highestBidders.put(listingID, bidderName);
							itemsPerBuyer.put(prevHighBidder, bid_count - 1);
						//	int win_bid_count = itemsPerBuyer.get(bidderName);
							itemsPerBuyer.put(bidderName, 1);
						//	itemsAndIDs.put(listingID, i);       This was adding duplicate values to itemsAndIds
							return true;

						} else {
							return false;
						}

					} 
					else {
						highestBids.put(listingID, biddingAmount);
						highestBidders.put(listingID, bidderName);
					//	int win_bid_count = itemsPerBuyer.get(bidderName);
						itemsPerBuyer.put(bidderName, 1);
						return true;
					}
				}
			} 
			else if(i.listingID() == listingID && i.biddingOpen() == false){
				highestBids.remove(i);
				
			}
		}


		//   See if it can be bid upon. // check if the bidding is active or not.
		//   See if this bidder has too many items in their bidding list. // if bids are less than bid capacity
		//   Get current bidding info. // synchronized and get current bid and bid duration
		//   See if they already hold the highest bid. // if highest bid belongs to bidder then deny bid
		//   See if the new bid isn't better than the existing/opening bid floor. // if new bid < existing/opening bid then deny
		//   Decrement the former winning bidder's count //
		//   Put your bid in place // 

		return false;
	}

	/**
	 * Check the status of a <code>Bidder</code>'s bid on an <code>Item</code>
	 * @param bidderName Name of <code>Bidder</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return 1 (success) if bid is over and this <code>Bidder</code> has won<br>
	 * 2 (open) if this <code>Item</code> is still up for auction<br>
	 * 3 (failed) If this <code>Bidder</code> did not win or the <code>Item</code> does not exist
	 */
	public synchronized int checkBidStatus(String bidderName, int listingID)
	{
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		//   If the bidding is closed, clean up for that item. // if bidduration returns false and there has been a bid, give it to buyer else emove it from the items list.
		//     Remove item from the list of things up for bidding. // 
		//     Decrease the count of items being bid on by the winning bidder if there was any...//
		//     Update the number of open bids for this seller//
		if(itemsAndIDs.containsKey(listingID)){
			if(itemsAndIDs.get(listingID).biddingOpen() == false){
				if(highestBidders.get(listingID).equalsIgnoreCase(bidderName)){
					itemsUpForBidding.remove(itemsAndIDs.get(listingID));
					int count = itemsPerBuyer.get(bidderName) - 1;
					itemsPerBuyer.put(bidderName, count);
					String seller = itemsAndIDs.get(listingID).seller();
					count = itemsPerSeller.get(seller) - 1;
					itemsPerSeller.put(seller, count);
					return 1;
					
					}
				else {
					return 3;
				}
			} else {
				return 2;
			}

		}

		return 3;
	}

	/**
	 * Check the current bid for an <code>Item</code>
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return The highest bid so far or the opening price if no bid has been made,
	 * -1 if no <code>Item</code> exists
	 */


	public int itemPrice(int listingID)
	{
		// TODO: IMPLEMENT CODE HERE
		// item price  = highest price
		// if no bid has been made then itemPrice = opening price
		// return item price


		
		if(highestBids.containsKey(listingID)){
			return highestBids.get(listingID);
		} else {

	

			for(Item i : getItems()){

				if(i.listingID() == listingID){
					return i.lowestBiddingPrice();
				}
				

			}
		}



		return -1;

	}
	/**
	 * Check whether an <code>Item</code> has been bid upon yet
	 * @param listingID Unique ID of the <code>Item</code>
	 * @return True if there is no bid or the <code>Item</code> does not exist, false otherwise
	 */
	public synchronized Boolean itemUnbid(int listingID)
	{
		// TODO: IMPLEMENT CODÏE HERE
		// 
		if(!(highestBids.containsKey(listingID))){
			return true;
		}
		return false;
	}


}


