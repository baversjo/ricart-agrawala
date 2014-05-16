package ui;

import java.io.Serializable;

public interface Message extends Serializable{
	public void setRequestId(int id);
	public int getRequestId();
}
