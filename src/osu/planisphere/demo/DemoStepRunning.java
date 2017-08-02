package osu.planisphere.demo;

import osu.planisphere.EventHook;
import osu.planisphere.MasterNode;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
import osu.planisphere.Role;
import osu.planisphere.StepRunning;

/**
 * This demo shows how to create two nodes and let them exchange
 * messages. These two nodes work in a ping-pong way: the first
 * node sends a message to the second; the second sends it back ...
 * @author yangwang
 *
 */
public class DemoStepRunning {
	
	
	public static void main(String args[]) throws Exception{
		
		EventHook hook = new StepRunning();
		MasterNode master = new MasterNode(2, hook);
		master.start();
		
//		Process p1 = Runtime.getRuntime().exec("java osu.planisphere.demo.Receiver 2");
//		Process p2 = Runtime.getRuntime().exec("java osu.planisphere.demo.Sender 2");
		
		
	}
	
}
