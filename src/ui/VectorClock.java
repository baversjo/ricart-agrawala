package ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VectorClock implements Serializable{
	private Map<String,Integer> vclock;
	
	
	public VectorClock(){
		vclock = new ConcurrentHashMap<String,Integer>();
	}
	
	public boolean updateWith(VectorClock other){
		return false;
		
	}
	
	public void initialize(String pid){
		vclock.put(pid, 0);
	}
	
	public void increment(String pid){
		vclock.put(pid,vclock.get(pid) + 1);
	}
}
