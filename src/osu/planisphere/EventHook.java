package osu.planisphere;

/**
 * This is the class to help inject errors into
 * a node. You can use this class to ask a node
 * to drop, re-order, or duplicate messages.
 * 
 * To use this functionality, one should create
 * an EventHook implementation and register to
 * the corresponding node.
 * 
 * See examples in demo.
 * 
 * @author Yang Wang
 *
 */
public interface EventHook {

	/**
	 * This function will be called when a node receives
	 * a message. You should call node.handleMessage if you
	 * want to process this message normally, or you can drop this message
	 * by returning directly. You can design more sophisticated
	 * errors by keeping more information.
	 * 
	 * @param msg
	 * @param node
	 */
	public void handleMessage(Message msg, Node node);
	
	/**
	 * This function will be called when a timer event
	 * is triggered. You should call node.handleTimer if
	 * you want it to be processed normally.
	 * @param node
	 */
	public void handleTimer(Node node);
	
	/**
	 * This function will be called when the node is
	 * sending a message. You should call node.sendMessageInternal if
	 * you want it to be sent normally.
	 * @param node
	 */
	public void sendMessage(NodeIdentifier receiver, Message msg, Node node);
}
