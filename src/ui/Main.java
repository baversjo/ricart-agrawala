package ui;

import java.util.Random;
import java.util.UUID;

public class Main {
	public static final String MULTICAST_ADDR = "239.255.255.242";
	public static final int MULTICAST_PORT = 6790;
	public static final String PROCESS_ID = UUID.randomUUID().toString();
	public static final int LISTEN_PORT  = 4600 + (new Random().nextInt(100));
	
	
	public static void main(String[] args) {
		
		
		int port = PROCESS_ID.charAt(0) + PROCESS_ID.charAt(1);
		
		BroadcastThread broadcastThread = new BroadcastThread();
		new Thread(broadcastThread).start();
		
		DiscoveryThread discoveryThread = new DiscoveryThread();
		new Thread(discoveryThread).start();
		
		
		
		
		System.out.println("Starting...");
		
		while(true){
			try{
				Thread.sleep(500);
			}catch(InterruptedException ex){
				broadcastThread.finish();
				discoveryThread.finish();
			}
		}
		
	}

}
