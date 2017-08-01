package osu.planisphere.messages;

import java.net.InetSocketAddress;

import osu.planisphere.Message;
import osu.planisphere.NodeIdentifier;

public class RegisterMessage extends Message {

	private InetSocketAddress address = null;
	public RegisterMessage(NodeIdentifier sender, InetSocketAddress address) {
		super(sender);
		if(address == null)
			throw new NullPointerException();
		this.address = address;
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}
}
