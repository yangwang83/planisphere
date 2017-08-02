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

	public static class HookAction{
		public ActionResponse response;
		public Message message;
		
		public HookAction(ActionResponse response, Message message) {
			this.response = response;
			this.message = message;
		}
	}
	
	public HookAction handleEvent(Timing timing, Action action, Message msg, NodeIdentifier node);
}
