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
	private Map<Integer, ResponseEvent> awaitingResponse;
	
	private int requestCounter;

	
	public RemoteProcessConnection(String pid, Socket socket, Map<String,ProcessConnection> connections) {
		stop = false;
		this.pid = pid;
		this.socket = socket;
		this.connections = connections;
		requestCounter = 0;
		
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
	
	public void sendMessage(Message message){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(message);
			System.out.println("Sent " + message.getClass().getName());
		} catch (IOException e) {
			finish();
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message, ResponseEvent e){
		requestCounter++;
		message.setRequestId(requestCounter);
		awaitingResponse.put(requestCounter, e);
		sendMessage(message);
	}
	private void handleMessage(Message o){
		Integer rid = o.getRequestId();
		ResponseEvent e = awaitingResponse.get(rid);
		System.out.println("Received " + o.getClass().getName());
		if(e != null){
			e.notify(o);
		}
	}
	
	private class ListenThread implements Runnable{

		@Override
		public void run() {
			while(!stop){
				try {
					ObjectInputStream ois = new ObjectInputStream(in);
					Message o = (Message) ois.readObject();
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

	@Override
	public VectorClock getVclock() {
		throw new UnsupportedOperationException();
	}

}


