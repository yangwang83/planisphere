package osu.planisphere.messages;

import java.net.InetSocketAddress;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class GetRegistryReplyMessage extends Message {

	private NodeIdentifier target;
	private InetSocketAddress address;
	
	public GetRegistryReplyMessage(NodeIdentifier sender, NodeIdentifier target, InetSocketAddress address) {
		super(sender);
		this.target = target;
		this.address = address;
	}
	
	public NodeIdentifier getTarget() {
		return this.target;
	}
	
	public InetSocketAddress getAddress() {
		return this.address;
	}

}
