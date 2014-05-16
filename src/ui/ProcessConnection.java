package ui;

import java.io.Serializable;

public interface ProcessConnection {
	
	public void sendMessage(Message message);
	
	public VectorClock getVclock();

}
