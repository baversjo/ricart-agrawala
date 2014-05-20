package ui;


public class ThisProcessConnection implements ProcessConnection{

	private String pid;
	
	public VectorClock vclock;
	
	private ResponseEvent awaitingResponse;

	private RicartAgrawala ra;

	public ThisProcessConnection(String pid) {
		this.pid = pid;
		
		vclock = new VectorClock();
		vclock.initialize(pid);
	}

	@Override
	public void sendMessage(Message message) {
		handleMessage(message);
		
	}
	public void sendMessage(Message message, ResponseEvent e) {
		awaitingResponse = e;
		sendMessage(message);
		
	}
	
	private void handleMessage(Message o){
		ResponseEvent e = awaitingResponse;
		
		System.out.println("Loopback Received " + o.getClass().getName());
		
		if(o instanceof OKMessage && e != null){
			awaitingResponse = null;
			e.notify(o);
		}
		
		if(o instanceof RequestMessage){
			ra.receiveRequest(this, (RequestMessage)o);
		}
	}
	
	@Override
	public VectorClock getVclock() {
		return vclock;
	}
	
	public String toString(){
		return "pid '" + pid.substring(0, 3) + "' (self)";
	}

	public void setRicartAgrawala(RicartAgrawala ra) {
		this.ra = ra;
		
	}

	@Override
	public String getPid() {
		return pid;
	}

}
