package ui;

import java.io.Serializable;

public class RequestMessage implements Message{
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
