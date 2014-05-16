package ui;

public class OKMessage implements Message{
	public String resourceName;
	public String pid;
	public VectorClock vclock;
	private int requestId;
	
	
	@Override
	public void setRequestId(int id) {
		requestId = id;
		
	}
	@Override
	public int getRequestId() {
		return requestId;
	}

}
