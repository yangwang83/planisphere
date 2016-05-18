package osu.planisphere.demo;

import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.Role;

/**
 * This demo shows how to create two nodes and let them exchange
 * messages. These two nodes work in a ping-pong way: the first
 * node sends a message to the second; the second sends it back ...
 * @author yangwang
 *
 */
public class DemoBasic {
	
	public static class PingPongMessage extends Message{
		
		private String str;
		public PingPongMessage(NodeIdentifier sender, String str){
			super(sender);
			this.str = str;
		}
		
		@Override
		public String toString(){
			return str + " from "+sender;
		}
	}
	
	public static class PingPongNode extends Node{

		public PingPongNode(NodeIdentifier id, Network network) {
			super(id, 5000, network);
		}

		public void sendOneMessage(NodeIdentifier other){
			this.sendMessage(other, new PingPongMessage(this.getID(), "first message"));
		}
		
		@Override
		public void handleTimer() {
			System.out.println(this.getID() + " timer event");
		}

		@Override
		public void handleMessage(Message msg) {
			System.out.println(this.getID() + " hanldes "+msg);
			try{
				Thread.sleep(1000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			this.sendMessage(msg.getSender(), new PingPongMessage(this.getID(), "haha"));
		}
		
	}
	
	public static void main(String args[]) throws Exception{
		//Create the network and all nodes
		Network network = new Network();
		NodeIdentifier id1= new NodeIdentifier(Role.DEMO, 1);
		PingPongNode node1 = new PingPongNode(id1, network);
		
		NodeIdentifier id2= new NodeIdentifier(Role.DEMO, 2);
		PingPongNode node2 = new PingPongNode(id2, network);
		
		//Start experiment
		node1.start();
		node2.start();
		node1.sendOneMessage(id2);
		
	}
	
}
