package ui;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RicartAgrawala {
	private Map<String, ProcessConnection> connections;
	private String pid;
	private ProcessConnection processConnection;
	
	private volatile boolean accessing;
	
	private volatile RequestMessage currentRequest;
	
	private Queue<ProcessConnection> okQueue;

	public RicartAgrawala(String pid, Map<String, ProcessConnection> connections){
		this.connections = connections;
		this.pid = pid;
		this.processConnection = connections.get(pid);

		
		accessing = false;
		
		currentRequest = null;
		
		okQueue = new ConcurrentLinkedQueue<ProcessConnection>();
		
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
			waitingFor.put(pc.getPid(), 1);
			pc.sendMessage(rm, new ResponseEvent() {
				
				@Override
				public void notify(Message message) {
					OKMessage okm = (OKMessage) message;
					
					VectorClock vclock = processConnection.getVclock();
					vclock.increment(pid);
					vclock.updateWith(okm.vclock);
					
					System.out.println("OK from " + okm.pid);
					waitingFor.remove(okm.pid);
					
					if(waitingFor.size() == 0){
						accessing = true;
						FakeResource.access();
						accessing = false;
						currentRequest = null;
						while(okQueue.peek() != null){
							ProcessConnection pc = okQueue.poll();
							sendOk(pc);
						}
					}
				}
			});
		}
	}
	
	public void receiveRequest(ProcessConnection pc, RequestMessage rm){
		
		VectorClock vclock = processConnection.getVclock();
		
		vclock.updateWith(rm.vclock);
		vclock.increment(pid);
		
		if(!accessing && (currentRequest == null || processConnection == pc)){
			sendOk(pc);
		}else{
			if(accessing){
				okQueue.add(pc);
			}else if(currentRequest != null){
				if(pc.getVclock().lowerThan(processConnection.getVclock())){
					sendOk(pc);
				}else{
					okQueue.offer(pc);
				}
			}
		}
	}
	
	
	
	private void sendOk(ProcessConnection pc){
		VectorClock vclock = processConnection.getVclock();
		vclock.increment(pid);
		
		OKMessage okm = new OKMessage();
		okm.resourceName = "exampleresource";
		okm.pid = pid;
		
		okm.vclock = vclock;
		
		pc.sendMessage(okm);
	}
}
