package ui;

import java.io.Serializable;

public class ThisProcessConnection implements ProcessConnection{

	private String pid;

	public ThisProcessConnection(String pid) {
		this.pid = pid;
	}

	@Override
	public void sendMessage(Serializable object) {
		System.out.println("PROCESS WAS SENT MESSAGE TO ITSELF:" + object.getClass().getName() + ". TODO: HANDLE THIS!");
		
	}
	
	public String toString(){
		return "pid '" + pid.substring(0, 3) + "' (self)";
	}

}
