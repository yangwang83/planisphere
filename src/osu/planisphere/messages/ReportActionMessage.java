package osu.planisphere.messages;

import osu.planisphere.Action;
import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.Timing;

public class ReportActionMessage extends Message{
	
	private Timing timing;
	private Action action;
	private Message message;
	
	
	public ReportActionMessage(NodeIdentifier sender, Timing timing, Action action, Message message) {
		super(sender);
		this.timing = timing;
		this.action = action;
		this.message = message;
	}
	
	public Timing getTiming(){
		return this.timing;
	}
	
	public Action getAction(){
		return this.action;
	}
	
	public Message getMessage(){
		return this.message;
	}

	@Override
	public String toString() {
		return this.getSender()+" "+timing+" "+action+" "+message;
	}
}
