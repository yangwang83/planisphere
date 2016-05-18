package osu.planisphere;

/**
 * Base class for all messages
 * @author Yang Wang
 *
 */
public class Message {

	public NodeIdentifier sender;
	
	public Message(NodeIdentifier sender){
		this.sender = sender;
	}
	
	public NodeIdentifier getSender(){
		return this.sender;
	}
}
