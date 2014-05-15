package ui;

import java.util.Map;

public class RicartAgrawala {
	private Map<String, ProcessConnection> connections;
	private String pid;
	private ProcessConnection processConnection;

	public RicartAgrawala(String pid, Map<String, ProcessConnection> connections){
		this.connections = connections;
		this.pid = pid;
		this.processConnection = connections.get(pid);
		
	}
	
	public void issueRequest(String name){
		RequestMessage rm = new RequestMessage();
		rm.pid = pid;
		rm.resourceName = name;
		rm.vclock = processConnection.getVclock();
		rm.vclock.increment(pid);
		
		//TODO: save request somewhere, and we need a __copy__ of vclock!
		
		
		System.out.println("Sending request to all processes..");
		for (Map.Entry<String, ProcessConnection> entry : connections.entrySet()) {
			ProcessConnection pc = entry.getValue();
			pc.sendMessage(rm);
		}
		
		//TODO: if all ok, access resource!
	}
	
	public void receiveRequest(RequestMessage rm){
		//TODO: update local vector clock (merge + increment)!
		//TODO: what defines lower / higher timestamp in vclock??
		
		//if not accessing and not currently requesting access => send OK msg
		//if accessing, queue request and do nothing
		//if currently requesting, compare timestamp in my request with timestamp in inc request.
		//	received lower: send OK message
		// 	sent lower: queue request and send nothing.
	}
}
