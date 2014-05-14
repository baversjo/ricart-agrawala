package ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ProcessConnection {
	
	private String pid;

	private boolean stop;
	private boolean active;
	
	InputStream in;
	OutputStream out;
	
	ObjectInputStream oin;
	ObjectOutputStream oout;
	private Socket socket;
	
	public ProcessConnection(String pid, Socket socket) {
		stop = false;
		this.pid = pid;
		this.socket = socket;
		
		try {
			socket.setSoTimeout(500);
			
			in = socket.getInputStream();
			oin = new ObjectInputStream(in);
			out = socket.getOutputStream();
			oout = new ObjectOutputStream(out);
			
			active = true;
		} catch (IOException e) {
			e.printStackTrace();
			active = false;
			return;
		}
		
		ListenThread lt = new ListenThread();
		new Thread(lt).start();
	}
	
	public void finish(){
		System.out.println("closing " + toString());
		stop = true;
		active = false;
		try {
			socket.close();
		} catch (IOException e) {}
	}
	
	public void sendMessage(Serializable object){
	}
	private void handleMessage(Object o){
		
	}
	
	private class ListenThread implements Runnable{

		@Override
		public void run() {
			while(!stop){
				try {
					Object o = oin.readObject();
					handleMessage(o);
					
				} catch (ClassNotFoundException | IOException e) {
					finish();
				}
				
			}
		}
	}
	
	public String toString(){
		return "pid '" + pid.substring(0, 3) + "'";
	}

}


