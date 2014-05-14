package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class Main {
	public static final String MULTICAST_ADDR = "239.255.255.242";
	public static final int MULTICAST_PORT = 6790;
	public static final String PROCESS_ID = UUID.randomUUID().toString();
	public static final int LISTEN_PORT  = 4600 + (new Random().nextInt(100));
	public static final Object connection_lock = new Object();
	
	
	public static void main(String[] args) {
		
		BroadcastThread broadcastThread = new BroadcastThread();
		new Thread(broadcastThread).start();
		
		DiscoveryThread discoveryThread = new DiscoveryThread();
		new Thread(discoveryThread).start();
		
		
		
		
		System.out.println("Started. Press the a key to request access to the fake resource.");
		System.out.println("Press the e key to exit");

		Scanner sc = new Scanner(System.in);
		while(true){
				String c = sc.next().trim();
				if(c.equals("a")){
					//TODO: request access here!
					System.out.println("requesting access.");
				}else if(c.equals("e")){
					System.out.println("exiting..");
					sc.close();
					broadcastThread.finish();
					discoveryThread.finish();
					return;
				}else{
					System.out.println("unrecognized command.");
				}
		}
		
	}

}
