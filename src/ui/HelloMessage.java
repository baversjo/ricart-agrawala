package ui;

import java.io.Serializable;

public class HelloMessage implements Serializable{
	public String pid;
	public HelloMessage(String pid){
		this.pid = pid;
	}
}
