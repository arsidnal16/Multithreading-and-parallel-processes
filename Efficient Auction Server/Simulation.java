import java.util.List;


/**
 * Class provided for ease of test. This will not be used in the project 
 * evaluation, so feel free to modify it as you like.
 */ 
public class Simulation
{
    public static void main(String[] args)
    {                
        int nrSellers = 400;
        int nrBidders = 30;
        AuctionServer server = AuctionServer.getInstance();
        Thread[] sellerThreads = new Thread[nrSellers];
        Thread[] bidderThreads = new Thread[nrBidders];
        Seller[] sellers = new Seller[nrSellers];
        Bidder[] bidders = new Bidder[nrBidders];
        
        // Start the sellers
        for (int i=0; i<nrSellers; ++i)
        {
            sellers[i] = new Seller(
            		server, 
            		"Seller"+i, 
            		100, 50, i
            );
            sellerThreads[i] = new Thread(sellers[i]);
            sellerThreads[i].start();
        }
        
        // Start the buyers
        for (int i=0; i<nrBidders; ++i)
        {
            bidders[i] = new Bidder(
            		server, 
            		"Buyer"+i, 
            		100, 20, 150, i
            );
            bidderThreads[i] = new Thread(bidders[i]);
            bidderThreads[i].start();
        }
        
        // Join on the sellers
        for (int i=0; i<nrSellers; ++i)
        {
            try
            {
                sellerThreads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        // Join on the bidders
        for (int i=0; i<nrBidders; ++i)
        {
            try
            {
                bidderThreads[i].join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        
        
      /*  
        for(int i =0;i<100;++i){
        	long period = System.currentTimeMillis();
        	server.reset();
        	
        	
        }*/
        
     /*   for(Seller s : sellers){
        	int bid = 23;
        	for(int i = 0; i < 2; i++){
        		if( i % 2 == 0){ bid = bid + 2;} else { bid = bid + 3; }
        		server.submitItem(s.name(), s.name() + "#" + i, 10, 100000);
        	}
        	
        }
        
        for(Bidder b : bidders){
        	int bid = 23;
        	
        	List<Item> bid_items = server.getItems();
        	System.out.println(bid_items.size());
        	for(int i = 0; i < 11; i++){
        		if( i % 2 == 0){ bid = bid + 2;} else { bid = bid + 3; }
        		if(server.checkBidStatus(b.name(), bid_items.get(i).listingID()) == 2){
        			server.submitBid(b.name(), bid_items.get(i).listingID(), bid);
        		} else if(server.checkBidStatus(b.name(), bid_items.get(i).listingID()) != 2){
        			return;
        		}
        	}
        } */
        
        System.out.println("Total sold items" + server.soldItemsCount());
        System.out.println("Total revenue" + server.revenue());
        System.out.println("Total participated bidders: " +  server.totalParticiptedBidders()); 
        System.out.println("Total participated sellers: " +  server.totalSellers());
        for (Bidder b : bidders){
        	int amount = server.bidderMoneySpent(b.name());
        	
        System.out.println("Money spent by " + b.name() + ":" + amount);
        }
        System.out.println("Total submitted items: " +  server.totalSubmittedItems()); 
        
        server.highestBidderBids();
        
        int TotalMoneySpentByBidders = 0;
        for(Bidder b : bidders){
        	
        
        TotalMoneySpentByBidders = TotalMoneySpentByBidders + b.cashSpent();
        
        
        }
        
        System.out.println("Total money spent by Bidders " + TotalMoneySpentByBidders );
        
        if (TotalMoneySpentByBidders == server.revenue()){
        	System.out.println("Revenue is equal to Total Money Spent, Auction Successful");
        }
        
        else{
        	System.out.println("Fault in auction server");
        }
        
        
        
        // TODO: Add code as needed to debug
        
    }
}
