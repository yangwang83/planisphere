package osu.planisphere.demo;

import osu.planisphere.Action;
import osu.planisphere.ActionResponse;
import osu.planisphere.EventHook;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
import osu.planisphere.Role;
import osu.planisphere.Timing;
import osu.planisphere.EventHook.HookAction;

/**
 * This demo shows how to create two nodes and let them exchange
 * messages. These two nodes work in a ping-pong way: the first
 * node sends a message to the second; the second sends it back ...
 * @author yangwang
 *
 */
public class DemoKillNode {
	
	public static class KillNodeAfterSecondMessage implements EventHook{

		private int count = 0;
		
		@Override
		public HookAction handleEvent(Timing timing, Action action, Message msg, NodeIdentifier node) {
			if(timing == Timing.before && action == Action.handle) {
				count++;
				if(count==3)
					return new HookAction(ActionResponse.killnode, null);
			}
			return new HookAction(ActionResponse.doit, null); 
		}
		
	}
	
	public static void main(String args[]) throws Exception{
		//Create the network and all nodes
		int debugMode = 0;
		NodeIdentifier id1= new NodeIdentifier(Role.DEMO, 1);
		Sender sender = new Sender(id1, debugMode);
		
		NodeIdentifier id2= new NodeIdentifier(Role.DEMO, 2);
		Receiver receiver = new Receiver(id2, debugMode);
		
		//You can try to comment this line to check the difference
		//receiver.addEventHook(new KillNodeAfterSecondMessage());
		
		//Start experiment
		sender.start();
		receiver.start();
		sender.sendMessages(id2);
		
	}
	
}
