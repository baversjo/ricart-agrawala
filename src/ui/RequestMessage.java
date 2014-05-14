package ui;

import java.io.Serializable;

public class RequestMessage implements Serializable{
	public String resourceName;
	public String pid;
	public VectorClock vclock;

}
