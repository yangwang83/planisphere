package osu.planisphere.paxos;
import osu.planisphere.*;

public class P2aMessage extends Message{

	private int slot;
	private int epoch;
	private Request req;
	
	public P2aMessage(NodeIdentifier sender, int slot, int epoch, Request req) {
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
		return "P2aMessage slot="+slot+" epoch="+epoch+" req=<"+req+">";
	}

}
