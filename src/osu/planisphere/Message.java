package osu.planisphere;

import java.io.Serializable;

/**
 * Base class for all messages
 * @author Yang Wang
 *
 */
public class Message implements Serializable{

	public NodeIdentifier sender;
	
	public Message(NodeIdentifier sender){
		this.sender = sender;
	}
	
	public NodeIdentifier getSender(){
		return this.sender;
	}
}
