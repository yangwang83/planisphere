package osu.planisphere.paxos;
import java.util.HashSet;

import osu.planisphere.*;

public class Quorum {
	
	private Message msg;
	private int expected;
	private HashSet<NodeIdentifier> received = new HashSet<NodeIdentifier>();
	
	public Quorum(Message msg, int expected){
		this.msg = msg;
		this.expected = expected;
	}
	
	public void addReceipt(NodeIdentifier id){
		received.add(id);
	}
	
	public boolean isReady(){
		return received.size() >= expected;
	}
	
	public Message getMessage(){
		return msg;
	}
}
