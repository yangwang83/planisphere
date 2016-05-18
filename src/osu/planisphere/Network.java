package osu.planisphere;

import java.util.HashMap;

/**
 * A pseudo network to transfer messages among nodes
 * @author Yang Wang
 *
 */
public class Network {
	
	private HashMap<NodeIdentifier, Node> nodes = new HashMap<NodeIdentifier, Node>();
	
	public void registerNode(NodeIdentifier id, Node node){
		nodes.put(id, node);
	}
	
	public void sendMessage(NodeIdentifier receiver, Message msg){
		if(!nodes.containsKey(receiver))
			throw new RuntimeException(receiver+" not found");
		nodes.get(receiver).enqueueMessage(msg);
	}
}
