package ui;

import java.io.Serializable;

public interface ProcessConnection {
	
	public void sendMessage(Serializable object);
	
	public VectorClock getVclock();

}
