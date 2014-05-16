package ui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RicartAgrawala {
	private Map<String, ProcessConnection> connections;
	private String pid;
	private ProcessConnection processConnection;
	
	private boolean accessing;
	
	private RequestMessage currentRequest;

	public RicartAgrawala(String pid, Map<String, ProcessConnection> connections){
		this.connections = connections;
		this.pid = pid;
		this.processConnection = connections.get(pid);
		
		accessing = false;
		
		currentRequest = null;
		
	}
	
	public void issueRequest(String name){
		RequestMessage rm = new RequestMessage();
		rm.pid = pid;
		rm.resourceName = name;
		rm.vclock = processConnection.getVclock();
		rm.vclock.increment(pid);
		
		currentRequest = rm;
		currentRequest.vclock = rm.vclock.copy();
		
		
		System.out.println("Sending request to all processes..");
		
		final ConcurrentHashMap<String,Integer> waitingFor = new ConcurrentHashMap<String,Integer>();
		
		for (Map.Entry<String, ProcessConnection> entry : connections.entrySet()) {
			ProcessConnection pc = entry.getValue();
			pc.sendMessage(rm, new ResponseEvent() {
				
				@Override
				public void notify(Message message) {
					OKMessage okm = (OKMessage) message;
					
					waitingFor.remove(okm.pid);
					
					if(waitingFor.size() == 0){
						accessing = true;
						FakeResource.access();
						accessing = false;
						currentRequest = null;
						//TODO: check queue
					}
				}
			});
		}
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
