package ui;


public interface ProcessConnection {
	
	public void sendMessage(Message message);
	public void sendMessage(Message message, ResponseEvent e);
	
	public VectorClock getVclock();
	
	public String getPid();

}
