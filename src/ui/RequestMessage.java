package ui;

import java.io.Serializable;

public class RequestMessage implements Message{
	public String resourceName;
	public String pid;
	public VectorClock vclock;


}
