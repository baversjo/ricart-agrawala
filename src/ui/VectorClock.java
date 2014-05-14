package ui;

import java.io.Serializable;
import java.util.HashMap;

public class VectorClock implements Serializable{
	public HashMap<String,Integer> vclock;
	
	public boolean updateWith(VectorClock other){
		return false;
		
	}
}
