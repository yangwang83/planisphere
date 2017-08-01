package osu.planisphere.messages;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class GetRegistryMessage extends Message {
	
	private NodeIdentifier target;

	public GetRegistryMessage(NodeIdentifier sender, NodeIdentifier target) {
		super(sender);
		this.target = target;
	}
	
	public NodeIdentifier getTarget() {
		return this.target;
	}

}
