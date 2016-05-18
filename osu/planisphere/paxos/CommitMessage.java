package osu.planisphere.paxos;
import osu.planisphere.*;

public class CommitMessage extends Message{

	private int slot;
	private int epoch;
	private Request req;
	
	public CommitMessage(NodeIdentifier sender, int slot, int epoch, Request req) {
		super(sender);
		this.slot = slot;
		this.epoch = epoch;
		this.req = req;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public int getEpoch(){
		return epoch;
	}
	
	public Request getRequest(){
		return req;
	}
	
	@Override
	public String toString(){
		return "CommitMessage slot="+slot+" epoch="+epoch+" req=<"+req+">";
	}

}
