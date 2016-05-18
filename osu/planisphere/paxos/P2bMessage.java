package osu.planisphere.paxos;
import osu.planisphere.*;

public class P2bMessage extends Message{

	private int slot;
	private int epoch;
	private boolean accepted;
	
	public P2bMessage(NodeIdentifier sender, int slot, int epoch, boolean accepted) {
		super(sender);
		this.slot = slot;
		this.epoch = epoch;
		this.accepted = accepted;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public int getEpoch(){
		return epoch;
	}
	
	public boolean isAccepted(){
		return accepted;
	}
	
	@Override
	public String toString(){
		return "P2bMessage slot="+slot+" epoch="+epoch+" accepted="+accepted;
	}

}
