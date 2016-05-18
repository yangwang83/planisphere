package osu.planisphere.paxos;
import osu.planisphere.*;

public class Request extends Message{

	private int type;
	private int key;
	private int value;
	
	private int clientID;
	private int clientSequenceID;
	/*
	 * Read request: type = 0; key is key; value is not used
	 * Write request: type = 1; key is key; value is value
	 */
	public Request(NodeIdentifier sender, int type, int key, int value, int clientSequenceID){
		super(sender);
		this.type = type;
		this.key = key;
		this.value = value;
		
		this.clientID = sender.getID();
		this.clientSequenceID = clientSequenceID;
	}
	
	public int getType(){
		return type;
	}
	
	public int getKey(){
		return key;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getClientID(){
		return clientID;
	}
	
	public int getClientSequenceID(){
		return clientSequenceID;
	}
	
	@Override
	public String toString(){
		if(type == 0){
			return "READ key="+key+" clientID="+clientID+" clientSequenceID="+clientSequenceID;
		}
		else{
			return "WRITE key="+key+" value="+value+" clientID="+clientID+" clientSequenceID="+clientSequenceID;
		}
	}
}
