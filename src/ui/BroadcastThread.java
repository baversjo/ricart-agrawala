package ui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class BroadcastThread implements Runnable{
	private boolean stop;
	@Override
	public void run() {
		stop = false;
		String msg = "pid:" + Main.PROCESS_ID + ";port:" + Main.LISTEN_PORT;
		InetAddress group = null;
		DatagramSocket s = null;
		
		try {
			group = InetAddress.getByName(Main.MULTICAST_ADDR);
			s = new DatagramSocket();
			
			s.setSoTimeout(500);
			
			DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),group, Main.MULTICAST_PORT);
			
			
			while(!stop){
				s.send(packet);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}

			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		s.close();
		
	}
	
	public void finish(){
		stop = true;
	}

}
