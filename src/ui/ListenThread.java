package ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;


public class ListenThread implements Runnable{
	private boolean stop;
	private int port;
	private ServerSocket socket;
	
	Map<String, ProcessConnection> connections;
	private RicartAgrawala ra;
	
	public ListenThread(int port, Map<String, ProcessConnection> connections, RicartAgrawala ra){
		stop = false;
		this.port = port;
		this.connections = connections;
		this.ra = ra;
	}
	@Override
	public void run() {
		try {
			socket = new ServerSocket(port);
			System.out.println("listening on: " + port);
			socket.setSoTimeout(500);
			
			while(!stop){
				Socket newClient = null;
				try{
					newClient = socket.accept();
					System.out.println("Accepted connection!");
					InputStream in = newClient.getInputStream();
					ObjectInputStream oin = new ObjectInputStream(in);
					
					Object o = oin.readObject();
					
					if(o instanceof HelloMessage){
						HelloMessage msg = (HelloMessage)o;
						String pid = msg.pid;
						synchronized(Main.connection_lock){
							if(connections.containsKey(pid)){
								newClient.close();
								continue;
							}
							InetAddress ip = newClient.getInetAddress();
							int port = newClient.getPort();
							System.out.println("Got TCP connection with " + pid + " at" + ip.toString() + ":" + port);
							
							ProcessConnection process = new RemoteProcessConnection(pid, newClient, connections, ra);
							connections.put(pid, process);
							System.out.println("connected");
							
						}
						
					}else{
						newClient.close();
						continue;
					}
				}
				catch(SocketTimeoutException ex){}
				catch (IOException | ClassNotFoundException e){
					if(newClient != null){
						newClient.close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (IOException e) {}
		
	}
	
	public void finish(){
		System.out.println("stopping tcp server");
		stop = true;
		
	}
	

}
