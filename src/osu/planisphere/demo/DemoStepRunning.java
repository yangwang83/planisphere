package osu.planisphere.demo;

import osu.planisphere.EventHook;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
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
	
	public static class TestMessage extends Message{
		
		private int value;
		public TestMessage(NodeIdentifier sender, int value){
			super(sender);
			this.value = value;
		}
		
		@Override
		public String toString(){
			return value + " from "+sender;
		}
	}
	
	public static class Sender extends Node{

		public Sender(NodeIdentifier id, Network network) {
			super(id, 5000, network);
		}

		public void sendMessages(NodeIdentifier other){
			for(int i=0; i<5; i++)
				this.sendMessage(other, new TestMessage(this.getID(), i));
		}
		
		@Override
		public void handleTimer() {
		}

		@Override
		public void handleMessage(Message msg) {

		}
		
	}
	
	public static class Receiver extends Node{

		public Receiver(NodeIdentifier id, Network network) {
			super(id, 5000, network);
		}

		@Override
		public void handleTimer() {
		}

		@Override
		public void handleMessage(Message msg) {
			System.out.println(this.getID() + " hanldes "+msg);
		}
		
	}
	
	
	public static void main(String args[]) throws Exception{
		//Create the network and all nodes
		Network network = new Network();
		NodeIdentifier id1= new NodeIdentifier(Role.DEMO, 1);
		Sender sender = new Sender(id1, network);
		
		NodeIdentifier id2= new NodeIdentifier(Role.DEMO, 2);
		Receiver receiver = new Receiver(id2, network);
		
		StepRunning step = new StepRunning();
		sender.addEventHook(step);
		receiver.addEventHook(step);
		
		//Start experiment
		sender.start();
		receiver.start();
		sender.sendMessages(id2);
		
	}
	
}
