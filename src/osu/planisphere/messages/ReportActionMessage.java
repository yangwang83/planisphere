package osu.planisphere.messages;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class ReportActionMessage extends Message{
	
	public static enum Timing{before, after};
	public static enum Action{send, handle};
	
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

}
