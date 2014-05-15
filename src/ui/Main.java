package ui;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static final String MULTICAST_ADDR = "239.255.255.242";
	public static final int MULTICAST_PORT = 6790;
	public static final String PROCESS_ID = UUID.randomUUID().toString();
	public static final int LISTEN_PORT  = 4600 + (new Random().nextInt(100));
	public static final Object connection_lock = new Object();
	
	
	public static void main(String[] args) {
		
		BroadcastThread broadcastThread = new BroadcastThread();
		new Thread(broadcastThread).start();
		
		Map<String, ProcessConnection> connections = new ConcurrentHashMap<String,ProcessConnection>();
		
		ThisProcessConnection thisProcess = new ThisProcessConnection(PROCESS_ID);
		
		connections.put(Main.PROCESS_ID, thisProcess);
		
		DiscoveryThread discoveryThread = new DiscoveryThread(connections);
		new Thread(discoveryThread).start();
		
		ListenThread listenThread = new ListenThread(LISTEN_PORT, connections);
		new Thread(listenThread).start();
		
		RicartAgrawala ra = new RicartAgrawala(Main.PROCESS_ID, connections);
		
		System.out.println("my pid:" + PROCESS_ID);
		
		
		System.out.println("Started. Press the a key to request access to the fake resource.");
		System.out.println("Press the c key to list active clients");
		System.out.println("Press the e key to exit");

		Scanner sc = new Scanner(System.in);
		while(true){
				String c = sc.next().trim();
				if(c.equals("a")){
					//TODO: request access here!
					System.out.println("requesting access to 'exampleresource'.");
					ra.issueRequest("exampleresource");
				}else if(c.equals("e")){
					System.out.println("exiting..");
					sc.close();
					broadcastThread.finish();
					discoveryThread.finish();
					listenThread.finish();
					return;
				}else if(c.equals("c")){
					System.out.println("Active clients:");
					for (Map.Entry<String, ProcessConnection> entry : connections.entrySet()) {
						System.out.println(entry.getValue());
					}
				}else{
					System.out.println("unrecognized command.");
				}
		}
		
	}

}
