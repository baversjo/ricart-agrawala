package ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class VectorClock implements Serializable{
	private Map<String,Integer> vclock;
	
	
	public VectorClock(){
		vclock = new ConcurrentHashMap<String,Integer>();
	}
	
	public void updateWith(VectorClock other){
		for (Entry<String, Integer> entry : vclock.entrySet()) {
			String pid = entry.getKey();
			Integer val1 = entry.getValue();
			Integer val2 = other.vclock.get(pid);
			
			if(val2 == null){ val2 = 0; }
			
			vclock.put(pid, Math.max(val1, val2));
			
		}
		
		for (Entry<String, Integer> entry : other.vclock.entrySet()) {
			String pid = entry.getKey();
			Integer val1 = entry.getValue();
			Integer val2 = vclock.get(pid);
			
			if(val2 == null){ val2 = 0; }
			
			vclock.put(pid, Math.max(val1, val2));
			
		}

	}
	
	public void initialize(String pid){
		vclock.put(pid, 0);
	}
	
	public void increment(String pid){
		vclock.put(pid,vclock.get(pid) + 1);
	}
	
	public boolean lowerThan(VectorClock other){
		for (Entry<String, Integer> entry : vclock.entrySet()) {
			String pid = entry.getKey();
			if(!other.vclock.containsKey(pid)){
				other.vclock.put(pid, 0); //zero is default value
			}
		}
		
		for (Entry<String, Integer> entry : other.vclock.entrySet()) {
			String pid = entry.getKey();
			if(!vclock.containsKey(pid)){
				vclock.put(pid, 0); //zero is default value
			}
		}
		
		boolean hasBigger = false;
		boolean hasAtLeastOneSmaller = false;
		
		for (Entry<String, Integer> entry : vclock.entrySet()) {
			String pid = entry.getKey();
			int val1 = entry.getValue();
			int val2 = other.vclock.get(pid);
			if(val1 > val2){
				hasBigger = true;
				break;
			}
			if(val1 < val2){
				hasAtLeastOneSmaller = true;
			}
			
		}
		
		return hasAtLeastOneSmaller && !hasBigger;
	}
	
	public VectorClock copy(){
		VectorClock newClock = new VectorClock();
		for(Entry<String, Integer> entry : vclock.entrySet()){
			vclock.put(entry.getKey(), entry.getValue());
		}
		return newClock;
	}
}
