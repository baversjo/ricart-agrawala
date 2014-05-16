package ui;

import java.io.Serializable;

public class HelloMessage implements Message{
	public String pid;
	
	private int requestId;
	
	public HelloMessage(String pid){
		this.pid = pid;
	}
	@Override
	public void setRequestId(int id) {
		requestId = id;
		
	}
	@Override
	public int getRequestId() {
		return requestId;
	}
}
