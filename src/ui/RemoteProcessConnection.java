package ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;

public class RemoteProcessConnection implements ProcessConnection{
	
	private String pid;

	private boolean stop;
	private boolean active;
	
	InputStream in;
	OutputStream out;
	
	private Socket socket;

	private Map<String, ProcessConnection> connections;
	
	public RemoteProcessConnection(String pid, Socket socket, Map<String,ProcessConnection> connections) {
		stop = false;
		this.pid = pid;
		this.socket = socket;
		this.connections = connections;
		
		try {
			socket.setSoTimeout(500);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
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
		System.out.println("closing " + toString() + ". TODO: notify connection list!");
		stop = true;
		active = false;
		try {
			connections.remove(pid);
			socket.close();
		} catch (IOException e) {}
	}
	
	public void sendMessage(Serializable object){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(object);
			System.out.println("Sent " + object.getClass().getName());
		} catch (IOException e) {
			finish();
			e.printStackTrace();
		}
	}
	private void handleMessage(Object o){
		System.out.println("Received " + o.getClass().getName());
	}
	
	private class ListenThread implements Runnable{

		@Override
		public void run() {
			while(!stop){
				try {
					ObjectInputStream ois = new ObjectInputStream(in);
					Object o = ois.readObject();
					//ois.close();
					handleMessage(o);
					
				} catch(SocketTimeoutException e){
					continue;
				}catch (ClassNotFoundException | IOException e) {
					finish();
				}
				
			}
		}
	}
	
	public String toString(){
		return "pid '" + pid.substring(0, 3) + "'";
	}

}


