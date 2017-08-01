package osu.planisphere.demo;

import java.net.Socket;

import osu.planisphere.MasterNode;
import osu.planisphere.Message;
import osu.planisphere.Network;
import osu.planisphere.Node;
import osu.planisphere.NodeIdentifier;
import osu.planisphere.NormalNode;
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
	
	public static class PingPongNode extends NormalNode{

		public PingPongNode(NodeIdentifier id) {
			super(id, 5000);
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
		NodeIdentifier masterID = new NodeIdentifier(Role.MASTER, 1);
		MasterNode masterNode = new MasterNode(masterID);
		
		NodeIdentifier id1= new NodeIdentifier(Role.DEMO, 1);
		PingPongNode node1 = new PingPongNode(id1);
		
		NodeIdentifier id2= new NodeIdentifier(Role.DEMO, 2);
		PingPongNode node2 = new PingPongNode(id2);
		
		node1.sendOneMessage(id2);
		
	}
	
}
