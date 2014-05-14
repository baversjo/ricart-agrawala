package ui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashSet;

public class DiscoveryThread implements Runnable{
	private boolean stop;
	private HashSet<String> connected_pids;
	
	public DiscoveryThread(){
		stop = false;
		connected_pids = new HashSet<String>();
		connected_pids.add(Main.PROCESS_ID);
	}
	@Override
	public void run() {
		
		InetAddress group = null;
		MulticastSocket s = null;
		
		try {
			group = InetAddress.getByName(Main.MULTICAST_ADDR);
			s = new MulticastSocket(Main.MULTICAST_PORT);
			
			s.joinGroup(group);
			s.setSoTimeout(500);
			
			byte[] buf = new byte[1000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			String pid;
			int port;
			String msg;
			String[] parts;
			
			while(!stop){
				try{
					s.receive(recv);
					
					//packet format:
					//pid:xxxxxxxxxxxxxxxxxxx
					//if an active connection doesn't already exist, create it
					msg = new String(buf, 0, recv.getLength(), "UTF-8");
					if(msg.startsWith("pid:")){
						parts = msg.split(";");
						if(parts.length == 2){
							try{
								pid = parts[0].substring(4);
							
								port = Integer.parseInt(parts[1].substring(5));
								
								if(!connected_pids.contains(pid)){
									connected_pids.add(pid);
									connect(pid, recv.getAddress(), port);
								}
								recv.setLength(buf.length);
								continue;
							}
							catch(NumberFormatException ex){}
							catch(IndexOutOfBoundsException ex){}
							
							

						}
					}
					
					System.out.println("unknown discovery message:");
					System.out.println(msg);
					System.out.println();

					
				}catch(SocketTimeoutException ex){
					continue;
				}
				
			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			s.leaveGroup(group);
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void finish(){
		stop = true;
	}
	
	private void connect(String pid, InetAddress ip, int port){
		System.out.println("Starting TCP connection with " + pid + " at" + ip.toString() + ":" + port);
		
	}

}
