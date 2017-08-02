package osu.planisphere.messages;

import osu.planisphere.ActionResponse;
import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class ReportActionResponseMessage extends Message{

	private ActionResponse response;
	private Message message;
	
	public ReportActionResponseMessage(NodeIdentifier sender, ActionResponse response, Message message) {
		super(sender);
		this.response = response;
		this.message = message;
	}
	
	public ActionResponse getResponse(){
		return this.response;
	}
	
	public Message getMessage(){
		return this.message;
	}

}
