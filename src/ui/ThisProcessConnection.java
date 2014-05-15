package ui;

import java.io.Serializable;

public class ThisProcessConnection implements ProcessConnection{

	private String pid;
	
	public VectorClock vclock;

	public ThisProcessConnection(String pid) {
		this.pid = pid;
		
		VectorClock vclock = new VectorClock();
		vclock.initialize(pid);
	}

	@Override
	public void sendMessage(Serializable object) {
		System.out.println("PROCESS WAS SENT MESSAGE TO ITSELF:" + object.getClass().getName() + ". TODO: HANDLE THIS!");
		
	}
	
	@Override
	public VectorClock getVclock() {
		return vclock;
	}
	
	public String toString(){
		return "pid '" + pid.substring(0, 3) + "' (self)";
	}

}
